package com.test_crypto.core_ui.extentions

import android.view.WindowManager
import androidx.fragment.app.Fragment


fun Fragment.setKeyboardAdjustResize(){
    this.activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
}

fun Fragment.setKeyboardAdjustPan(){
    this.activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
}


fun Fragment.setKeyboardAdjustNothing(){
    this.activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
}