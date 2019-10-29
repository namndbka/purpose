package vn.zuni.findpurpose

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson

/**
 *
 * Created by namnd on 1/20/18.
 */

fun Context.defaultSharedPreferences(): SharedPreferences {
    return getSharedPreferences(getEmail(), Context.MODE_PRIVATE)
}

fun Context.getEmail(): String {
    return PreferenceManager.getDefaultSharedPreferences(this).getString("subscribe_email", "")
}

fun Context.saveEmail(email: String) {
    PreferenceManager.getDefaultSharedPreferences(this).edit().putString("subscribe_email", email.toLowerCase()).apply()
}

// DATA

fun Context.saveIntroRead(surveyId: Int) {
    defaultSharedPreferences().edit().putBoolean("read_s_$surveyId", true).apply()
}

fun Context.isIntroRead(surveyId: Int): Boolean {
    return defaultSharedPreferences().getBoolean("read_s_$surveyId", false)
}

fun Context.saveAnswer(surveyId: Int, questionIndex: Int, answer: String) {
    defaultSharedPreferences().edit().putString("ans_s_${surveyId}_q_$questionIndex", answer).apply()
    needPostAnswer()
}

fun Context.getAnswer(surveyId: Int, questionIndex: Int): String {
    val answerStr = defaultSharedPreferences().getString("ans_s_${surveyId}_q_$questionIndex", "")
    return answerStr
}

fun Context.saveVideo(surveyId: Int, questionIndex: Int, url: String) {
    defaultSharedPreferences().edit().putString("ans_s_${surveyId}_q_${questionIndex}_video_url", url).apply()
}

fun Context.getVideo(surveyId: Int, questionIndex: Int): String {
    return defaultSharedPreferences().getString("ans_s_${surveyId}_q_${questionIndex}_video_url", "")
}

fun Context.saveQuestionCount(surveyId: Int, questionCount: Int) {
    defaultSharedPreferences().edit().putInt("quest_count_$surveyId", questionCount).apply()
}

fun Context.getQuestionCount(surveyId: Int): Int {
    var defaultValue = 1
    if(surveyId == 0) defaultValue = 0
    if(surveyId == 5) defaultValue = 3
    return defaultSharedPreferences().getInt("quest_count_$surveyId", defaultValue)
}

fun Context.needPostAnswer(needPost: Boolean = true) {
    defaultSharedPreferences().edit().putBoolean("need_post_answer_key", needPost).apply()
}

fun Context.isNeedPostAnswer(): Boolean {
    return defaultSharedPreferences().getBoolean("need_post_answer_key", true)
}
