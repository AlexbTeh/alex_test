package com.test_crypto.feature_home.prices

import androidx.lifecycle.viewModelScope
import com.intuit.sdp.R
import com.test_crypto.components.ItemHeader
import com.test_crypto.components.ItemHeaderBinder
import com.test_crypto.core_ui.adapter.DelegateAdapterItem
import com.test_crypto.core_ui.extentions.StringResLink
import com.test_crypto.core_ui.extentions.formatNumberWithSymbol
import com.test_crypto.core_ui.mvi.BaseViewModel
import com.test_crypto.core_ui.mvi.Reducer
import com.test_crypto.core_ui.styles.Margin
import com.test_crypto.core_ui.styles.Text
import com.test_crypto.domain.DataState
import com.test_crypto.domain.core.collect
import com.test_crypto.domain.core.runCase
import com.test_crypto.domain.home.GetCurrenciesUseCase
import com.test_crypto.domain.home.SyncCurrenciesScenario
import com.test_crypto.domain.home.entities.DataCurrency
import com.test_crypto.domain.home.entities.FilterId
import com.test_crypto.feature_home.prices.adapter.ItemCurrency
import com.test_crypto.feature_home.prices.adapter.ItemCurrencyBinder
import com.test_crypto.feature_home.prices.mvi.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PricesViewModel @Inject constructor(
    private val getCurrenciesUseCase: GetCurrenciesUseCase,
    private val syncScenario: SyncCurrenciesScenario,
) : BaseViewModel<PricesState, PricesIntent, PricesResult, Reducer<PricesResult, PricesState>>() {
    private var getCurrenciesJob: Job? = null

    override val state: Flow<PricesState>
        get() = reducer.state

    private val filter = FilterId(FilterId.ALL_ASSETS)

    override fun dispatch(intent: PricesIntent) {
        when (intent) {
            is PricesIntent.LoadItems -> viewModelScope.launch {
                initItems(intent.data)
            }
            is PricesIntent.SelectFilter -> getData(intent.id)
            is PricesIntent.InitialLoad -> {
                initialLoad()
            }
            is PricesIntent.RefreshIntent -> reload()
            is PricesIntent.UnselectFilter -> {
                getData(FilterId.ALL_ASSETS)
            }
            PricesIntent.HideWatchlistIntent -> {
                getData(FilterId.ALL_ASSETS)
            }
            PricesIntent.ShowWatchListIntent -> getData(FilterId.WATCHLIST)
        }
    }

    override val reducer: Reducer<PricesResult, PricesState> = PricesReducer()

    private fun initialLoad() {
        println("FragmentPrices initialLoad ")

        syncScenario.runCase(viewModelScope, emitLoading = true) { result ->
            getData(FilterId.ALL_ASSETS)
            if (result is DataState.Error) {
                reducer.sendEvent(result.toResult())
            }
        }
    }

    private fun reload() {
        syncScenario.runCase(viewModelScope)
    }

    private fun getData(id: Int) {
        filter.id = id
        val params = GetCurrenciesUseCase.Params(FilterId(id))

        getCurrenciesJob?.cancelChildren()
        getCurrenciesJob =
            getCurrenciesUseCase.collect(viewModelScope, params, emitLoading = false) {
                if (it is DataState.Success) {
                    dispatch(PricesIntent.LoadItems(it.data))
                } else {
                    reducer.sendEvent(it.toItems())
                }
            }
    }

    private suspend fun initItems(data: DataCurrency) {
        withContext(Dispatchers.Default) {
            val adapterItems = mutableListOf<DelegateAdapterItem>()

            adapterItems.add(
                ItemHeaderBinder(
                    ItemHeader(
                        margin = Margin.Only(
                            left = R.dimen._17sdp,
                            top = R.dimen._10sdp
                        ),
                        tag = "title",
                        title = Text.Resource(StringResLink.prices),
                        titleSize = com.intuit.ssp.R.dimen._24ssp,
                    )
                )
            )


            data.currencies.mapIndexed { index, priceCurrency ->
                val calculateRate = priceCurrency.price?.toBigDecimal()?.toPlainString()
                    ?: 0.00.toString()

                val rate = formatNumberWithSymbol(
                    calculateRate,
                    count = 2,
                    symbol = ""
                )

                adapterItems.add(
                    ItemCurrencyBinder(
                        ItemCurrency(
                            tag = "tag" + priceCurrency.id,
                            name = priceCurrency.name,
                            symbol = priceCurrency.symbol,
                            iconUrl = priceCurrency.iconUrl,
                            price = rate,
                            id = priceCurrency.id
                        )
                    )
                )
            }

            val showEmptyPage = data.currencies.isEmpty()
            reducer.sendEvent(PricesResult.Items(adapterItems, showEmptyPage))
        }
    }
}


