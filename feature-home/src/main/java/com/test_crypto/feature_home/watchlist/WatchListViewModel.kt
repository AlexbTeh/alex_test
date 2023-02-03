package com.test_crypto.feature_home.watchlist

import androidx.lifecycle.viewModelScope
import com.intuit.sdp.R
import com.test_crypto.components.ItemHeader
import com.test_crypto.components.ItemHeaderBinder
import com.test_crypto.core_ui.adapter.DelegateAdapterItem
import com.test_crypto.core_ui.extentions.StringResLink
import com.test_crypto.core_ui.mvi.BaseViewModel
import com.test_crypto.core_ui.mvi.Reducer
import com.test_crypto.core_ui.styles.Margin
import com.test_crypto.core_ui.styles.Text
import com.test_crypto.domain.DataState
import com.test_crypto.domain.core.collect
import com.test_crypto.domain.home.entities.DataWatchList
import com.test_crypto.domain.home.entities.WatchListsUseCase
import com.test_crypto.feature_home.prices.adapter.ItemCurrency
import com.test_crypto.feature_home.prices.adapter.ItemCurrencyBinder
import com.test_crypto.feature_home.watchlist.mvi.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class WatchListViewModel @Inject constructor(
    private val getWatchListsUseCase: WatchListsUseCase,
) : BaseViewModel<WatchListState, WatchListIntent, WatchListResult, Reducer<WatchListResult, WatchListState>>() {
    private var getCurrenciesJob: Job? = null

    override val state: Flow<WatchListState>
        get() = reducer.state

    override fun dispatch(intent: WatchListIntent) {
        when (intent) {
            is WatchListIntent.LoadItems -> viewModelScope.launch {
                initItems(intent.data)
            }
            is WatchListIntent.InitialLoad -> {
                initialLoad()
            }
        }
    }

    override val reducer: Reducer<WatchListResult, WatchListState> = WatchListReducer()


    private fun initialLoad() {
        val params = WatchListsUseCase.Params
        getCurrenciesJob?.cancelChildren()
        getCurrenciesJob =
            getWatchListsUseCase.collect(viewModelScope, params, emitLoading = false) {
                if (it is DataState.Success) {
                    dispatch(WatchListIntent.LoadItems(it.data))
                } else {
                    reducer.sendEvent(it.toItems())
                }
            }
    }

    private suspend fun initItems(data: DataWatchList) {
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
                        title = Text.Resource(StringResLink.watchlist),
                        titleSize = com.intuit.ssp.R.dimen._24ssp,
                    )
                )
            )


            data.watchLists.mapIndexed { index, priceCurrency ->

                adapterItems.add(
                    ItemCurrencyBinder(
                        ItemCurrency(
                            tag = "tag" + priceCurrency.id,
                            name = priceCurrency.name,
                            symbol = priceCurrency.symbol,
                            iconUrl = priceCurrency.iconUrl,
                            id = priceCurrency.id
                        )
                    )
                )
            }

            val showEmptyPage = data.watchLists.isEmpty()
            reducer.sendEvent(WatchListResult.Items(adapterItems, showEmptyPage))
        }
    }
}


