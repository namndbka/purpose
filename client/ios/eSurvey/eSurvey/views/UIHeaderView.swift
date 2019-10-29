//
//  CustomUIView.swift
//  AGCustomViewBy_XIB
//
//  Created by Aman Gupta on 11/10/17.
//  Copyright © 2017 aman19ish. All rights reserved.
//

import UIKit
import GTProgressBar

@IBDesignable
class UIHeaderView: UIView {

    //MARK: - IBOutlets
    @IBOutlet var headerView: UIView!

    @IBOutlet weak var backButton: UIButton!
    @IBOutlet weak var homeButton: UIButton!
    @IBOutlet weak var indexLabel: UIButton!
    @IBOutlet weak var headerTitle: UILabel!
    @IBOutlet weak var headerProgressBar: GTProgressBar!
    
    @IBInspectable
    var hideBackButton: Bool {
        get {
            return backButton.isHidden
        }
        set(hideBackButton) {
            headerTitle.isHidden = hideBackButton
        }
    }
    
    @IBInspectable
    var titleText: String? {
        get {
            return headerTitle.text
        }
        set(titleText) {
            headerTitle.text = titleText
        }
    }
    
    @IBInspectable
    var indexText: String? {
        get {
            return indexLabel.title(for: .normal)
        }
        set(indexText) {
            indexLabel.setTitle(indexText, for: .normal)
        }
    }
    
    @IBInspectable
    var progress: CGFloat {
        get {
            return headerProgressBar.progress
        }
        set(progress) {
            headerProgressBar.progress = progress
        }
    }
    
    func homeClick(_ target: Any?, action: Selector) {
        self.backButton.addTarget(target, action: action, for: .touchUpInside)
        self.homeButton.addTarget(target, action: action, for: .touchUpInside)
    }
    
    //MARK: - UIView Overided methods
    override func draw(_ rect: CGRect) {
        // Drawing code
        if let context = UIGraphicsGetCurrentContext() {
            context.setStrokeColor(UIColor.lightGray.cgColor)
            context.setLineWidth(1)
            context.move(to: CGPoint(x: 0, y: bounds.height))
            context.addLine(to: CGPoint(x: bounds.width, y: bounds.height))
            context.strokePath()
        }
    }
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        // 3. Setup view from .xib file
        configureXIB()
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        // 3. Setup view from .xib file
        configureXIB()
    }
    
    //MARK: - Custom Methods
    func configureXIB() {
        headerView = configureNib()
        // use bounds not frame or it'll be offset
        headerView.frame = bounds
        // Make the flexible view
        headerView.autoresizingMask = [UIViewAutoresizing.flexibleWidth, UIViewAutoresizing.flexibleHeight]
        // Adding custom subview on top of our view (over any custom drawing > see note below)
        addSubview(headerView)
    }
    
    func configureNib() -> UIView {
        let bundle = Bundle(for: type(of: self))
        let nib = UINib(nibName: "UIHeaderView", bundle: bundle)
        let view = nib.instantiate(withOwner: self, options: nil)[0] as! UIView
        return view
    }
}
