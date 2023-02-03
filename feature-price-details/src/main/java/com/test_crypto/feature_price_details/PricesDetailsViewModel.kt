package com.test_crypto.feature_price_details

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.intuit.sdp.R
import com.test_crypto.components.ItemHeader
import com.test_crypto.components.ItemHeaderBinder
import com.test_crypto.core_ui.adapter.DelegateAdapterItem
import com.test_crypto.core_ui.mvi.BaseViewModel
import com.test_crypto.core_ui.mvi.Reducer
import com.test_crypto.core_ui.styles.Margin
import com.test_crypto.core_ui.styles.Text
import com.test_crypto.domain.DataState
import com.test_crypto.domain.core.collect
import com.test_crypto.domain.core.runCase
import com.test_crypto.domain.home.AddOrDeleteWatchListFlowUseCase
import com.test_crypto.domain.home.CurrencyFlowUseCase
import com.test_crypto.domain.home.entities.CurrencyDetails
import com.test_crypto.feature_price_details.mvi.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PricesDetailsViewModel @Inject constructor(
    private val currencyUseCase: CurrencyFlowUseCase,
    private val addWatchListUseCase: AddOrDeleteWatchListFlowUseCase,
) : BaseViewModel<PriceDetailsState, PriceDetailsIntent, PriceDetailsResult, Reducer<PriceDetailsResult, PriceDetailsState>>() {
    private var currencyJob: Job? = null
    private var dataCurrencyDetails = CurrencyDetails()

    override val state: Flow<PriceDetailsState>
        get() = reducer.state

    override fun dispatch(intent: PriceDetailsIntent) {
        when (intent) {
            is PriceDetailsIntent.LoadData -> initialLoad(intent.id)
            is PriceDetailsIntent.LoadItems -> initItems()
            is PriceDetailsIntent.AddWatchList -> addWatchList(intent.id, intent.isAdd)
        }
    }

    override val reducer: Reducer<PriceDetailsResult, PriceDetailsState> = PriceDetailsReducer()

    private fun addWatchList(id: Int, isAdd: Boolean) {
        dataCurrencyDetails.isFavorite = isAdd
        val params = AddOrDeleteWatchListFlowUseCase.Params(id, isAdd, dataCurrencyDetails)
        addWatchListUseCase.runCase(viewModelScope, params) {
            when (it) {
                is DataState.Loading -> {
                    reducer.sendEvent(PriceDetailsResult.LoadingFavorite)
                }
                is DataState.Error -> {
                    reducer.sendEvent(PriceDetailsResult.Error(it.error))
                }
                is DataState.Success -> {
                    initItems()
                }
            }
        }
    }

    private fun initialLoad(id: Int) {
        val params = CurrencyFlowUseCase.Params(id)

        currencyJob?.cancelChildren()
        currencyJob = currencyUseCase.collect(viewModelScope, params, emitLoading = false) {
            if (it is DataState.Success) {
                dataCurrencyDetails = it.data
                initItems()
            } else {
                reducer.sendEvent(it.toDataCurrencyDetails())
            }
        }
    }

    private fun initItems() {
        val adapterItems = mutableListOf<DelegateAdapterItem>()
        val description = dataCurrencyDetails.description
        val name = dataCurrencyDetails.name

        setDescription(description, name).map {
            adapterItems.add(it)
        }

        reducer.sendEvent(
            PriceDetailsResult.Items(
                adapterItems,
                isFavorite = dataCurrencyDetails.isFavorite,
                dataCurrency = dataCurrencyDetails
            )
        )
    }

    private fun setDescription(
        description: String?,
        name: String?
    ): MutableList<DelegateAdapterItem> {
        val adapterItems = mutableListOf<DelegateAdapterItem>()
        description?.let {
            name?.let { name ->
                adapterItems.add(
                    ItemHeaderBinder(
                        ItemHeader(
                            tag = "description",
                            margin = Margin.Only(
                                top = R.dimen._25sdp,
                                left = R.dimen._17sdp
                            ),
                            titleSize = R.dimen._16sdp,
                            title = Text.Simple(it)
                        )
                    )
                )
            }
        }
        return adapterItems
    }
}