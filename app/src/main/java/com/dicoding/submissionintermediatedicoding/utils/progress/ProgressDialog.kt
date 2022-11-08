package com.dicoding.submissionintermediatedicoding.utils.progress

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import com.dicoding.submissionintermediatedicoding.R

class ProgressDialog (context: Activity?) {
    private var activity: Activity? = null
    private var dialog: AlertDialog? = null

    init {
        activity = context
    }
    @SuppressLint("InflateParams")
    fun startProgressDialog() {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity!!.layoutInflater
        builder.setView(inflater.inflate(R.layout.progress_dialog, null))
        dialog = builder.create()
        if (dialog != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(0))
            dialog!!.setCancelable(false)
            dialog!!.show()
        }
    }

    fun dismissProgressDialog() {
        dialog!!.dismiss()
    }
}