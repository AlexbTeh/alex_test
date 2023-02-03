package com.test_crypto.core_ui

import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun Fragment.showSuccess(message: String) {
    val snackbar = Snackbar.make(
        activity!!.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG
    )
        .setActionTextColor(ContextCompat.getColor(activity!!, android.R.color.white))
    snackbar.view.setBackgroundColor(
        ContextCompat.getColor(
            activity!!,
            android.R.color.holo_green_light
        )
    )
    snackbar.show()
}


fun Fragment.showError(message: String) {
    val snackbar = Snackbar.make(
        activity!!.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG
    )
        .setActionTextColor(ContextCompat.getColor(activity!!, android.R.color.white))
    snackbar.view.setBackgroundColor(
        ContextCompat.getColor(
            activity!!,
            android.R.color.holo_red_dark
        )
    )
    snackbar.show()
}

fun Fragment.showPending(message: String) {
    val snackbar = Snackbar.make(
        activity!!.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG
    )
        .setActionTextColor(ContextCompat.getColor(activity!!, android.R.color.white))
    snackbar.view.setBackgroundColor(Color.parseColor("#0D6CF2"))
    snackbar.show()
}