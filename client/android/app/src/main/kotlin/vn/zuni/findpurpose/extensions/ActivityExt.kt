package vn.zuni.findpurpose.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.WindowManager
import java.io.IOException
import java.nio.charset.Charset

/**
 *
 * Created by namnd on 10/21/17.
 */

inline fun <reified T : Activity> Activity.startForResult(
        requestCode: Int = -1,
        options: Bundle? = null,
        noinline init: Intent.() -> Unit = {}) {

    val intent = newIntent<T>(this)
    intent.init()
    startActivityForResult(intent, requestCode, options)
}


inline fun <reified T : Activity> Context.start(
        isFinish: Boolean = false,
        options: Bundle? = null,
        noinline init: Intent.() -> Unit = {}) {
    val intent = newIntent<T>(this)
    intent.init()
    startActivity(intent, options)
    if (isFinish) (this as Activity).finish()
}

inline fun <reified T : Any> newIntent(context: Context): Intent =
        Intent(context, T::class.java)


fun Context.loadDataFromAssets(fileName: String): String? {
    return try {
        val isFile = assets.open(fileName)
        val size = isFile.available()
        val buffer = ByteArray(size)
        isFile.read(buffer)
        isFile.close()
        String(buffer, Charset.forName("UTF-8"))
    } catch (ex: IOException) {
        ex.printStackTrace()
        null
    }
}

fun Activity.actionBarHeight(): Int {
    val metrics = Resources.getSystem().displayMetrics
    var actionBarHeight = (50 * metrics.density).toInt()
    val typedValue = TypedValue()
    if (theme.resolveAttribute(android.R.attr.actionBarSize, typedValue, true)) {
        actionBarHeight = TypedValue.complexToDimensionPixelSize(typedValue.data, metrics)
    }
    return actionBarHeight
}

@SuppressLint("NewApi")
fun Activity.updateStatusBarColor(color: Int) {
    KKorAbove {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }
    LLorAbove {
        window.addFlags(Integer.MIN_VALUE)
        val arrayOfFloat = FloatArray(3)
        Color.colorToHSV(color, arrayOfFloat)
        arrayOfFloat[2] *= 0.8f
        window.statusBarColor = Color.HSVToColor(arrayOfFloat)
    }
}

fun Activity.isPortrait() = this.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT


fun Activity.screenWidth(): Float {
    val display = windowManager.defaultDisplay
    val point = Point()
    display.getSize(point)
    return point.x.toFloat()
}

fun Context.showAlert(msgResId: Int, warning: Boolean = true, function: () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val builder = AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert)
        builder.setMessage(msgResId)
        if (!warning) {
            builder.setPositiveButton("OK", { _, _ -> run { function() } })
        } else {
            builder.setNegativeButton(vn.zuni.findpurpose.R.string.dimmis, { dialog, _ ->
                run { dialog.dismiss() }
            })
        }
        builder.show()
    } else {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(msgResId)
        if (!warning) {
            builder.setPositiveButton("OK", { _, _ -> run { function() } })
        } else {
            builder.setNegativeButton(vn.zuni.findpurpose.R.string.dimmis, { dialog, _ ->
                run { dialog.dismiss() }
            })
        }
        builder.show()
    }
}