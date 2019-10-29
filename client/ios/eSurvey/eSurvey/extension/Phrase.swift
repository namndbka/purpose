//
//  Phrase.swift
//  eSurvey
//
//  Created by Nam Nguyen on 1/12/18.
//  Copyright © 2018 Unilever. All rights reserved.
//

import Foundation

open class Phrase {
    fileprivate var mKeys = [String]()
    fileprivate var mKeysToValue = [String: AnyObject]()
    
    fileprivate var mLocalizeString = ""
    
    open var description : String {
        get {
            return mLocalizeString
        }
    }
    
    public init(localizeKey: String) {
        mLocalizeString = NSLocalizedString(localizeKey, comment: "")
        mKeys = matchesForRegexInText("\\{(.+?)\\}", text: mLocalizeString)
    }
    
    open static func from(_ localizeKey: String) -> Phrase {
        return Phrase(localizeKey: localizeKey)
    }
    
    open func put(_ key: String, value: AnyObject) -> Phrase {
        mKeysToValue["{\(key)}"] = value
        return self
    }
    
    open func fomat() -> Phrase {
        for key in mKeys {
            let value: AnyObject? = mKeysToValue[key]
            if( value == nil){
                print("not exist key \(key)")
                continue
            }
            mLocalizeString = mLocalizeString.replacingOccurrences(of: key, with: value!.description, options: NSString.CompareOptions(), range: nil)
        }
        return self
    }
    
    fileprivate func matchesForRegexInText(_ regex: String!, text: String!) -> [String] {
        let regex = try! NSRegularExpression(pattern: regex, options: [])
        let nsString = text as NSString
        let results = regex.matches(in: text,
                                    options: [], range: NSMakeRange(0, nsString.length))
        
        return results.map{ nsString.substring(with: $0.range)}
    }
}

