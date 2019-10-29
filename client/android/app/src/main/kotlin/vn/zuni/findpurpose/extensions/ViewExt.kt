package vn.zuni.findpurpose.extensions

import android.graphics.PorterDuff
import android.support.annotation.Px
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

/**
 *
 * Created by namnd on 10/21/17.
 */
fun TextView.leftIcon(drawableId: Int) {
    setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, drawableId), null, null, null)
}


fun TextView.asString(): String {
    return text.toString()
}

fun EditText.asString(): String {
    return text.toString()
}

var View.scale: Float
    get() = Math.min(scaleX, scaleY)
    set(value) {
        scaleY = value
        scaleX = value
    }

fun View.addTopMargin(@Px marginInPx: Int) {
    (layoutParams as ViewGroup.MarginLayoutParams).topMargin = marginInPx
}

fun View.addBottomMargin(@Px marginInPx: Int) {
    (layoutParams as ViewGroup.MarginLayoutParams).bottomMargin = marginInPx
}

fun View.addLeftMargin(@Px marginInPx: Int) {
    (layoutParams as ViewGroup.MarginLayoutParams).leftMargin = marginInPx
}
fun View.show(isShow: Boolean = true) {
    visibility = if (isShow) View.VISIBLE else View.GONE
}

fun View.showOrInvisible(isShow: Boolean = true) {
    visibility = if (isShow) View.VISIBLE else View.INVISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.onClick(function: () -> Unit) {
    setOnClickListener {
        function()
    }
}

fun CompoundButton.onCheckedChanged(f: (CompoundButton, Boolean) -> Unit) {
    this.setOnCheckedChangeListener(f)
}

infix fun ViewGroup.inflate(layoutResId: Int): View =
        LayoutInflater.from(context).inflate(layoutResId, this, false)

fun ImageView.tintColor(color: Int) {
    setColorFilter(color, PorterDuff.Mode.SRC_IN)
}
operator fun ViewGroup.get(index: Int): View = getChildAt(index)