package com.test_crypto.feature_home.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayout
import com.test_crypto.core_ui.BaseFragment
import com.test_crypto.core_ui.extentions.ColorResLink
import com.test_crypto.core_ui.extentions.StringResLink
import com.test_crypto.feature_home.R
import com.test_crypto.feature_home.databinding.FragmentHomeBinding
import com.test_crypto.feature_home.home.adapter.AdapterTabsMain
import com.test_crypto.feature_home.home.mvi.HomeIntent
import com.test_crypto.feature_home.home.mvi.HomeState
import com.test_crypto.feature_home.prices.FragmentPrices
import com.test_crypto.feature_home.watchlist.FragmentWatchList
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FragmentHome : BaseFragment<FragmentHomeBinding, HomeState, HomeViewModel>() {

    private var tabPosition: Int = PRICES

    override val viewModel: HomeViewModel by viewModels()

    private lateinit var fragmentAdapter: AdapterTabsMain

    override fun inflate(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.dispatch(HomeIntent.HomeInit)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        setupTabs()
    }

    override fun render(state: HomeState) {
    }


    private fun setupViewPager() {
        val fragments = mutableListOf<Fragment>()
        fragments.add(FragmentPrices())
        fragments.add(FragmentWatchList())

        fragmentAdapter = AdapterTabsMain(this, fragments)
        requireBinding().viewPager.adapter = fragmentAdapter
        requireBinding().viewPager.isUserInputEnabled = false
    }

    private fun setupTabs() {
        val tabLayout: TabLayout = requireBinding().tabs
        val tab1 = tabLayout.newTab()
        val tab2 = tabLayout.newTab()
        setCustomTabView(tab1, PRICES)
        setCustomTabView(tab2, WATCHLIST)
        tabLayout.addTab(tab1)
        tabLayout.addTab(tab2)


        requireBinding().tabs.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.position?.let { position ->
                    tabPosition = position
                    requireBinding().viewPager.setCurrentItem(tabPosition, false)
                    changeTabIconColor(requireBinding().tabs.getTabAt(tabPosition), true)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                changeTabIconColor(tab, false)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        println("tabPosition $tabPosition")
        changeTabIconColor(tabLayout.getTabAt(tabPosition), true)
        tabLayout.selectTab(tabLayout.getTabAt(tabPosition))
    }

    private fun setCustomTabView(tab: TabLayout.Tab, position: Int) {
        val view: View = LayoutInflater.from(requireContext())
            .inflate(R.layout.main_tab_item, view as ViewGroup, false)

        val title = when (position) {
            PRICES -> getString(StringResLink.prices)
            WATCHLIST -> getString(StringResLink.watchlist)
            else -> getString(StringResLink.watchlist)
        }
        view.findViewById<TextView>(R.id.title).text = title

        tab.customView = view
    }

    private fun changeTabIconColor(tab: TabLayout.Tab?, isSelected: Boolean) {
        context?.let {
            val colorNormal = ContextCompat.getColor(it, ColorResLink.black)
            val colorSelected = ContextCompat.getColor(it, ColorResLink.selected_color)
            val title = tab?.customView?.findViewById<AppCompatTextView>(R.id.title)
            if (isSelected) {
                title?.setTextColor(colorSelected)
            } else {
                title?.setTextColor(colorNormal)
            }
        }
    }

    companion object {
        const val PRICES = 0
        const val WATCHLIST = 1
    }
}