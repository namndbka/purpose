//
//  MainViewController.swift
//  eSurvey
//
//  Created by Nam Nguyen on 1/12/18.
//  Copyright © 2018 Unilever. All rights reserved.
//

import UIKit
import GTProgressBar
import ObjectMapper

class MainViewController: UIViewController {
    
    fileprivate var mSurveyProperties = [SurveyProperties]()
    
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var progressBar: GTProgressBar!
    @IBOutlet weak var tableView: UITableView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.titleLabel.text = Phrase.from("your_overall").description
        self.loadSurveys()
    
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(true)
        navigationController?.setNavigationBarHidden(true, animated: true)
        self.tableView.reloadData()
        let needPost = DataManager.isNeedPostAnswer()
        self.updateProgressBar(needPost)
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(true)
        navigationController?.setNavigationBarHidden(false, animated: false)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if(segue.identifier == R.segue.mainViewController.gotoSurveyDetail.identifier) {
            let surv = segue.destination as! SurveyViewController
            let survey = sender as! SurveyProperties
            surv.mSurvey = survey
            surv.mSurveyProperties = mSurveyProperties
        }
    }
    
    private func loadSurveys(){
        let path = Bundle.main.path(forResource: "survey_properties", ofType: "json")
        let url = URL(fileURLWithPath: path!)
        do {
            let data = try Data(contentsOf: url, options: NSData.ReadingOptions.mappedIfSafe)
            let jsons = try JSONSerialization.jsonObject(with: data) as? [[String: Any]]
            self.mSurveyProperties =  Mapper<SurveyProperties>().mapArray(JSONArray: jsons!)
            self.tableView.reloadData()
            self.updateProgressBar()
        } catch {}
    }

    private func updateProgressBar(_ postAnswer: Bool=false) {
        let surveyCount = mSurveyProperties.count
        if(surveyCount == 0) {
            self.progressBar.progress = 0
        } else {
            var complete = 0
            var answerRequest = [AnswerRequest]()
            var questionRemain = 0
            for survey in mSurveyProperties {
                let ansReq = AnswerRequest()
                ansReq.sid = survey.id
                if(survey.id == 0){
                    if(DataManager.isIntroRead(survey.id)){
                        complete += 1
                        ansReq.completed = 100 // 100 percent
                    }
                } else {
                    let questionCount = DataManager.getQuestionCount(survey.id)
                    if (questionCount > 0) {
                        var progress = 0
                        var dict = Dictionary<String, String>.init()
                        for index in 0..<questionCount {
                            let ans = DataManager.getAnswer(survey.id, questionIndex: index)
                            if(!ans.isEmpty) {
                                dict[String(index)] = ans
                                progress += 1
                            } else {
                                questionRemain += 1
                            }
                        }
                        ansReq.answers = dict.toJSONString()!
                        if(progress == questionCount) {
                            complete += 1
                            ansReq.completed = 100 // 100 percent
                        } else {
                            ansReq.completed = Int((CGFloat(progress)/CGFloat(questionCount))*100)
                        }
                    }
                }
                answerRequest.append(ansReq)
            }
            let progress = CGFloat(complete)/CGFloat(surveyCount)
            DataManager.saveQuestionRemin(questionRemain)
            self.progressBar.progress = progress
            if(postAnswer) {
                let answerReqString = Mapper().toJSONString(answerRequest)
                self.answersPost(answerReqString!, completed: Int(progress*100))
                notificationInitter()
            }
        }
    }
    private func answersPost(_ answers: String, completed: Int){
        let email = DataManager.getEmail()
        ISurveyApi.answerSurvery(email!, answers: answers, completed: completed, onSuccess: { (surveyRespon) in
            guard let errorMsg = surveyRespon.errorMsg else {
                DataManager.needPostAnswer(false)
                return
            }
            print(errorMsg)
        }) { (error) in
            print (error.localizedDescription)
        }
    }
    private func notificationInitter() {
        if LocalNotificationHelper().checkNotificationEnabled() == true {
            // If local notifications are enabled, schedule the notification, write to the Text View and set the Switch to ON
            LocalNotificationHelper().schedule(atHour: 7, atMinute: 30)
            LocalNotificationHelper().schedule(atHour: 20, atMinute: 30)
        } else {
            displayNotificationsDisabled()
        }
    }
    
    private func displayNotificationsDisabled() {
        let alertController = UIAlertController(
            title: "Notifications disabled for this app",
            message: "Please enable Notifications in Settings -> Notifications -> Purpose Discovery",
            preferredStyle: UIAlertControllerStyle.alert)
        alertController.addAction(UIAlertAction(
            title: Phrase.from("dimmis").description,
            style: UIAlertActionStyle.default,
            handler: nil))
        self.present(alertController, animated: true, completion: nil)
    }
}

extension MainViewController: UITableViewDataSource, UITableViewDelegate {

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.mSurveyProperties.count
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let row = indexPath.row
        let survey = self.mSurveyProperties[row]
        let cell = tableView.dequeueReusableCell(withIdentifier: R.reuseIdentifier.survey)
        cell!.itemIndicator.setTitle(String(row), for: .normal)
        cell!.itemTitle.text = survey.title
        if(survey.id == 0) {
            if(DataManager.isIntroRead(survey.id)) { cell!.itemProgressBar.progress = 1 }
            else { cell!.itemProgressBar.progress = 0 }
        } else {
            let questionCount = DataManager.getQuestionCount(survey.id)
            if (questionCount > 0){
                var progress = 0
                for index in 0..<questionCount {
                    let ans = DataManager.getAnswer(survey.id, questionIndex: index)
                    if(!ans.isEmpty) {
                        progress += 1
                    }
                }
                cell!.itemProgressBar.progress = CGFloat(progress)/CGFloat(questionCount)
            } else {
                cell!.itemProgressBar.progress = 0
            }
        }
        
        return cell!
    }
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        let survey = self.mSurveyProperties[indexPath.row]
        self.performSegue(withIdentifier: R.segue.mainViewController.gotoSurveyDetail.identifier, sender: survey)
    }
}

class SurveyTableViewCell: UITableViewCell {
    @IBOutlet weak var itemIndicator: UIButton!
    @IBOutlet weak var itemTitle: UILabel!
    @IBOutlet weak var itemProgressBar: GTProgressBar!
}

