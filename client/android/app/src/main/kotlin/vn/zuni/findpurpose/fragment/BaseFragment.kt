package vn.zuni.findpurpose.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment

import kotlinx.android.synthetic.main.footer.*
import kotlinx.android.synthetic.main.layout_header.*
import vn.zuni.findpurpose.SurveyActivity
import vn.zuni.findpurpose.extensions.ExtraUtils
import vn.zuni.findpurpose.extensions.emptyString
import vn.zuni.findpurpose.extensions.onClick
import vn.zuni.findpurpose.extensions.showOrInvisible
import vn.zuni.findpurpose.getAnswer
import vn.zuni.findpurpose.models.Question
import vn.zuni.findpurpose.models.SurveyProperties


/**
 *
 * Created by namnd on 09-Jan-18.
 */
abstract class BaseFragment : Fragment() {

    var mQuestionData: Question? = null
    var mSurveyProperties: SurveyProperties? = null
    var mQuestionCount: Int = 0

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.let {
            mQuestionCount = it.getInt(ExtraUtils.EXTRA_QUESTION_COUNT, 0)
            mQuestionData = it.getSerializable(ExtraUtils.EXTRA_QUESTION) as Question
            mSurveyProperties = it.getSerializable(ExtraUtils.EXTRA_SURVEY_PROPERTIES) as SurveyProperties
            mSurveyProperties?.let { sendUpdateHeader(mQuestionData?.index ?: 0, it) }
        }
        logo.onClick {
            homeBack()
        }
        headerBackBtn.onClick {
            homeBack()
        }
        backBtn.onClick {
            (activity as SurveyActivity).onBackPressed()
        }
        continueBtn.onClick {
            (activity as SurveyActivity).goToNext()
        }
    }

    fun homeBack() {
        val surveyActivity = activity as SurveyActivity
        surveyActivity.setResult(Activity.RESULT_OK)
        surveyActivity.finish()
    }

    @SuppressLint("SetTextI18n")
    private fun sendUpdateHeader(questionIndex: Int, surveyProperties: SurveyProperties) {
        backBtn.showOrInvisible(questionIndex > 0)
        continueBtn.showOrInvisible(questionIndex < (mQuestionCount - 1))
        if (questionIndex < mQuestionCount) {
            progressBar.max = mQuestionCount
            val indicator = questionIndex + 1
            label.text = surveyProperties.title
            indicatorLabel.text = indicator.toString() + "/" + mQuestionCount.toString()
        }
        context?.let {
            updateHeaderProgress(surveyProperties)
            headerIndicator.text = surveyProperties.id.toString()
        }
    }

    private fun updateHeaderProgress(surveyProperties: SurveyProperties) {
        var progress = 0
        for (index in 0..(mQuestionCount - 1)) {
            val ans = context?.getAnswer(surveyProperties.id, index) ?: emptyString
            if (!ans.isEmpty()) {
                progress += 1
            }
        }
        progressBar.progress = progress
    }

    private fun list2String(data: List<String>): String {
        val sb = StringBuilder()
        (0 until data.size)
                .filter { data[it].isNotEmpty() }
                .forEach { sb.append(data[it]).append(";") }
        return sb.toString()
    }

    private fun string2List(data: String): List<String> {
        val sbs = data.split(";")
        val list = emptyList<String>().toMutableList()
        (0 until sbs.size).mapTo(list) { sbs[it] }
        return list.toList()
    }
}