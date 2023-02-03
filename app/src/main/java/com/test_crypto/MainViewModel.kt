package com.test_crypto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test_crypto.domain.core.runCase
import com.test_crypto.domain.home.SyncCurrencies
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val syncCurrenciesAndUser: SyncCurrencies
) : ViewModel() {

    var onShowFaceAuth: ((type: Boolean) -> Unit)? = null

    private var timerJob: Job? = null

    fun init() {
    }


    private fun syncData() {
        syncCurrenciesAndUser.runCase(viewModelScope, emitLoading = false) {
            syncDataEveryMinute()
        }
    }

    fun syncDataEveryMinute() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            val totalSeconds = TimeUnit.SECONDS.toSeconds(59.toLong())
            val tickSeconds = 0
            for (second in totalSeconds downTo tickSeconds) {
                val time = second - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(second))
                if (time.toInt() == 0) {
                    syncData()
                }
                delay(1000)
            }
        }
    }
}