package vn.zuni.findpurpose.presenters

import android.content.Context
import android.os.Bundle
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import vn.zuni.findpurpose.SurveyApplication
import vn.zuni.findpurpose.api.ISurveyApi
import vn.zuni.findpurpose.extensions.loadDataFromAssets
import vn.zuni.findpurpose.getEmail
import vn.zuni.findpurpose.models.SurveyProperties
import vn.zuni.findpurpose.models.SurveyRespon
import icepick.State
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiConsumer
import nucleus5.presenter.Factory
import vn.zuni.findpurpose.extensions.ThreadHelper
import javax.inject.Inject

/**
 *
 * Created by namnd on 1/11/18.
 */
class MainPresenter : BasePresenter<MainPresenter.SurveyCallback>() {


    @Inject
    lateinit var surveyApi: ISurveyApi

    @Inject
    lateinit var context: Context

    @Inject
    lateinit var gson: Gson

    @State
    lateinit var mEmail: String

    @State
    lateinit var mAnswers: String

    @State
    lateinit var mCompleted: String

    override fun onCreate(savedState: Bundle?) {
        SurveyApplication.instance.inject(this)
        super.onCreate(savedState)
        restartableLatestCache(REQUEST_SURVEYS, Factory {
            loadSurveyProperties()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(ThreadHelper.instance.scheduler)
        }, BiConsumer(SurveyCallback::onSurveyLoaded)
                , BiConsumer(SurveyCallback::onLoadFailed))
        restartableLatestCache(POST_ANSWERS, Factory {
            surveyApi.answerSurvery(mEmail, mAnswers, mCompleted.toInt())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(ThreadHelper.instance.scheduler)
        }, BiConsumer(SurveyCallback::onAnswerLoaded))
        if (savedState == null) {
            requestSurveys()
        }
    }

    private fun loadSurveyProperties(): Observable<List<SurveyProperties>> {
        val surveyProperties: List<SurveyProperties> = gson.fromJson(context.loadDataFromAssets("survey_properties.json"), object : TypeToken<List<SurveyProperties>>() {}.type)
        return Observable.just(surveyProperties)
    }

    private fun requestSurveys() {
        start(REQUEST_SURVEYS)
    }

    fun postAnswer(answers: String, completed: Int) {
        mEmail = context.getEmail()
        mAnswers = answers
        mCompleted = completed.toString()
        start(POST_ANSWERS)
    }

    interface SurveyCallback {

        fun onSurveyLoaded(surveyProperties: List<SurveyProperties>)
        fun onAnswerLoaded(surveyRespon: SurveyRespon)
        fun onLoadFailed(throwable: Throwable)
    }

    companion object {
        val REQUEST_SURVEYS = 771
        val POST_ANSWERS = 711
    }
}