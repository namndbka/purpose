package vn.zuni.findpurpose.extensions

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.text.Html
import android.text.Spanned
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import android.widget.Toast
import java.math.BigDecimal
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

/**
 *
 * Created by namnd on 10/20/17.
 */

@Deprecated("Use emptyString instead", ReplaceWith("emptyString"), level = DeprecationLevel.WARNING)
fun emptyString() = ""

val emptyString = ""

val ZuniImageHost = "http://img1.res.zuni.vn"

fun <K, V> emptyHashMap(): HashMap<K, V> = HashMap()

fun <T1, T2> ifNotNull(value1: T1?, value2: T2?, bothNotNull: (T1, T2) -> (Unit)) {
    if (value1 != null && value2 != null) {
        bothNotNull(value1, value2)
    }
}

fun String.isEmailValid(): Boolean {
    return Pattern.compile(
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
    ).matcher(this).matches()
}

fun String.isUnileverEmailValid(): Boolean {
    if (isEmailValid()) {
        return this.endsWith("@unilever.com", true)
    }
    return false
}

fun String.toCurrency(): String {
    return if (!this.isBlank()) {
        val vn = Locale("vi", "VN")
        val numberFormatVN = NumberFormat.getCurrencyInstance(vn)
        numberFormatVN.format(BigDecimal(this))
    } else {
        this
    }
}

fun String.toTime(): Long {
    return try {
        //2016-04-22 10:01:03
        val sdf = SimpleDateFormat("MM/yyyy-MM-dd HH:mm:ss", Locale.US)
        val date = sdf.parse(this)
        date.time
    } catch (d: ParseException) {
        0L
    }
}

fun WebView.loadData(data: String) {
    loadData(data, "text/html; charset=UTF-8", "UTF-8")
}

fun Context.screenSize(): IntArray {
    val window = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val displayMetrics = DisplayMetrics()
    window.defaultDisplay.getMetrics(displayMetrics)
    return intArrayOf(displayMetrics.widthPixels, displayMetrics.heightPixels)
}

infix fun Context.takeDrawable(resId: Int): Drawable? = ContextCompat.getDrawable(this, resId)
infix fun Context.takeColor(colorId: Int) = ContextCompat.getColor(this, colorId)
infix fun Context.takeFont(fontId: Int) = ResourcesCompat.getFont(this, fontId)

operator fun Context.get(resId: Int): String = getString(resId)

infix fun Context.inflate(layoutResId: Int): View =
        LayoutInflater.from(this).inflate(layoutResId, null)

operator fun Fragment.get(resId: Int): String = getString(resId)

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.getInputMethodManager(): InputMethodManager = getSystemServiceAs(Context.INPUT_METHOD_SERVICE)

fun Activity.hideSoftKeyboard() {
    val im = getInputMethodManager()
    im.hideSoftInputFromWindow(currentFocus.windowToken, 0)
}

inline fun delay(milliseconds: Long, crossinline action: () -> Unit) {
    Handler().postDelayed({
        action()
    }, milliseconds)
}

// api 17 or Above
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
inline fun JBMr1orAbove(body: () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        body()
    }
}

// api 18 or Above
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
inline fun JBMr2orAbove(body: () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
        body()
    }
}

// api 19 or Above
@TargetApi(Build.VERSION_CODES.KITKAT)
inline fun KKorAbove(body: () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        body()
    }
}

// api 21 or Above
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
inline fun LLorAbove(body: () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        body()
    }
}

// api 23 or Above
@TargetApi(Build.VERSION_CODES.M)
inline fun MorAbove(body: () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        body()
    }
}

// api 24 or Above
@TargetApi(Build.VERSION_CODES.N)
inline fun NGorAbove(body: () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        body()
    }
}

@Suppress("DEPRECATION")
@SuppressLint("NewApi")
fun String.toHtml(): Spanned {
    NGorAbove {
        return Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT)
    }
    return Html.fromHtml(this)
}

fun <T> unSafeLazy(initializer: () -> T): Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) {
        initializer()
    }
}

fun Int.isZero(): Boolean = this == 0

inline fun <F, S> doubleWith(first: F, second: S, runWith: F.(S) -> Unit) {
    first.runWith(second)
}

val Any?.isNull: Boolean
    get() = this == null

fun Float.pxToDp(): Int {
    val metrics = Resources.getSystem().displayMetrics
    val dp = this / (metrics.densityDpi / 160f)
    return Math.round(dp)
}

fun Float.dpToPx(): Int {
    val metrics = Resources.getSystem().displayMetrics
    val px = this * (metrics.densityDpi / 160f)
    return Math.round(px)
}

fun Int.pxToDp(): Int {
    val metrics = Resources.getSystem().displayMetrics
    val dp = this / (metrics.densityDpi / 160f)
    return Math.round(dp)
}

fun Int.dpToPx(): Int {
    val metrics = Resources.getSystem().displayMetrics
    val px = this * (metrics.densityDpi / 160f)
    return Math.round(px)
}


@Suppress("UNCHECKED_CAST")
fun <T> Context.getSystemServiceAs(serviceName: String): T =
        this.getSystemService(serviceName) as T