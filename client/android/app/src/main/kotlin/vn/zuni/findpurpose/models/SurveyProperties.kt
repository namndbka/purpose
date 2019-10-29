package vn.zuni.findpurpose.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import vn.zuni.findpurpose.extensions.emptyString
import java.io.Serializable

class SurveyProperties constructor(@SerializedName("id") @Expose var id: Int = 0,
                                   @SerializedName("title") @Expose var title: String? = emptyString,
                                   @SerializedName("keyword") @Expose var keyword: String? = emptyString,
                                   @SerializedName("intro_message") @Expose var introMessage: String? = emptyString,
                                   @SerializedName("end_message") @Expose var endMessage: String? = emptyString,
                                   @SerializedName("skip_intro") @Expose var skipIntro: Boolean = false)
    : Serializable
