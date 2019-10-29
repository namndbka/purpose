package vn.zuni.findpurpose.extensions

import android.os.Build
import vn.zuni.findpurpose.BuildConfig
import java.util.*


/**
 *
 * Created by namnd on 10/16/17.
 */
object DeviceUtils {

    fun deviceName(): String {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return if (model.startsWith(manufacturer)) {
            model.toUpperCase()
        } else String.format(Locale.US, "%s %s", model.toUpperCase(), model)
    }

    fun userAgent(): String {
        val userAgentTemplate = "ZUNIS %s (v%d) : %s : Android %d"
        return String.format(Locale.US, userAgentTemplate,
                BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE,
                deviceName(), Build.VERSION.SDK_INT
        )
    }
}