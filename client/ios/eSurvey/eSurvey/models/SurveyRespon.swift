//
//  SurveyRespon.swift
//  eSurvey
//
//  Created by Nam Nguyen on 1/18/18.
//  Copyright © 2018 Unilever. All rights reserved.
//

import ObjectMapper

open class SurveyRespon: Mappable {
    open var id: Int = 0
    open var email: String!
    open var answers: [AnswerRequest]!
    open var completed: Int = 0
    open var errorMsg: String!
    
    public init() {
    }
    
    required public init?(map: Map){
    }
    
    open func mapping(map: Map) {
        id <- map["id"]
        email <- map["email"]
        answers <- map["answers"]
        completed <- map["completed"]
        errorMsg <- map["error"]
    }
}
