//
//  SurveyProperties.swift
//  eSurvey
//
//  Created by Nam Nguyen on 1/12/18.
//  Copyright © 2018 Unilever. All rights reserved.
//

import ObjectMapper

open class SurveyProperties: Mappable {
    
    open var id: Int = 0
    open var title: String!
    open var keyword: String!
    open var introMessage: String!
    open var endMessage: String!
    open var skipIntro: Bool = false
    
    public init() {
        
    }
    
    required public init?(map: Map){
        
    }
    
    open func mapping(map: Map) {
        id <- map["id"]
        title <- map["title"]
        keyword <- map["keyword"]
        introMessage <- map["intro_message"]
        endMessage <- map["end_message"]
        skipIntro <- map["skip_intro"]
    }
}
