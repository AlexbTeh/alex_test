package com.test_crypto.core_ui.adapter

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.util.forEach
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

@Suppress("UNCHECKED_CAST")
class CompositeAdapter(
    private val delegates: SparseArray<DelegateBinder<DelegateAdapterItem, RecyclerView.ViewHolder>>
) : ListAdapter<DelegateAdapterItem, RecyclerView.ViewHolder>(DelegateAdapterItemDiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        for (i in 0 until delegates.size()) {
            if (delegates[i].modelClass == getItem(position).javaClass) {
                return delegates.keyAt(i)
            }
        }
        throw NullPointerException("Can not get viewType for position $position")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return delegates[viewType].createViewHolder(layoutInflater, parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        onBindViewHolder(holder, position, mutableListOf())

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        val delegateAdapter = delegates[getItemViewType(position)]
        if (delegateAdapter != null) {
            val delegatePayloads = mutableListOf<DelegateAdapterItem.Payloadable>()
            payloads.forEach {
                if (it is List<*>)
                    delegatePayloads.addAll(it as List<DelegateAdapterItem.Payloadable>)
            }
            delegateAdapter.bindViewHolder(getItem(position), holder, delegatePayloads)
        } else {
            throw NullPointerException("can not find adapter for position $position")
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        delegates[holder.itemViewType].onViewRecycled(holder)
        super.onViewRecycled(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        delegates[holder.itemViewType].onViewDetachedFromWindow(holder)
        super.onViewDetachedFromWindow(holder)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        delegates[holder.itemViewType].onViewAttachedToWindow(holder)
        super.onViewAttachedToWindow(holder)
    }

    class Builder {
        private var count: Int = 0
        private val delegates: SparseArray<DelegateBinder<DelegateAdapterItem, RecyclerView.ViewHolder>> =
            SparseArray()

        fun add(delegateBinder: DelegateBinder<out DelegateAdapterItem, *>): Builder {
            delegates.put(
                count++,
                delegateBinder as DelegateBinder<DelegateAdapterItem, RecyclerView.ViewHolder>
            )
            return this
        }

        fun addActionProcessor(actions: (Actions) -> Unit): Builder {
            delegates.forEach { _, value ->
                value.action = actions
            }
            return this
        }

        fun build(): CompositeAdapter {
            require(count != 0) { "Register at least one adapter" }
            return CompositeAdapter(delegates)
        }
    }

    fun removeItem(position: Int) {
        val items = mutableListOf<DelegateAdapterItem?>()
        items.addAll(currentList)
        if (items[position] != null) {
            items.removeAt(position)
            submitList(items)
        }
    }

    fun removeItem(item: DelegateAdapterItem) {
        val items = mutableListOf<DelegateAdapterItem>()
        items.addAll(currentList)
        if (items.contains(item)) {
            items.remove(item)
            submitList(items)
        }
    }

    fun addItem(item: DelegateAdapterItem, position: Int = 0) {
        val items = mutableListOf<DelegateAdapterItem>()
        items.addAll(currentList)
        if (position >= currentList.size) {
            items.add(item)
        } else
            items.add(position, item)
        submitList(items)
    }
}

