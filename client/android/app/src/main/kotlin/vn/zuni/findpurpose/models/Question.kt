package vn.zuni.findpurpose.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import vn.zuni.findpurpose.extensions.emptyString
import java.io.Serializable

data class Question constructor(@SerializedName("index") @Expose var index: Int = 0,
                                @SerializedName("question_type") @Expose var questionType: String? = emptyString,
                                @SerializedName("question_title") @Expose var questionTitle: String? = emptyString,
                                @SerializedName("description") @Expose var description: String? = emptyString,
                                @SerializedName("required") @Expose var required: Boolean = false,
                                @SerializedName("random_choices") @Expose var randomChoices: Boolean = false,
                                @SerializedName("choices") @Expose var choices: List<String> = emptyList(),
                                @SerializedName("min") @Expose var min: Int = 0,
                                @SerializedName("max") @Expose var max: Int = 0,
                                @SerializedName("number_of_lines") @Expose var numberOfLines: Int = 0)
    : Serializable