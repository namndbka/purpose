package vn.zuni.findpurpose.presenters

import android.os.Bundle
import vn.zuni.findpurpose.SurveyApplication
import vn.zuni.findpurpose.api.ISurveyApi
import vn.zuni.findpurpose.models.SurveyRespon
import icepick.State
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiConsumer
import nucleus5.presenter.Factory
import vn.zuni.findpurpose.extensions.ThreadHelper
import javax.inject.Inject

/**
 *
 * Created by namnd on 1/20/18.
 */

class HomePresenter : BasePresenter<HomePresenter.HomeCallBack>() {

    @Inject
    lateinit var surveyApi: ISurveyApi

    @State
    lateinit var mEmail: String

    override fun onCreate(savedState: Bundle?) {
        SurveyApplication.instance.inject(this)
        super.onCreate(savedState)
        restartableLatestCache(REQUEST_SUBSCRIBE, Factory {
            surveyApi.subscribeEmail(mEmail)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(ThreadHelper.instance.scheduler)
        }, BiConsumer(HomeCallBack::onSubscribeSuccess)
                , BiConsumer(HomeCallBack::onSubscribeFailed))
    }

    fun subscribeEmail(email: String) {
        mEmail = email
        start(REQUEST_SUBSCRIBE)
    }

    interface HomeCallBack {
        fun onSubscribeSuccess(surveyRespon: SurveyRespon)
        fun onSubscribeFailed(throwable: Throwable)
    }

    companion object {
        val REQUEST_SUBSCRIBE = 731
    }
}