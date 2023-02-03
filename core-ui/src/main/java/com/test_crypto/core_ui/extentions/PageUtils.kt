package com.test_crypto.core_ui.extentions

import androidx.collection.LongSparseArray
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import kotlin.reflect.full.superclasses

fun FragmentStateAdapter.getItem(position: Int): Fragment? {
    return this::class.superclasses.find { it == FragmentStateAdapter::class }
        ?.java?.getDeclaredField("mFragments")
        ?.let { field ->
            field.isAccessible = true
            val mFragments = field.get(this) as LongSparseArray<Fragment>
            return@let mFragments[getItemId(position)]
        }
}