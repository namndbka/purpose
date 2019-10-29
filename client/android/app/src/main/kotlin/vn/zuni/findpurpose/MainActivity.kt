package vn.zuni.findpurpose

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import vn.zuni.findpurpose.adapters.SurveyAdapter
import vn.zuni.findpurpose.extensions.L
import vn.zuni.findpurpose.models.AnswerRequest
import vn.zuni.findpurpose.models.SurveyProperties
import vn.zuni.findpurpose.models.SurveyRespon
import vn.zuni.findpurpose.presenters.MainPresenter
import kotlinx.android.synthetic.main.activity_main.*
import nucleus5.factory.RequiresPresenter

/**
 *
 * Created by namnd on 1/11/18.
 */

@RequiresPresenter(MainPresenter::class)
class MainActivity : vn.zuni.findpurpose.BaseActivity<MainPresenter>(), MainPresenter.SurveyCallback {

    private var mSurveyAdapter: SurveyAdapter? = null
    private lateinit var mSurveyProperties: List<SurveyProperties>

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        setContentView(L.activity_main)
        surveyListView.layoutManager = LinearLayoutManager(this@MainActivity)
        mSurveyAdapter = SurveyAdapter()
        surveyListView.adapter = mSurveyAdapter
    }


    override fun onSurveyLoaded(surveyProperties: List<SurveyProperties>) {
        overallProgressBar.max = surveyProperties.size
        mSurveyProperties = surveyProperties
        mSurveyAdapter?.setData(surveyProperties)
        updateProgressBar(true)
    }

    private fun updateProgressBar(postAnswer: Boolean = false) {
        var completed = 0
        var answerReqs = emptyList<AnswerRequest>().toMutableList()
        mSurveyProperties.forEach {
            val ansReq = AnswerRequest()
            ansReq.sid = it.id
            if (it.id == 0) {
                if (isIntroRead(it.id)) {
                    completed += 1
                    ansReq.completed = 100 // 100 percent
                }
            } else {
                val questCount = getQuestionCount(it.id)
                if (questCount > 0) {
                    var progress = 0
                    var dict = HashMap<String, String>()
                    for (index in 0..(questCount - 1)) {
                        val ans = getAnswer(it.id, index)
                        if (!ans.isEmpty()) {
                            dict.put(index.toString(), ans)
                            progress += 1
                        }
                    }
                    ansReq.answers = Gson().toJson(dict)
                    if (progress == questCount) {
                        completed += 1
                        ansReq.completed = 100  // 100 percent
                    } else {
                        ansReq.completed = (progress.toFloat() / questCount.toFloat()).toInt()*100 // percent
                    }
                }
            }
            answerReqs.add(ansReq)
        }
        overallProgressBar.progress = completed
        if (postAnswer) {
            val turnsType = object : TypeToken<List<AnswerRequest>>() {}.type
            val turns = Gson().toJson(answerReqs, turnsType)
            presenter.postAnswer(turns, (completed.toFloat()/mSurveyProperties.size).toInt()*100)
        }
    }

    override fun onAnswerLoaded(surveyRespon: SurveyRespon) {

    }

    override fun onLoadFailed(throwable: Throwable) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SURVEY_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                mSurveyAdapter?.notifyDataSetChanged()
                updateProgressBar(true)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    companion object {
        val SURVEY_REQUEST = 1337
    }

}
