package com.test_crypto.core_ui.recycler_view

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

class NoScrollLayoutManager(context : Context?) : LinearLayoutManager(context) {
    override fun canScrollVertically(): Boolean {
        return false
    }
}