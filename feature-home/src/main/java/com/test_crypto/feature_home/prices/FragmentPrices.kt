package com.test_crypto.feature_home.prices

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
import com.test_crypto.feature_home.databinding.FragmentPricesBinding
import com.test_crypto.feature_home.prices.adapter.ItemCurrencyDelegate
import com.test_crypto.feature_home.prices.adapter.PriceDetailsAction
import dagger.hilt.android.AndroidEntryPoint
import com.test_crypto.feature_home.prices.mvi.PricesIntent
import com.test_crypto.feature_home.prices.mvi.PricesState
import com.test_crypto.nav.PricesDetailsNavContract
import com.test_crypto.nav.horizontal
import javax.inject.Inject

@AndroidEntryPoint
class FragmentPrices : BaseFragment<FragmentPricesBinding, PricesState, PricesViewModel>() {

    override val viewModel: PricesViewModel by viewModels()

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.dispatch(PricesIntent.InitialLoad)
    }

    override fun inflate(inflater: LayoutInflater, container: ViewGroup?): FragmentPricesBinding {
        return FragmentPricesBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        requireBinding().recyclerView.layoutManager = LinearLayoutManager(context)
        requireBinding().recyclerView.adapter = compositeAdapter
    }

    override fun render(state: PricesState) {
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