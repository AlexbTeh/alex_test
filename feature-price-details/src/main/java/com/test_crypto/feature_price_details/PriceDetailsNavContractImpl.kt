package com.test_crypto.feature_price_details

import android.net.Uri
import androidx.navigation.NavDeepLinkRequest
import com.test_crypto.nav.InternalDeeplinks.CURRENCY
import com.test_crypto.nav.PricesDetailsNavContract
import javax.inject.Inject

class PriceDetailsNavContractImpl @Inject constructor() : PricesDetailsNavContract {
    override fun openCurrencyDetails(id: Int): NavDeepLinkRequest {
        return NavDeepLinkRequest.Builder.fromUri(Uri.parse("$CURRENCY$id")).build()
    }
}