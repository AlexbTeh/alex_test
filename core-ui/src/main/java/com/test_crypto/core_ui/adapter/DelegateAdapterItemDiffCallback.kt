package com.test_crypto.core_ui.adapter

import androidx.recyclerview.widget.DiffUtil


internal class DelegateAdapterItemDiffCallback: DiffUtil.ItemCallback<DelegateAdapterItem>() {
    override fun areItemsTheSame(oldItem: DelegateAdapterItem, newItem: DelegateAdapterItem): Boolean =
            oldItem::class == newItem::class && oldItem.id() == newItem.id()

    override fun areContentsTheSame(oldItem: DelegateAdapterItem, newItem: DelegateAdapterItem): Boolean =
        oldItem.isContentTheSame(newItem.content)

    override fun getChangePayload(oldItem: DelegateAdapterItem, newItem: DelegateAdapterItem): List<DelegateAdapterItem.Payloadable> =
            oldItem.payload(newItem.content)
}