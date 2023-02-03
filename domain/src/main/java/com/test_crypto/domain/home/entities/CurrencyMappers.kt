package com.test_crypto.domain.home.entities

fun Currency.toFeedEntity() : FeedCurrency {
    return FeedCurrency(
        id = id,
        name = name,
        price = price,
        symbol = symbol,
        iconUrl = iconUrl,
        code = code,
    )
}
