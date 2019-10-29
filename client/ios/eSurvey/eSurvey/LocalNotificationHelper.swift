//
//  LocalNotificationHelper.swift
//  Purpose Discovery
//
//  Created by Nam Nguyen on 1/25/18.
//  Copyright © 2018 Unilever. All rights reserved.
//

import UIKit

class LocalNotificationHelper: NSObject {

    func checkNotificationEnabled() -> Bool {
        // Check if the user has enabled notifications for this app and return True / False
        guard let settings = UIApplication.shared.currentUserNotificationSettings else { return false}
        if settings.types == .none {
            return false
        } else {
            return true
        }
    }
    
    func checkNotificationExists(_ taskTypeId: String) -> Bool {
        // Loop through the pending notifications
        for notification in UIApplication.shared.scheduledLocalNotifications! as [UILocalNotification] {
            // Find the notification that corresponds to this task entry instance (matched by taskTypeId)
            if (notification.userInfo!["taskObjectId"] as! String == String(taskTypeId)) {
                return true
            }
        }
        return false
    }
    
    func schedule(atHour: Int, atMinute: Int) {
        let questionRemin = DataManager.getQuestionRemin()
        removeNotification("schedule_at_\(atHour)h\(atMinute)")
        if(questionRemin > 0) {
            let reminderDate = NSCalendar.current.date(bySettingHour: atHour, minute: atMinute, second: 0, of: NSDate() as Date)
            let notification = UILocalNotification()
            notification.fireDate = reminderDate
            var message = String(format: Phrase.from("morning_message").description, questionRemin)
            if(atHour >= 12) {
                message = String(format: Phrase.from("evening_message").description, questionRemin)
            }
            notification.alertBody = message
            notification.alertAction = "Schedule at \(atHour)h\(atMinute)"
            notification.soundName = UILocalNotificationDefaultSoundName
            notification.userInfo = ["taskObjectId": "schedule_at_\(atHour)h\(atMinute)"]
            notification.applicationIconBadgeNumber = questionRemin
            notification.repeatInterval = NSCalendar.Unit.day
            notification.timeZone = NSTimeZone.local
            UIApplication.shared.scheduleLocalNotification(notification)
//            print("Notification set at \(atHour)h\(atMinute) : \(questionRemin)")
        }
    }
    
    func removeNotification(_ taskTypeId: String) {
        // loop through the pending notifications
        for notification in UIApplication.shared.scheduledLocalNotifications! as [UILocalNotification] {
            // Cancel the notification that corresponds to this task entry instance (matched by taskTypeId)
            if (notification.userInfo!["taskObjectId"] as! String == String(taskTypeId)) {
                UIApplication.shared.cancelLocalNotification(notification)
                UIApplication.shared.applicationIconBadgeNumber = 0
//                print("Notification deleted for taskTypeID: \(taskTypeId)")
                break
            }
        }
    }
    
    func listNotifications() -> [UILocalNotification] {
        var localNotify:[UILocalNotification]?
        for notification in UIApplication.shared.scheduledLocalNotifications! as [UILocalNotification] {
            localNotify?.append(notification)
//            print ("\(notification)")
        }
        return localNotify!
    }
}
