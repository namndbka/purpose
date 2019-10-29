<?php

namespace App\Helpers;

use Carbon\Carbon;
use App\Models\Announcement;
use App\Models\Grade;
use App\Models\NotificationTopic;
use FCM;
use LaravelFCM\Message\OptionsBuilder;
use LaravelFCM\Message\OptionsPriorities;
use LaravelFCM\Message\PayloadData;
use LaravelFCM\Message\PayloadDataBuilder;


class NotificationHelper {
	/**
     * @param string|string[] $topics
     * @param PayloadData $data
     */
    public static function send($topics, $data) {
        self::sendToPlatform($topics, null, $data, null);
        // Fix for android in background mode
		// self::sendToPlatform($topics, null, $data, 'android');
    }
	/**
     * @param string|string[] $topics
     * @param PayloadNotification $notification
     * @param PayloadData $data
     * @param string $platform
     */
    private static function sendToPlatform($topics, $notification, $data, $platform) {
        if(empty($topics)) {
            return;
        }

        if(!is_array($topics)) {
            $topics = [$topics];
        }

        $query = NotificationTopic::distinct()->whereIn('user_id', $topics);
        if(!empty($platform)) {
            $query->where('platform', $platform);
        }
        $deviceTokens = $query->pluck('device_token')->toArray();
        if(empty($deviceTokens)) {
            return;
        }
        $optionBuiler = new OptionsBuilder();
        $optionBuiler->setPriority(OptionsPriorities::high);
        $optionBuiler->setContentAvailable(true);
        $option = $optionBuiler->build();
        $option->verify = false;
        FCM::sendTo($deviceTokens, $option, $notification, $data);
    }
    
    public static function sendGradeNofitication(Grade $grade, $subjectName) {
        $userId = $grade->student->user_id;
        self::send(
            $userId,
            (new PayloadDataBuilder())
                ->setData(['event' => 'grade',
                    'title_key'=> 'notification_new_grade',
                    'id'=>intval($grade->id),
                    'student_id'=> intval($grade->student_id),
                    'semester_id' => intval($grade->semester_id),
                    'course_id' => intval($grade->course_id),
                    'subject_id' => intval($grade->subject_id),
                    'name' => $subjectName,
                    'extra' => $grade->extra,
                    'updated_at' => $grade->updated_at])
                ->build()
        );
    }

    public static function sendAnnouncementNotification(Announcement $announcement, $userIdObjects) {
        $topics = [];
        foreach($userIdObjects as $userIdObject) {
            if($userIdObject['user_id'] != $announcement->created_id)
                $topics[] = $userIdObject['user_id'];
        }
        self::send(
            $topics,
            (new PayloadDataBuilder())
                ->setData(['event' => 'announcement',
                    'title_key'=> 'notification_new_announcement',
                    'id'=> intval($announcement->id),
                    'title'=> $announcement->title,
                    'subtitle'=> $announcement->subtitle,
                    'content'=> $announcement->content,
                    'updated_at'=> intval($announcement->updated_at),
                    'created_id'=> intval($announcement->created_id),
                    'avatar'=> $announcement->avatar])
                ->build()
        );
    }
}