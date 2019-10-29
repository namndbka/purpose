//
//  ChoiceRow.swift
//  eSurvey
//
//  Created by Nam Nguyen on 1/15/18.
//  Copyright © 2018 Unilever. All rights reserved.
//

import UIKit

@IBDesignable class ChoiceRowView: UIView {

    @IBOutlet var choiceView: UIView!
    @IBOutlet weak var indexButton: UIButton!
    @IBOutlet weak var answerView: UITextField!

    
    var choices: [String]!
    
    @IBInspectable
    var indexText: String? {
        get {
            return indexButton.title(for: .normal)
        }
        set(indexText) {
            indexButton.setTitle(indexText, for: .normal)
        }
    }

    @IBInspectable
    var answerValue: String? {
        get {
            return answerView.text
        }
        set(answerValue) {
            answerView.text = answerValue
        }
    }
    
    func updateList(data: [String]) {
        self.choices = data
    }
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        self.configureXIB()
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        self.configureXIB()
    }
    //MARK: - Custom Methods
    func configureXIB() {
        choiceView = configureNib()
        // use bounds not frame or it'll be offset
        choiceView.frame = bounds
        // Make the flexible view
        choiceView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        // Adding custom subview on top of our view (over any custom drawing > see note below)
        addSubview(choiceView)
        self.answerView.delegate = self
        self.answerView.placeholder = Phrase.from("enter_here").description
    }
    
    func configureNib() -> UIView {
        let bundle = Bundle(for: type(of: self))
        let nib = UINib(nibName: "ChoiceRowView", bundle: bundle)
        let view = nib.instantiate(withOwner: self, options: nil)[0] as! UIView
        return view
    }
}
extension ChoiceRowView : UITextFieldDelegate {
    
    func textFieldShouldBeginEditing(_ textField: UITextField) -> Bool {
        if(self.answerView.text == ""){
            let answerPickerView = UIPickerView()
            answerPickerView.dataSource = self
            answerPickerView.delegate = self
            self.answerView.inputView = answerPickerView
        } else {
            self.answerView.inputView = nil
        }
        return true
    }
}
extension ChoiceRowView : UIPickerViewDataSource, UIPickerViewDelegate {
    
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return choices.count
    }
    
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        return choices[row]
    }
    
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        answerView.text = choices[row]
    }
}
