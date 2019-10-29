package vn.zuni.findpurpose.fragment

import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.footer.*
import kotlinx.android.synthetic.main.fragment_text_multiline.*
import vn.zuni.findpurpose.SurveyActivity
import vn.zuni.findpurpose.extensions.*
import vn.zuni.findpurpose.getAnswer
import vn.zuni.findpurpose.saveAnswer

class FragmentMultiline : BaseFragment() {

    private var questionIndex: Int = 0
    private var surveyId: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(L.fragment_text_multiline, container, false)
    }

    @Suppress("DEPRECATION")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.let {
            questionIndex = mQuestionData?.index ?: 0
            surveyId = mSurveyProperties?.id ?: 0
            val required = mQuestionData?.required ?: false
            if (required) {
                continueBtn.hide()
                editText_answer.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                    override fun afterTextChanged(s: Editable) {
                        continueBtn.show(s.length > 3)
                    }
                })
            }
            val hideDescription = mQuestionData?.description?.isBlank() ?: true
            descriptionView.show(!hideDescription)
            if (!hideDescription) {
                descriptionView.settings.javaScriptEnabled = true
                descriptionView.loadData(mQuestionData?.description ?: "")
            }
            textview_q_title.text = Html.fromHtml(mQuestionData?.questionTitle)
            val saveAnswer = context?.getAnswer(surveyId, questionIndex) ?: emptyString
            if (!saveAnswer.isEmpty()) {
                editText_answer.setText(saveAnswer)
                editText_answer.setSelection(saveAnswer.length)
            }
        }

        saveBtn.onClick {
            val answerStr = editText_answer.text.toString().trim { it <= ' ' }
            if (answerStr.isEmpty()) {
                context?.showAlert(S.answer_empty) { }
            } else {
                context?.saveAnswer(surveyId, questionIndex, answerStr)
                context?.showAlert(S.end_text_title, false) {
                    if (questionIndex < (mQuestionCount - 1)) {
                        (activity as SurveyActivity).goToNext()
                    } else {
                        homeBack()
                    }
                }
            }
        }
    }
}