package vn.zuni.findpurpose.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SurveyPojo constructor(@SerializedName("survey_properties") @Expose var surveyProperties: SurveyProperties? = null,
                             @SerializedName("questions") @Expose var questions: List<Question> = emptyList())
    : Serializable