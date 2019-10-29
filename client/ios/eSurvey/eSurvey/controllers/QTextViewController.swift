//
//  QuestionViewController.swift
//  eSurvey
//
//  Created by Nam Nguyen on 1/13/18.
//  Copyright © 2018 Unilever. All rights reserved.
//

import UIKit
import GTProgressBar

class QTextViewController: UIViewController {
    
    var mQuestion: Question!
    var mSurveyProperties: SurveyProperties!
    var mQuestionsCount: Int = 0
    @IBOutlet weak var questionContent : UITextView!
    @IBOutlet weak var answerTextView : GrowingTextView!
    @IBOutlet weak var backButton: UIButton!
    @IBOutlet weak var nextButton: UIButton!
    @IBOutlet weak var titleLabel: UILabel!
    
    @IBOutlet weak var headerView: UIHeaderView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.titleLabel.text = String(mQuestion.index + 1) + "/" + String(mQuestionsCount)
        self.updateHeaderView()
        self.updateBottomView()
        self.questionContent.attributedText = mQuestion.questionTitle.htmlAttributedString()
        self.questionContent.sizeToFit()
        self.initAnswerTextView()
    }
    
    private func initAnswerTextView(){
        self.answerTextView.layer.borderColor = UIColor.init(hexString: "#FBC02D").cgColor
        self.answerTextView.layer.borderWidth = 1.0
        self.answerTextView.layer.cornerRadius = 3.0
        self.answerTextView.placeHolder = Phrase.from("answer_hint").description
        self.answerTextView.text = DataManager.getAnswer(mSurveyProperties.id, questionIndex: mQuestion.index)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(true)
        self.updateHeaderProgress()
    }
    
    private func updateHeaderProgress(){
        var progress = 0
        for index in 0..<mQuestionsCount {
            let ans = DataManager.getAnswer(mSurveyProperties.id, questionIndex: index)
            if(!ans.isEmpty) { progress += 1 }
        }
        self.headerView.progress = CGFloat(progress)/CGFloat(mQuestionsCount)
    }
    
    private func updateHeaderView(){
        self.headerView.indexText = String(mSurveyProperties.id)
        self.headerView.titleText = mSurveyProperties.title
        self.headerView.homeClick(self, action: #selector(self.homeActionClicked))
    }
    private func updateBottomView() {
        let backBtnHide = (mQuestion.index == 0)
        let nextBtnHide = (mQuestion.index >= (mQuestionsCount - 1))
        if(backBtnHide && !nextBtnHide) {
            self.titleLabel.padding = UIEdgeInsets(top: 0, left: 70, bottom: 0, right: 0)
        }
        if(!backBtnHide && nextBtnHide) {
            self.titleLabel.padding = UIEdgeInsets(top: 0, left: 0, bottom: 0, right: 70)
        }
        self.backButton.isHidden = backBtnHide
        self.nextButton.isHidden = nextBtnHide
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    @IBAction func backActionClicked() {
        let parentView = self.parent as! SurveyViewController
        let index = mQuestion.index - 1
        parentView.setViewControllers([parentView.mOrderedViewControllers[index]], direction: .reverse, animated: true, completion: nil)
    }
    
    @IBAction func nextActionClicked() {
        let parentView = self.parent as! SurveyViewController
        let index = mQuestion.index + 1
        parentView.setViewControllers([parentView.mOrderedViewControllers[index]], direction: .forward, animated: true, completion: nil)
    }
    
    @IBAction func sendButtonClick(_ sender: Any) {
        if(answerTextView.validate()) {
            let answerString = answerTextView.text
            DataManager.saveAnswer(mSurveyProperties.id, questionIndex: mQuestion.index, answer: answerString!)
            DataManager.needPostAnswer()
            showAlert(Phrase.from("answer_sent").description, warning: false)
        }
        else {
            showAlert(Phrase.from("answer_empty").description)
        }
    }
    
    @IBAction func homeActionClicked() {
        self.navigationController?.popViewController(animated: true)
    }
    
    func actionOK(action: UIAlertAction) {
        if(mQuestion.index < (mQuestionsCount - 1)) { nextActionClicked() }
        else { homeActionClicked() }
    }
    private func showAlert(_ msg: String, warning: Bool = true) {
        let alert = UIAlertController(title: "", message: msg, preferredStyle: UIAlertControllerStyle.alert)
        var action = "OK"
        if(warning) { action = Phrase.from("dimmis").description }
        if(warning) {
            alert.addAction(UIAlertAction(title: action, style: UIAlertActionStyle.destructive, handler: nil))
        } else {
            alert.addAction(UIAlertAction(title: action, style: UIAlertActionStyle.default, handler: actionOK))
        }
        self.present(alert, animated: true, completion: nil)
    }
    
}
extension QTextViewController : GrowingTextViewDelegate {
    func textViewDidChangeHeight(_ textView: GrowingTextView, height: CGFloat) {
        UIView.animate(withDuration: 0.2) {
            self.view.layoutIfNeeded()
        }
    }
}
