package vn.zuni.findpurpose.extensions

import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.net.Uri

/**
 *
 * Created by namnd on 10/21/17.
 */
fun Context.sendEmail(subject: String,
                      senderMail: String,
                      sendText: String) {
    val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
            "mailto", senderMail, null))
    emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
    emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(senderMail))
    startActivity(Intent.createChooser(emailIntent, sendText))
}

inline fun Context.actionView(url: () -> String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url()))
    startActivity(intent)
}

fun Context.getAccountManager(): AccountManager =
        getSystemServiceAs(Context.ACCOUNT_SERVICE)

fun Context.getAudioManager(): AudioManager =
        getSystemServiceAs(Context.AUDIO_SERVICE)