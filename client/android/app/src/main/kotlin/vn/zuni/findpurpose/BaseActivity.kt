package vn.zuni.findpurpose

import android.Manifest
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import icepick.Icepick
import nucleus5.presenter.Presenter
import nucleus5.view.NucleusAppCompatActivity
import vn.zuni.findpurpose.extensions.*

/**
 *
 * Created by namnd on 1/11/18.
 */

abstract class BaseActivity<P : Presenter<*>> : NucleusAppCompatActivity<P>() {


    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.enterTransition = null
            window.exitTransition = null
        }
        Icepick.restoreInstanceState(this, savedState)
        if (!BuildConfig.DEBUG)
            window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        updateStatusBarColor(takeColor(C.colorPrimary))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Icepick.saveInstanceState(this, outState)
        super.onSaveInstanceState(outState)
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun checkPermission(requestCode: Int) {
        when (requestCode) {
            REQUEST_PERMISSIONS_CODE -> if (PermissionUtils.hasSelfPermission(this, vn.zuni.findpurpose.BaseActivity.Companion.STORAGE_PERMISSIONS))
                onRequestPermissionResult(requestCode, true)
            else MorAbove {
                requestPermissions(STORAGE_PERMISSIONS, requestCode)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            vn.zuni.findpurpose.BaseActivity.Companion.REQUEST_PERMISSIONS_CODE -> onRequestPermissionResult(requestCode, PermissionUtils.verifyAllPermissions(grantResults))
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    open fun onRequestPermissionResult(requestCode: Int, isSuccess: Boolean) {

    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(base))
    }

    companion object {
        val REQUEST_PERMISSIONS_CODE = 1001
        //Read and write permission for storage listed here.
        var STORAGE_PERMISSIONS = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
    }
}