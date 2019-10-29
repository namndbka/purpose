//
//  VideoRequest.swift
//  eSurvey
//
//  Created by Nam Nguyen on 1/19/18.
//  Copyright © 2018 Unilever. All rights reserved.
//

import ObjectMapper

open class VideoRequest: Mappable {
    
    open var msg: String!
    open var error: String!
    
    public init() {
    }
    
    required public init?(map: Map){
        
    }
    
    open func mapping(map: Map) {
        msg <- map["msg"]
        error <- map["error"]
    }
}

