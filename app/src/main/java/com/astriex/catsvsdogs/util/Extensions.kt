package com.astriex.catsvsdogs.util

import android.net.ConnectivityManager
import android.os.Build
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun Fragment.isConnected(): Boolean {
    var status = false
    val conManager =
        requireContext().getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        if (conManager != null && conManager.activeNetwork != null && conManager.getNetworkCapabilities(
                conManager.activeNetwork
            ) != null
        ) {
            status = true
        }
    } else {
        if (conManager.activeNetwork != null) {
            status = true
        }
    }
    return status
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.INVISIBLE
}