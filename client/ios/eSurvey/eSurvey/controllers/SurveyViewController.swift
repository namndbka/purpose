//
//  SurveyViewController.swift
//  eSurvey
//
//  Created by Nam Nguyen on 1/12/18.
//  Copyright © 2018 Unilever. All rights reserved.
//

import UIKit
import ObjectMapper

class SurveyViewController: UIPageViewController {
    
    var mSurvey : SurveyProperties!
    var mSurveyProperties = [SurveyProperties]()
    fileprivate var mQuestions = [Question]()
    
    var mOrderedViewControllers = [UIViewController]()
//    @IBOutlet weak var titleLabel : UILabel!
//    @IBOutlet weak var scrollView : UIScrollView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.dataSource = self
        self.delegate = self
        self.loadQuestions(surveyId: mSurvey.id)
        self.initOrderedViewController()
        if let firstViewController = mOrderedViewControllers.first {
            setViewControllers([firstViewController],
                               direction: .forward,
                               animated: true,
                               completion: nil)
        }
    }
    
    func nextSurveyLoad(){
        let currentIndex = mSurvey.id
        if(currentIndex < (mSurveyProperties.count - 1 )) {
            self.mSurvey = mSurveyProperties[currentIndex + 1]
            self.loadQuestions(surveyId: mSurvey.id)
            mOrderedViewControllers = [UIViewController]()
            self.initOrderedViewController()
            if let firstViewController = mOrderedViewControllers.first {
                setViewControllers([firstViewController],
                                   direction: .forward,
                                   animated: true,
                                   completion: nil)
            }
            print("reload")
        }
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(true)
        navigationController?.setNavigationBarHidden(true, animated: true)
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(true)
        navigationController?.setNavigationBarHidden(false, animated: false)
    }
    
    private func initOrderedViewController(){
        DataManager.saveQuestionCount(mSurvey.id, count: mQuestions.count)
        if(mSurvey.skipIntro){
            let introViewController = self.storyboard?.instantiateViewController(withIdentifier: "IntroViewController") as! IntroViewController
            introViewController.mSurveyProperties = mSurvey
            mOrderedViewControllers.append(introViewController)
        }
        for question in mQuestions {
            if(question.questionType == "Video"){
                let qVideoViewController = self.storyboard?.instantiateViewController(withIdentifier: "QVideoViewController") as! QVideoViewController
                qVideoViewController.mQuestion = question
                qVideoViewController.mQuestionsCount = mQuestions.count
                qVideoViewController.mSurveyProperties = mSurvey
                mOrderedViewControllers.append(qVideoViewController)
            } else if(question.questionType == "Checkboxes") {
                let qCheckViewController = self.storyboard?.instantiateViewController(withIdentifier: "QCheckboxViewController") as! QCheckboxViewController
                qCheckViewController.mQuestion = question
                qCheckViewController.mQuestionsCount = mQuestions.count
                qCheckViewController.mSurveyProperties = mSurvey
                mOrderedViewControllers.append(qCheckViewController)
            } else {
                let qTextViewController = self.storyboard?.instantiateViewController(withIdentifier: "QTextViewController") as! QTextViewController
                qTextViewController.mQuestion = question
                qTextViewController.mQuestionsCount = mQuestions.count
                qTextViewController.mSurveyProperties = mSurvey
                mOrderedViewControllers.append(qTextViewController)
            }
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    private func loadQuestions(surveyId: Int){
        let path = Bundle.main.path(forResource: "example_survey_\(surveyId)", ofType: "json")
        let url = URL(fileURLWithPath: path!)
        do {
            let data = try Data(contentsOf: url, options: NSData.ReadingOptions.mappedIfSafe)
            let jsons = try JSONSerialization.jsonObject(with: data) as? [[String: Any]]
            self.mQuestions =  Mapper<Question>().mapArray(JSONArray: jsons!)
        } catch {}
    }
}

extension SurveyViewController : UIPageViewControllerDataSource {
    
    func pageViewController(_ pageViewController: UIPageViewController, viewControllerBefore viewController: UIViewController) -> UIViewController? {
        guard let viewControllerIndex = mOrderedViewControllers.index(of: viewController) else {
            return nil
        }
        let previousIndex = viewControllerIndex - 1
        guard previousIndex >= 0 else {
            return nil
        }
        guard mOrderedViewControllers.count > previousIndex else {
            return nil
        }
        return mOrderedViewControllers[previousIndex]
    }
    
    func pageViewController(_ pageViewController: UIPageViewController, viewControllerAfter viewController: UIViewController) -> UIViewController? {
        guard let viewControllerIndex = mOrderedViewControllers.index(of: viewController) else {
            return nil
        }
        let nextIndex = viewControllerIndex + 1
        let orderedViewControllersCount = mOrderedViewControllers.count
        
        guard orderedViewControllersCount != nextIndex else {
            return nil
        }
        guard orderedViewControllersCount > nextIndex else {
            return nil
        }
        return mOrderedViewControllers[nextIndex]
    }
}

extension SurveyViewController: UIPageViewControllerDelegate {}
