//
//  UIVideoPreview.swift
//  eSurvey
//
//  Created by Nam Nguyen on 1/17/18.
//  Copyright © 2018 Unilever. All rights reserved.
//

import UIKit
import AVKit

@IBDesignable
class UIVideoPreview: UIView {

    @IBOutlet var contentView: UIView!
    
    @IBOutlet weak var progressView: UIProgressView!
    @IBOutlet weak var videoView: UIView!
    @IBOutlet weak var recordAgainButton: UIButton!
    @IBOutlet weak var nextButton: UIButton!
    
    let movie = AVPlayerViewController()
    
    @IBInspectable
    open var videoUrl: URL? {
        didSet{
            movie.view.frame = self.videoView.bounds
            let player = AVPlayer(url: videoUrl!)
            movie.player = player
            self.videoView.addSubview(movie.view)
        }
    }
    
    @IBInspectable
    open var progress: Float = 0.0 {
        didSet{
            progressView.progress = progress
        }
    }
    
    func progressHide(){
        UIView.animate(withDuration: 0.5, delay: 6.0, options: [], animations: {
            self.progressView.alpha = 0.0
        }) { (finished: Bool) in
            self.progressView.isHidden = true
        }
    }
    func recordAgainClick(_ target: Any?, action: Selector) {
        self.recordAgainButton.addTarget(target, action: action, for: .touchUpInside)
    }
    func nextQuestionClick(_ target: Any?, action: Selector) {
        self.nextButton.addTarget(target, action: action, for: .touchUpInside)
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
        contentView = configureNib()
        // use bounds not frame or it'll be offset
        contentView.frame = bounds
        // Make the flexible view
        contentView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        // Adding custom subview on top of our view (over any custom drawing > see note below)
        addSubview(contentView)
        self.recordAgainButton.setTitle(Phrase.from("record_again").description.uppercased(), for: .normal)
        self.progressView.isHidden = true
        self.progressView.alpha = 1.0
    }
    
    func configureNib() -> UIView {
        let bundle = Bundle(for: type(of: self))
        let nib = UINib(nibName: "UIVideoPreview", bundle: bundle)
        let view = nib.instantiate(withOwner: self, options: nil)[0] as! UIView
        return view
    }
}
