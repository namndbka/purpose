//
//  ViewController.swift
//  eSurvey
//
//  Created by Nam Nguyen on 1/12/18.
//  Copyright © 2018 Unilever. All rights reserved.
//

import UIKit

class HomeViewController: UIViewController {

    @IBOutlet weak var subscibeEmail: UITextField!
    
    @IBOutlet weak var versionLabel: UILabel!
    var internetConntected: Bool = false
    
    override func viewDidLoad() {
        super.viewDidLoad()
        NotificationCenter.default.addObserver(self, selector: #selector(statusManager), name: .flagsChanged, object: Network.reachability)
        updateUserInterface()
        if let version = Bundle.main.infoDictionary?["CFBundleShortVersionString"] as? String {
            self.versionLabel.text = "Version: \(version)"
        }
        let emailStr = DataManager.getEmail()
        self.subscibeEmail.text = emailStr
    }
    
    func updateUserInterface() {
        guard let status = Network.reachability?.status else {
            return
        }
        switch status {
        case .unreachable:
            self.internetConntected = false || (TARGET_OS_SIMULATOR != 0)
        case .wifi:
            self.internetConntected = true
        case .wwan:
            self.internetConntected = true
        }
    }
    
    @objc func statusManager(_ notification: NSNotification){
        self.updateUserInterface()
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    @IBAction func subscribeActionClicked() {
        // do something
        let email = self.subscibeEmail.text!
        if(email.isValidUnileverEmail()){
            self.subscribeEmail(email)
        } else {
            showMessage(Phrase.from("email_invalidate").description)
        }
    }
    private func subscribeEmail(_ email: String){
        if(self.internetConntected) {
            ISurveyApi.subscribeEmail(email, onSuccess: { (surveyRespon) in
                guard let _ = surveyRespon.errorMsg else {
                    DataManager.saveEmail(email)
                    DataManager.saveSubscribe()
                    if(surveyRespon.answers != nil) {
                        if(!DataManager.isNeedPostAnswer()) {
                            self.firstLoadSaved(surveyRespon.answers!)
                        }
                        DataManager.needPostAnswer()
                    }
                    self.gotoMainScreen()
                    return
                }
            }) { (error) in
                print (error.localizedDescription)
            }
        } else {
            DataManager.saveEmail(email)
            self.gotoMainScreen()
        }
    }
    private func firstLoadSaved(_ answers: [AnswerRequest]) {
        for answer in answers {
            if(answer.completed == 100) { DataManager.saveIntroRead(answer.sid) }
            if(answer.answers != nil) {
                let ans = answer.answers.convertToAnswerDict()
                if(ans != nil) {
                    for (key, value) in ans! {
                        DataManager.saveAnswer(answer.sid, questionIndex: Int(key)!, answer: value)
                    }
                }
            }
        }
    }
    func showMessage(_ msg: String) {
        let alert = UIAlertController(title: nil, message: msg, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: Phrase.from("dimmis").description, style: .default))
        self.present(alert, animated: true, completion: nil)
    }
    
    func gotoMainScreen() {
        AppDelegate.sharedAppDelegate().switchRootViewController(R.storyboard.main.mainNavigationController()!, animated: true, completion: nil)
    }
}

