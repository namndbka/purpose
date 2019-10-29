package vn.zuni.findpurpose.presenters

import android.os.Bundle
import icepick.Icepick
import nucleus5.presenter.RxPresenter

/**
 *
 * Created by namnd on 1/11/18.
 */
open class BasePresenter<V> : RxPresenter<V>() {

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        Icepick.restoreInstanceState(this, savedState)
    }

    override fun onSave(state: Bundle) {
        super.onSave(state)
        Icepick.saveInstanceState(this, state)
    }

}