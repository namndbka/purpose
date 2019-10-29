package vn.zuni.findpurpose.presenters

import android.content.Context
import android.os.Bundle
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import vn.zuni.findpurpose.SurveyApplication
import vn.zuni.findpurpose.extensions.loadDataFromAssets
import vn.zuni.findpurpose.models.Question
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
class SurveyPresenter : BasePresenter<SurveyPresenter.QuestionsCallback>() {

    @Inject
    lateinit var context: Context

    @Inject
    lateinit var gson: Gson

    @State
    lateinit var mSurveyId: String

    override fun onCreate(savedState: Bundle?) {
        SurveyApplication.instance.inject(this)
        super.onCreate(savedState)
        restartableLatestCache(REQUEST_QUESTIONS, Factory {
            loadQuestions(mSurveyId.toInt())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(ThreadHelper.instance.scheduler)
        }, BiConsumer(QuestionsCallback::onQuestionsLoaded), BiConsumer(QuestionsCallback::onLoadFailed))
    }

    fun requestQuestions(surveyId: Int) {
        mSurveyId = surveyId.toString()
        start(REQUEST_QUESTIONS)
    }


    private fun loadQuestions(surveyId: Int): Observable<List<Question>> {
        val questions: List<Question> = gson.fromJson(context.loadDataFromAssets("example_survey_$surveyId.json"), object : TypeToken<List<Question>>() {}.type)
        return Observable.just(questions)
    }

    interface QuestionsCallback {

        fun onQuestionsLoaded(questions: List<Question>)
        fun onLoadFailed(throwable: Throwable)
    }

    companion object {
        val REQUEST_QUESTIONS = 711
    }
}