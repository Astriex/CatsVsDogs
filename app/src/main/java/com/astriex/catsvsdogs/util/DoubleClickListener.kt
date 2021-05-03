package com.astriex.catsvsdogs.util

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.View


abstract class DoubleClickListener : View.OnClickListener {
    private val DEFAULT_QUALIFICATION_SPAN: Long = 200
    private var isSingleEvent = false
    private var doubleClickQualificationSpanInMillis: Long = 0
    private var timestampLastClick: Long = 0
    private var handler: Handler? = null
    private var runnable: Runnable? = null

    init {
        doubleClickQualificationSpanInMillis = DEFAULT_QUALIFICATION_SPAN
        timestampLastClick = 0
        handler = Handler(Looper.getMainLooper())
        runnable = Runnable {
            if (isSingleEvent) {
                onSingleClick()
            }
        }
    }

    override fun onClick(v: View?) {
        if (SystemClock.elapsedRealtime() - timestampLastClick < doubleClickQualificationSpanInMillis) {
            isSingleEvent = false
            handler!!.removeCallbacks(runnable!!)
            onDoubleClick()
            return
        }
        isSingleEvent = true
        handler!!.postDelayed(runnable!!, DEFAULT_QUALIFICATION_SPAN)
        timestampLastClick = SystemClock.elapsedRealtime()
    }

    abstract fun onDoubleClick()
    abstract fun onSingleClick()
}