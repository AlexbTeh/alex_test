package com.test_crypto.core_ui.adapter

abstract class DelegateAdapterItem(val content: Item) {
    abstract fun id(): Any

    abstract fun isContentTheSame(other: Item): Boolean

    abstract fun payload(other: Item): List<Payloadable>

    /**
     * Simple marker interface for payloads
     */
    interface Payloadable {
        object None : Payloadable
    }
}


interface Item {
    override fun equals(other: Any?): Boolean
}

abstract class ItemWithError(open var error: String?) : Item

data class EmptyItem(val id : Long = -1) : Item


class EmptyDelegateItem(item : Item) : DelegateAdapterItem(item) {
    override fun id() = -1

    override fun isContentTheSame(other: Item): Boolean {
        return true
    }

    override fun payload(other: Item): List<Payloadable> {
        return mutableListOf()
    }
}

interface Actions {}
