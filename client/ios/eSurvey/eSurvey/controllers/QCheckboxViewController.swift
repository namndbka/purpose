//
//  QCheckboxViewController.swift
//  eSurvey
//
//  Created by Nam Nguyen on 1/15/18.
//  Copyright © 2018 Unilever. All rights reserved.
//

import UIKit
import GTProgressBar

class QCheckboxViewController: UIViewController {
    
    var mQuestion: Question!
    var mSurveyProperties: SurveyProperties!
    var mQuestionsCount: Int = 0
    
    private var choiceViews = [ChoiceRowView]()
    @IBOutlet weak var scrollView: UIScrollView!
    @IBOutlet weak var questionContent : UITextView!
    @IBOutlet weak var answerLayout: VerticalLayout!
    @IBOutlet weak var backButton: UIButton!
    @IBOutlet weak var nextButton: UIButton!
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var headerView: UIHeaderView!
    
    @IBOutlet weak var sentButtonTopConstraint: NSLayoutConstraint!
    @IBOutlet weak var sendButtonBottonContraint: NSLayoutConstraint!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.titleLabel.text = String(mQuestion.index + 1) + "/" + String(mQuestionsCount)
        self.updateHeaderView()
        self.updateBottomView()
        self.questionContent.attributedText = mQuestion.questionTitle.htmlAttributedString()
        self.questionContent.sizeToFit()
        self.initAnswerView()
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

    private func updateHeaderView() {
        self.headerView.indexText = String(mSurveyProperties.id)
        self.headerView.titleText = mSurveyProperties.title
        self.headerView.progress = 0.8
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
    
    private func initAnswerView() {
        let choices = mQuestion.choices.sorted()
        let saveAnswers = DataManager.getAnswer(mSurveyProperties.id, questionIndex: mQuestion.index)
        if(choices.count > 5) {
            let bounds = self.answerLayout.bounds
            let oldHeight = bounds.height
            for i in 1...mQuestion.max {
                let choiceRow = ChoiceRowView(frame: CGRect(x: 8, y: 6, width: Int(bounds.width),height: 40))
                choiceRow.autoresizingMask = .flexibleWidth
                choiceRow.bounds = choiceRow.frame.insetBy(dx: 0, dy: 0)
                choiceRow.indexText = String(i)
                choiceRow.updateList(data: choices.sorted())
                if(!saveAnswers.isEmpty && i <= saveAnswers.count) { choiceRow.answerValue = saveAnswers }
                self.answerLayout.addSubview(choiceRow)
                self.choiceViews.append(choiceRow)
            }
            self.answerLayout.layoutIfNeeded()
            let adjustment = self.answerLayout.bounds.height - oldHeight
            self.sentButtonTopConstraint.constant = self.sentButtonTopConstraint.constant + adjustment
            self.sendButtonBottonContraint.constant = self.sendButtonBottonContraint.constant - adjustment
        }
        
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
    
    @IBAction func sendButtonClicked(_ sender: Any) {
        let answer = ""
        var answerOk = false
        for choiceView in choiceViews {
            var answer = choiceView.answerValue!
            if(!answer.isEmpty) { answer += ";" + answer }
            if(!answer.isEmpty) { answerOk = true }
        }
        if(answerOk) {
            DataManager.needPostAnswer()
            DataManager.saveAnswer(mSurveyProperties.id, questionIndex: mQuestion.index, answer: answer)
            if(mQuestion.index < (mQuestionsCount - 1)) { nextActionClicked() } else { homeActionClicked() }
        } else { showAlert(Phrase.from("answer_empty").description) }
        
    }
    
    @IBAction func homeActionClicked() {
        self.navigationController?.popViewController(animated: true)
    }
    
    private func showAlert(_ msg: String) {
        let alert = UIAlertController(title: nil, message: msg, preferredStyle: UIAlertControllerStyle.alert)
        alert.addAction(UIAlertAction(title: Phrase.from("dimmis").description, style: UIAlertActionStyle.default, handler: nil))
        self.present(alert, animated: true, completion: nil)
    }
}
