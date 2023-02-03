package com.test_crypto.feature_home.watchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.test_crypto.components.ItemHeaderDelegete
import com.test_crypto.core_ui.BaseFragment
import com.test_crypto.core_ui.adapter.Actions
import com.test_crypto.core_ui.adapter.CompositeAdapter
import com.test_crypto.feature_home.databinding.FragmentWatchListBinding
import com.test_crypto.feature_home.prices.adapter.ItemCurrencyDelegate
import com.test_crypto.feature_home.prices.adapter.PriceDetailsAction
import com.test_crypto.feature_home.watchlist.mvi.WatchListIntent
import com.test_crypto.feature_home.watchlist.mvi.WatchListState
import com.test_crypto.nav.PricesDetailsNavContract
import com.test_crypto.nav.horizontal
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FragmentWatchList : BaseFragment<FragmentWatchListBinding, WatchListState, WatchListViewModel>() {

    override val viewModel: WatchListViewModel by viewModels()

    @Inject
    lateinit var pricesDetailsNavContract: PricesDetailsNavContract

    private val compositeAdapter by lazy {
        CompositeAdapter.Builder()
            .add(ItemHeaderDelegete())
            .add(ItemCurrencyDelegate())
            .addActionProcessor {
                processActions(it)
            }
            .build()
    }

    private fun processActions(action: Actions) {
        when (action) {
            is PriceDetailsAction.Click -> {
                action.id?.let { openPriceDetails(it) }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.dispatch(WatchListIntent.InitialLoad)
    }

    override fun inflate(inflater: LayoutInflater, container: ViewGroup?): FragmentWatchListBinding {
        return FragmentWatchListBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        requireBinding().recyclerView.layoutManager = LinearLayoutManager(context)
        requireBinding().recyclerView.adapter = compositeAdapter
    }

    override fun render(state: WatchListState) {
        state.items?.let {
            compositeAdapter.submitList(it)
        }

        state.error?.let {
            processApiError(it)
        }
    }

    private fun openPriceDetails(id: Int) {
        navController?.horizontal(pricesDetailsNavContract.openCurrencyDetails(id))
    }
}