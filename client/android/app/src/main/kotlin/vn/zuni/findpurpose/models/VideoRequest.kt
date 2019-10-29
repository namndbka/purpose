package vn.zuni.findpurpose.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 *
 * Created by namnd on 1/20/18.
 */
class VideoRequest constructor(@SerializedName("msg") var msg: String? = null,
                               @SerializedName("error") var error: String? = null) : Serializable