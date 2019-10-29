//
//  StringExt.swift
//  eSurvey
//
//  Created by Nam Nguyen on 1/12/18.
//  Copyright © 2018 Unilever. All rights reserved.
//

import Foundation
import UIKit

extension String {
    public func isValidEmail() -> Bool {
        // print("validate calendar: \(testStr)")
        let emailRegEx = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}"
        let emailTest = NSPredicate(format:"SELF MATCHES %@", emailRegEx)
        return emailTest.evaluate(with: self)
    }
    
    public func isValidUnileverEmail() -> Bool {
        if(self.isValidEmail()) {
            return self.lowercased().hasSuffix("@unilever.com")
        }
        return false
    }
    
    func htmlAttributedString(_ center: Bool = false) -> NSAttributedString? {
        var strin = self.replacingOccurrences(of: "<h1>", with: "<h2>").replacingOccurrences(of: "</h1>", with: "</h2>")
        if(center) { strin = "<center>\(strin)</center>" }
        var fontSize = 5
        if UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiom.pad { fontSize = 6 }
        let str = " <font face=\"arial\" size=\"\(fontSize)\">\(strin)</font> "
        guard let data = NSString(string: str).data(using: String.Encoding.unicode.rawValue) else { return nil }
        guard let html = try? NSAttributedString(
            data: data,
            options: [NSAttributedString.DocumentReadingOptionKey.documentType: NSAttributedString.DocumentType.html],
            documentAttributes: nil) else { return nil }
        return html
    }
    
    func convertToAnswerDict() -> [String: String]? {
        if let data = self.data(using: .utf8) {
            do {
                return try JSONSerialization.jsonObject(with: data, options: []) as? [String: String]
            } catch { }
        }
        return nil
    }
}
extension Dictionary {
    func toJSONString() -> String? {
        if #available(iOS 11.0, *) {
            if let theJSONData = try?  JSONSerialization.data( withJSONObject: self, options: .sortedKeys),
                let theJSONText = String(data: theJSONData, encoding: String.Encoding.utf8) {
                return theJSONText
            }
        } else {
            if let theJSONData = try?  JSONSerialization.data( withJSONObject: self, options: []),
                let theJSONText = String(data: theJSONData, encoding: String.Encoding.utf8) {
                return theJSONText
            }
        }
        return nil
    }
}
