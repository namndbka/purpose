package vn.zuni.findpurpose.extensions

import android.content.Context
import android.net.ConnectivityManager

/**
 *
 * Created by namnd on 10/25/17.
 */


fun Context.getConnectivityManager(): ConnectivityManager = getSystemServiceAs(Context.CONNECTIVITY_SERVICE)

fun Context.isWifiConnected(): Boolean {
    val cm = getConnectivityManager()
    val networkInfo = cm.activeNetworkInfo
    return networkInfo.type == ConnectivityManager.TYPE_WIFI
}

/**
 * Check whether the mobile network (4G / 3G / 2G) is connected
 */
fun Context.isMobileConnected(): Boolean {
    val cm = getConnectivityManager()
    val networkInfo = cm.activeNetworkInfo
    return networkInfo.type == ConnectivityManager.TYPE_MOBILE
}

/**
 * Check if there is a network available
 */
fun Context.isNetworkConnected(): Boolean {
    val cm = getConnectivityManager()
    val info = cm.activeNetworkInfo
    return info != null && info.isConnectedOrConnecting
}