package vn.zuni.findpurpose.api

import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.*
import vn.zuni.findpurpose.models.SurveyRespon
import vn.zuni.findpurpose.models.VideoRequest

/**
 *
 * Created by namnd on 1/18/18.
 *
 */

interface ISurveyApi {

    @FormUrlEncoded
    @POST("subcribe_email")
    fun subscribeEmail(@Field("email") email: String): Observable<SurveyRespon>

    @FormUrlEncoded
    @POST("answer_survey")
    fun answerSurvery(@Field("email") email: String, @Field("answers") answers: String, @Field("completed") completed: Int): Observable<SurveyRespon>

    @Multipart
    @POST("upload_video")
    fun uploadVideo(@Part video: MultipartBody.Part): Observable<VideoRequest>
}