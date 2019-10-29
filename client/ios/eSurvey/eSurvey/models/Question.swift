//
//  Question.swift
//  eSurvey
//
//  Created by Nam Nguyen on 1/13/18.
//  Copyright © 2018 Unilever. All rights reserved.
//
import ObjectMapper

open class Question: Mappable {
    
    open var index: Int = 0
    open var questionType: String!
    open var questionTitle: String!
    open var description: String!
    open var required: Bool = false
    open var randomChoices: Bool = false
    open var choices : [String] = [String]()
    open var min: Int = 0
    open var max: Int = 0
    open var numberOfLines: Int = 0
    
    public init() {
    }
    
    required public init?(map: Map){
    }
    
    open func mapping(map: Map) {
        index <- map["index"]
        questionType <- map["question_type"]
        questionTitle <- map["question_title"]
        description <- map["description"]
        required <- map["required"]
        randomChoices <- map["random_choices"]
        choices <- map["choices"]
        min <- map["min"]
        max <- map["max"]
        numberOfLines <- map["number_of_lines"]
    }
}
