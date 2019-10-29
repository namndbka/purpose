package vn.zuni.findpurpose.extensions

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

/**
 *
 * Created by namnd on 10/17/17.
 */

object PermissionUtils {

    // For Android 6 or above
    fun verifyAllPermissions(permissions: IntArray): Boolean {
        for (result in permissions) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    fun hasSelfPermission(activity: Context, permissions: Array<String>): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        // Verify that all the permissions.
        permissions.forEach {
            if (activity.checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED) return false
        }
        return true
    }
}
