//
//  ISurveryAPI.swift
//  eSurvey
//
//  Created by Nam Nguyen on 1/18/18.
//  Copyright © 2018 Unilever. All rights reserved.
//

import Foundation
import ObjectMapper
import Alamofire
import AlamofireObjectMapper

class ISurveyApi {
    open static let HOSTNAME = "esurvey.zuni.vn"
    private static let API_URL = "http://\(HOSTNAME)/api/v1"
    private static let headers: HTTPHeaders = [
        "Bundle": Bundle.main.bundleIdentifier! ,
        "From": "ios_survey",
        "Accept": "application/json"
    ]
    static func subscribeEmail(_ email: String, onSuccess: @escaping (_ surveyRespon: SurveyRespon)->Void, onFailure: @escaping (_ error: NSError) -> Void) {
        Alamofire.request(API_URL+"/subcribe_email", method: .post, parameters: ["email": email], headers: headers).responseObject(completionHandler: {(response: DataResponse<SurveyRespon>) -> Void in
            switch response.result {
            case .failure(let error):
                onFailure(error as NSError)
            case .success(let surveyRespon):
                onSuccess(surveyRespon)
            }
        })
    } // END subscribeEmail
    
    static func answerSurvery(_ email: String, answers:String, completed: Int, onSuccess: @escaping (_ surveyRespon: SurveyRespon)->Void, onFailure: @escaping (_ error: NSError) -> Void) {
        Alamofire.request(API_URL+"/answer_survey", method: .post, parameters: ["email": email, "answers": answers, "completed": completed], headers: headers).responseObject(completionHandler: {(response: DataResponse<SurveyRespon>) -> Void in
            switch response.result {
            case .failure(let error):
                onFailure(error as NSError)
            case .success(let surveyRespon):
                onSuccess(surveyRespon)
            }
        })
    } // END answerSurvey
    
    open static func uploadVideo(_ videoUrl: URL,onProgress: @escaping(_ progress: Double)->Void, onSuccess: @escaping (_ message: VideoRequest)->Void, onFailure: @escaping (_ error: NSError) -> Void) {
        var headerMultiPart = headers
        headerMultiPart["Content-Type"] = "multipart/form-data"
        Alamofire.upload(multipartFormData: {(formData) in formData.append(videoUrl, withName: "video")}, usingThreshold: UInt64.init(), to: API_URL+"/upload_video", method: .post, headers: headerMultiPart) { (result) in
            switch result {
            case .success(let upload, _, _):
                upload.uploadProgress(closure: { (progress) in
                    onProgress(progress.fractionCompleted)
                })
                upload.responseObject { (response: DataResponse<VideoRequest>) -> Void in
                    switch response.result {
                    case .failure(let error):
                        onFailure(error as NSError)
                    case .success(let message):
                        onSuccess(message)
                    }
                }
                break
            case .failure(let error):
                onFailure(error as NSError)
            }
        }
    }
    
}
