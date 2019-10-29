package vn.zuni.findpurpose.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_start.*
import vn.zuni.findpurpose.SurveyActivity
import vn.zuni.findpurpose.extensions.ExtraUtils
import vn.zuni.findpurpose.extensions.L
import vn.zuni.findpurpose.extensions.emptyString
import vn.zuni.findpurpose.extensions.onClick
import vn.zuni.findpurpose.isIntroRead
import vn.zuni.findpurpose.models.SurveyProperties
import vn.zuni.findpurpose.needPostAnswer
import vn.zuni.findpurpose.saveIntroRead

class FragmentStart : Fragment() {


    private var mQuestionCount: Int = 0
    private var surveyProperties: SurveyProperties? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(L.fragment_start, container, false)
    }

    @Suppress("DEPRECATION")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.let {
            surveyProperties = it.getSerializable(ExtraUtils.EXTRA_SURVEY_PROPERTIES) as SurveyProperties
            mQuestionCount = it.getInt(ExtraUtils.EXTRA_QUESTION_COUNT, 0)
            textView_start.text = Html.fromHtml(surveyProperties?.introMessage ?: emptyString)
        }
        startBtn.onClick {
            context?.let {
                val resultOK = !it.isIntroRead(surveyProperties?.id ?: 0)
                it.needPostAnswer(resultOK)
                it.saveIntroRead(surveyProperties?.id ?: 0)
                (activity as SurveyActivity).eventCompleted(resultOK)
            }
        }

    }
}