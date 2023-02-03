package com.test_crypto.domain.events

import androidx.lifecycle.MutableLiveData

class EventChanges {
    private var updateCurrencies = MutableLiveData<Event<Boolean>>()

    fun listenUpdateCurrencies(): MutableLiveData<Event<Boolean>> {
        return updateCurrencies
    }

    fun updateCurrencies(isShow: Boolean) {
        updateCurrencies.value = Event(isShow)
    }
}