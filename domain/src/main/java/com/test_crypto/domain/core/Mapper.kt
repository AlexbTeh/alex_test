package com.test_crypto.domain.core

interface Mapper<I, O> {
    fun map(data: I): O
}