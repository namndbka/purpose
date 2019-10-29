//
//  IntroViewController.swift
//  eSurvey
//
//  Created by Nam Nguyen on 1/14/18.
//  Copyright © 2018 Unilever. All rights reserved.
//

import UIKit
class IntroViewController: UIViewController {
    
    @IBOutlet weak var contentIntroView : UITextView!
    @IBOutlet weak var startButton: UIButton!
    var mSurveyProperties: SurveyProperties!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.startButton.setTitle(Phrase.from("start_action").description.uppercased(), for: .normal)
        self.contentIntroView.attributedText = mSurveyProperties.introMessage.htmlAttributedString()
    }
    
    @IBAction func startActionClicked() {
        DataManager.needPostAnswer(!DataManager.isIntroRead(mSurveyProperties.id))
        DataManager.saveIntroRead(mSurveyProperties.id)
        self.navigationController?.popViewController(animated: true)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
}
