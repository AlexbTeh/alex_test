package com.test_crypto.feature_price_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.test_crypto.components.ItemHeaderDelegete
import com.test_crypto.core_ui.BaseFragment
import com.test_crypto.core_ui.adapter.Actions
import com.test_crypto.core_ui.adapter.CompositeAdapter
import com.test_crypto.core_ui.extentions.DrawableResLink
import com.test_crypto.feature_price_details.databinding.FragmentPriceDetailsBinding
import com.test_crypto.feature_price_details.mvi.PriceDetailsIntent
import com.test_crypto.feature_price_details.mvi.PriceDetailsState
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FragmentPricesDetails :
    BaseFragment<FragmentPriceDetailsBinding, PriceDetailsState, PricesDetailsViewModel>() {

    override val viewModel: PricesDetailsViewModel by viewModels()

    private val args: FragmentPricesDetailsArgs by navArgs()


    private val compositeAdapter by lazy {
        CompositeAdapter.Builder()
            .add(ItemHeaderDelegete())
            .addActionProcessor {
                processActions(it)
            }
            .build()
    }

    private fun processActions(action: Actions) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.dispatch(PriceDetailsIntent.LoadData(args.code))
    }

    override fun inflate(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPriceDetailsBinding {
        return FragmentPriceDetailsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireBinding().toolbar.showRightIcon(false)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        requireBinding().recyclerView.layoutManager = LinearLayoutManager(context)
        requireBinding().recyclerView.adapter = compositeAdapter
    }

    override fun render(state: PriceDetailsState) {
        requireBinding().loading.isVisible = state.loadingFavorite || state.loading

        state.items?.let {
            println(" state.items ${state.dataCurrencyDetails}")
            compositeAdapter.submitList(it)
        }

        println(" state.dataCurrencyDetails ${state.dataCurrencyDetails}")

        state.dataCurrencyDetails?.let {
            it.name?.let { name -> requireBinding().toolbar.setTitle(name) }
            requireBinding().toolbar.loadAvatar(it.iconUrl)
        }

        requireBinding().toolbar.setLeftClickListener {
            navigatePopBackStack()
        }

        requireBinding().toolbar.showAvatar(true)
        requireBinding().toolbar.loadAvatar(state.dataCurrencyDetails?.iconUrl)
        requireBinding().toolbar.showRightIcon(true)
        requireBinding().toolbar.setRightIcon(DrawableResLink.add_watch_selector) {
            viewModel.dispatch(
                PriceDetailsIntent.AddWatchList(
                    args.code,
                    !state.isFavorite
                )
            )
        }

        setFavorite(state.isFavorite)

        state.error?.let {
            processApiError(it)
        }
    }

    private fun setFavorite(isFavorite: Boolean) {

        requireBinding().toolbar.setSelectedRightIcon(isFavorite)
    }
}