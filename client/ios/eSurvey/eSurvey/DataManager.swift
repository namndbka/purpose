//
//  DataManager.swift
//  eSurvey
//
//  Created by Nam Nguyen on 1/12/18.
//  Copyright © 2018 Unilever. All rights reserved.
//

import Foundation

open class DataManager {
    
    open static func saveEmail(_ email: String){
        Defaults[.semail] = email
    }
    open static func getEmail() -> String? {
        guard  let saveEmailStr =  Defaults[.semail] else {
            return nil
        }
        return saveEmailStr
    }
    open static func saveSubscribe(){
        guard  let emailStr =  Defaults[.semail] else {
            return
        }
        let email = emailStr.replacingOccurrences(of: "@", with: "_").replacingOccurrences(of: ".", with: "_")
        let subsribeEmail = DefaultsKey<Bool?>("\(email)_subscribe")
        Defaults[subsribeEmail] = true
    }
    open static func isSubscribe(_ email: String) -> Bool {
        let email = email.replacingOccurrences(of: "@", with: "_").replacingOccurrences(of: ".", with: "_")
        let subsribeEmail = DefaultsKey<Bool?>("\(email)_subscribe")
        guard  let subscribe =  Defaults[subsribeEmail] else {
            return false
        }
        return subscribe
    }
    
    open static func saveAnswer(_ surveyId: Int, questionIndex: Int, answer:String){
        guard  let emailStr =  Defaults[.semail] else {
            return
        }
        let email = emailStr.replacingOccurrences(of: "@", with: "_").replacingOccurrences(of: ".", with: "_")
        let answerKey = DefaultsKey<String?>("\(email)_ans_s_\(surveyId)_q_\(questionIndex)")
        Defaults[answerKey] = answer
    }
    
    open static func getAnswer(_ surveyId: Int, questionIndex: Int) -> String {
        guard  let emailStr =  Defaults[.semail] else {
            return ""
        }
        let email = emailStr.replacingOccurrences(of: "@", with: "_").replacingOccurrences(of: ".", with: "_")
        let answerKey = DefaultsKey<String?>("\(email)_ans_s_\(surveyId)_q_\(questionIndex)")
        guard  let answer =  Defaults[answerKey] else {
            return ""
        }
        return answer
    }
    open static func saveVideo(_ surveyId: Int, questionIndex: Int, url: String){
        guard  let emailStr =  Defaults[.semail] else {
            return
        }
        let email = emailStr.replacingOccurrences(of: "@", with: "_").replacingOccurrences(of: ".", with: "_")
        let videoUrl = DefaultsKey<String?>("\(email)_ans_s_\(surveyId)_q_\(questionIndex)_video_url")
        Defaults[videoUrl] = url
    }
    
    open static func getVideo(_ surveyId: Int, questionIndex: Int) -> String {
        guard  let emailStr =  Defaults[.semail] else {
            return ""
        }
        let email = emailStr.replacingOccurrences(of: "@", with: "_").replacingOccurrences(of: ".", with: "_")
        let videoUrl = DefaultsKey<String?>("\(email)_ans_s_\(surveyId)_q_\(questionIndex)_video_url")
        guard  let answerUrl =  Defaults[videoUrl] else {
            return ""
        }
        return answerUrl
    }
    
    open static func saveIntroRead(_ surveyId: Int){
        guard  let emailStr =  Defaults[.semail] else {
            return
        }
        let email = emailStr.replacingOccurrences(of: "@", with: "_").replacingOccurrences(of: ".", with: "_")
        let introRead = DefaultsKey<Bool?>("\(email)_read_s_\(surveyId)")
        Defaults[introRead] = true
    }
    
    open static func isIntroRead(_ surveyId: Int) -> Bool {
        guard  let emailStr =  Defaults[.semail] else {
            return false
        }
        let email = emailStr.replacingOccurrences(of: "@", with: "_").replacingOccurrences(of: ".", with: "_")
        let introRead = DefaultsKey<Bool?>("\(email)_read_s_\(surveyId)")
        guard  let read =  Defaults[introRead] else {
            return false
        }
        return read
    }
    open static func saveQuestionCount(_ surveyId: Int, count: Int){
        let questionKey = DefaultsKey<Int?>("quest_count_\(surveyId)")
        Defaults[questionKey] = count
    }
    
    open static func getQuestionCount(_ surveyId: Int) -> Int {
        let questionKey = DefaultsKey<Int?>("quest_count_\(surveyId)")
        guard  let questionCount =  Defaults[questionKey] else {
            if(surveyId == 0) { return 0 }
            else if(surveyId == 5) { return 3 }
            else { return 1 }
        }
        return questionCount
    }
    
    open static func needPostAnswer(_ needpost: Bool = true) {
        Defaults[.needPost] = needpost
    }
    
    open static func isNeedPostAnswer() -> Bool {
        guard let needpost = Defaults[.needPost] else {
            return true
        }
        return needpost
    }
    
    open static func setNoticationEnable(_ enable: Bool = true) {
        Defaults[.notifEnable] = enable
    }
    
    open static func isNotification() -> Bool {
        guard let notifEnable = Defaults[.notifEnable] else {
            return true
        }
        return notifEnable
    }
    open static func saveQuestionRemin(_ questionRemin: Int){
        guard  let emailStr =  Defaults[.semail] else {
            return
        }
        let email = emailStr.replacingOccurrences(of: "@", with: "_").replacingOccurrences(of: ".", with: "_")
        let questionReminKey = DefaultsKey<Int?>("\(email)_question_remin)")
        Defaults[questionReminKey] = questionRemin
    }
    
    open static func getQuestionRemin() -> Int {
        guard  let emailStr =  Defaults[.semail] else {
            return 0
        }
        let email = emailStr.replacingOccurrences(of: "@", with: "_").replacingOccurrences(of: ".", with: "_")
        let questionReminKey = DefaultsKey<Int?>("\(email)_question_remin)")
        guard  let quesremin =  Defaults[questionReminKey] else {
            return 7
        }
        return quesremin
    }
}

extension DefaultsKeys {
    static let semail = DefaultsKey<String?>("save_email_key")
    static let needPost = DefaultsKey<Bool?>("need_post_answer_key")
    static let notifEnable = DefaultsKey<Bool?>("notication_enable_key")
}
