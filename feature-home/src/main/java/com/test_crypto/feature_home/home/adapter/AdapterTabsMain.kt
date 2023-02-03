package com.test_crypto.feature_home.home.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


class AdapterTabsMain(fragment: Fragment, private val fragments : List<Fragment>) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}