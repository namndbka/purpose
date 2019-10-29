package vn.zuni.findpurpose.models

import com.google.gson.annotations.SerializedName
import vn.zuni.findpurpose.extensions.emptyString
import java.io.Serializable

/**
 * Created by namnd on 1/20/18.
 */
class SurveyRespon constructor(@SerializedName("id") var id: Int = 0,
                               @SerializedName("email") var email: String = emptyString,
                               @SerializedName("answers") var answers: List<AnswerRequest>? = null,
                               @SerializedName("completed") var completed: Int = 0,
                               @SerializedName("error") var errorMsg: String? = null) : Serializable