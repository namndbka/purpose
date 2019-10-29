package vn.zuni.findpurpose

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import vn.zuni.findpurpose.adapters.AdapterFragmentQ
import vn.zuni.findpurpose.fragment.*
import vn.zuni.findpurpose.models.Question
import vn.zuni.findpurpose.models.SurveyProperties
import vn.zuni.findpurpose.presenters.SurveyPresenter
import kotlinx.android.synthetic.main.activity_survey.*
import nucleus5.factory.RequiresPresenter
import vn.zuni.findpurpose.extensions.ExtraUtils
import java.util.*

@RequiresPresenter(SurveyPresenter::class)
class SurveyActivity : vn.zuni.findpurpose.BaseActivity<SurveyPresenter>(), SurveyPresenter.QuestionsCallback {

    private lateinit var mSurveyProperties: SurveyProperties
    //    private var questionCount: Int = 0
    private var mQuestions: List<Question>? = null

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        setContentView(vn.zuni.findpurpose.R.layout.activity_survey)

        intent.extras?.let {
            mSurveyProperties = it.getSerializable(ExtraUtils.EXTRA_SURVEY_PROPERTIES) as SurveyProperties
            presenter.requestQuestions(mSurveyProperties.id)
        }
    }

    override fun onQuestionsLoaded(questions: List<Question>) {
        if (mQuestions != null) return
        mQuestions = questions
        saveQuestionCount(mSurveyProperties.id, questions.size)
        val fragments = ArrayList<Fragment>()
        //- START -
        val skipIntro = mSurveyProperties.skipIntro
        if (skipIntro) {
            val fragStart = FragmentStart()
            val sBundle = Bundle()
            sBundle.putInt(ExtraUtils.EXTRA_QUESTION_COUNT, questions.size)
            sBundle.putSerializable(ExtraUtils.EXTRA_SURVEY_PROPERTIES, mSurveyProperties)
            fragStart.arguments = sBundle
            fragments.add(fragStart)
        }
        var requiredPermission = false
        //- FILL -
        for (question in questions) {
            val xBundle = Bundle()
            xBundle.putSerializable(ExtraUtils.EXTRA_QUESTION, question)
            xBundle.putInt(ExtraUtils.EXTRA_QUESTION_COUNT, questions.size)
            xBundle.putSerializable(ExtraUtils.EXTRA_SURVEY_PROPERTIES, mSurveyProperties)

            when (question.questionType) {
                "StringMultiline" -> {
                    val frag = FragmentMultiline()
                    frag.arguments = xBundle
                    fragments.add(frag)
                }
                "Video" -> {
                    requiredPermission = true
                    val frag = FragmentVideo()
                    frag.arguments = xBundle
                    fragments.add(frag)
                }
            }
        }
        val mPagerAdapter = AdapterFragmentQ(supportFragmentManager, fragments)
        pager.adapter = mPagerAdapter
        if (requiredPermission) checkPermission(REQUEST_PERMISSIONS_CODE)
    }

    override fun onLoadFailed(throwable: Throwable) {

    }

    fun goToNext() {
        pager.currentItem = pager.currentItem + 1
    }

    override fun onBackPressed() {
        if (pager.currentItem == 0) {
            super.onBackPressed()
        } else {
            pager.currentItem = pager.currentItem - 1
        }
    }

    fun eventCompleted(resultOK: Boolean = false) {
        if (resultOK) setResult(Activity.RESULT_OK)
        finish()
    }


}
