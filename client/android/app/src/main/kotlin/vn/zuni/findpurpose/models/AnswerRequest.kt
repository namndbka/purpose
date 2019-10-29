package vn.zuni.findpurpose.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 *
 * Created by namnd on 1/20/18.
 */
class AnswerRequest constructor( @SerializedName("sid") var sid: Int = 0,
                                 @SerializedName("answers") var answers : String? = null,
                                 @SerializedName("completed") var completed: Int = 0) : Serializable {
    override fun toString(): String {
        return Gson().toJson(this@AnswerRequest)
    }
}