//
//  AnswerRequest.swift
//  eSurvey
//
//  Created by Nam Nguyen on 1/18/18.
//  Copyright © 2018 Unilever. All rights reserved.
//

import ObjectMapper

open class AnswerRequest: Mappable {
    
    open var sid: Int = 0
    open var answers : String!
    open var completed: Int = 0 // percent
    
    public init() {
    }
    
    required public init?(map: Map){
        
    }
    
    open func mapping(map: Map) {
        sid <- map["sid"]
        answers <- map["answers"]
        completed <- map["completed"]
    }
}
