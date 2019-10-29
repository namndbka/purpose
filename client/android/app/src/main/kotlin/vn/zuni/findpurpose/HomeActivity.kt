package vn.zuni.findpurpose

import android.os.Bundle
import android.util.Log
import vn.zuni.findpurpose.extensions.*
import vn.zuni.findpurpose.models.AnswerRequest
import vn.zuni.findpurpose.models.SurveyRespon
import vn.zuni.findpurpose.presenters.HomePresenter
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.layout_progress_bar.*
import nucleus5.factory.RequiresPresenter
import org.json.JSONException
import org.json.JSONObject

/**
 *
 * Created by namnd on 01-Jan-18.
 */
@RequiresPresenter(HomePresenter::class)
class HomeActivity : BaseActivity<HomePresenter>(), HomePresenter.HomeCallBack {
    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        setContentView(L.activity_home)
        val emailStore = getEmail()
        etEmail.setText(emailStore)
        etEmail.setSelection(emailStore.length)
        subscribeBtn.onClick {
            val email = etEmail.text.toString()
            if (email.isUnileverEmailValid()) {
                progress_bar.show()
                subscribeEmail(email)
            } else {
                showAlert(S.email_invalidate) {}
            }
        }
    }

    private fun subscribeEmail(email: String) {
        if (isNetworkConnected()) {
            presenter.subscribeEmail(email)
        } else {
            progress_bar.hide()
            saveEmail(email)
            start<MainActivity>(true) {}
        }
    }

    override fun onSubscribeSuccess(surveyRespon: SurveyRespon) {
        if (surveyRespon.errorMsg == null) {
            saveEmail(surveyRespon.email)
            surveyRespon.answers?.let {
                if (it.isNotEmpty()) {
                    this.firstLoadSave(it)
                }
            }
            progress_bar.hide()
            start<MainActivity>(true) {}
        }
    }

    override fun onSubscribeFailed(throwable: Throwable) {
        progress_bar.hide()
        Log.e("SubscribeEmail", throwable.localizedMessage)
    }

    private fun firstLoadSave(answers: List<AnswerRequest>) {
        answers.forEach {
            if (it.completed == 100) saveIntroRead(it.sid)
            val sid = it.sid
            it.answers?.let {
                try {
                    val jsonObj = JSONObject(it)
                    jsonObj.keys().forEach {
                        baseContext.saveAnswer(sid, it.toInt(), jsonObj[it] as String)
                    }
                } catch (ex: JSONException) {
                }
            }
        }
    }

}