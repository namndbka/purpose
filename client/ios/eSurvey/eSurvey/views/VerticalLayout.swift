//
//  VerticalLayout.swift
//  eSurvey
//
//  Created by Nam Nguyen on 1/15/18.
//  Copyright © 2018 Unilever. All rights reserved.
//

import UIKit

class VerticalLayout: UIView {
    
    var yOffsets: [CGFloat] = []
    
    override init(frame: CGRect) {
        super.init(frame: frame)
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }
    
    override func layoutSubviews() {
        
        var height: CGFloat = 0
        
        for i in 0..<subviews.count {
            let view = subviews[i] as UIView
            view.layoutSubviews()
            height += yOffsets[i]
            view.frame.origin.y = height
            height += view.frame.height
        }
        
        self.frame.size.height = height + yOffsets[0]
        
    }
    
    override func addSubview(_ view: UIView) {
        yOffsets.append(view.frame.origin.y)
        super.addSubview(view)
        
    }
    
    func removeAll() {
        for view in subviews {
            view.removeFromSuperview()
        }
        yOffsets.removeAll(keepingCapacity: false)
        
    }
    
}
