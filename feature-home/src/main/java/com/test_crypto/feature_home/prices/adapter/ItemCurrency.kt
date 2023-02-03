package com.test_crypto.feature_home.prices.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test_crypto.core_ui.adapter.Actions
import com.test_crypto.core_ui.adapter.DelegateAdapterItem
import com.test_crypto.core_ui.adapter.DelegateBinder
import com.test_crypto.core_ui.adapter.Item
import com.test_crypto.core_ui.extentions.DrawableResLink
import com.test_crypto.core_ui.styles.*
import com.test_crypto.feature_home.databinding.ItemCurrencyBinding


data class ItemCurrency(
    val tag: String,
    val margin: IMargin = IMargin.None,
    var name: String? = null,
    var symbol: String? = null,
    var iconUrl: String? = null,
    var price: String? = null,
    var id: Int
) : Item

class ItemCurrencyBinder(val item: ItemCurrency) : DelegateAdapterItem(item) {
    override fun id(): Any = item.tag
    override fun isContentTheSame(other: Item): Boolean {
        if (other is ItemCurrency) {
            return other.name == item.name
                    && other.symbol == item.symbol
                    && other.iconUrl == item.iconUrl
                    && other.price == item.price
                    && other.id == item.id
        }

        return false
    }

    override fun payload(other: Item): List<Payloadable> {
        val payloads = mutableListOf<Payloadable>()
        if (other is ItemCurrency) {
            payloads.apply {
                if (other.name != item.name)
                    add(Payloads.NameChanged(other.name))
                if (other.symbol != item.symbol)
                    add(Payloads.SymbolChanged(other.symbol))
                if (other.iconUrl != item.iconUrl)
                    add(Payloads.IconChanged(other.iconUrl))
                if (other.price != item.price)
                    add(Payloads.ExchangeRateChanged(other.price))
            }
        }
        return payloads
    }


    sealed class Payloads : Payloadable {
        data class NameChanged(val text: String?) : Payloads()
        data class SymbolChanged(val text: String?) : Payloads()
        data class IconChanged(val url: String?, val hex: String? = null) : Payloads()
        data class ExchangeRateChanged(val text: String?) : Payloads()
    }
}

class ItemCurrencyDelegate :
    DelegateBinder<ItemCurrencyBinder, ItemCurrencyDelegate.ViewHolderItemBenefitBinder>(
        ItemCurrencyBinder::class.java
    ) {
    override fun onViewAttachedToWindow(viewHolder: ViewHolderItemBenefitBinder) {
        super.onViewAttachedToWindow(viewHolder)

    }

    override fun createViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): RecyclerView.ViewHolder {

        val view = ItemCurrencyBinding.inflate(inflater, parent, false)
        return ViewHolderItemBenefitBinder(view)
    }


    override fun bindViewHolder(
        model: ItemCurrencyBinder,
        viewHolder: ViewHolderItemBenefitBinder,
        payloads: List<DelegateAdapterItem.Payloadable>
    ) {
        if (payloads.isEmpty())
            viewHolder.bind(model.item)
        else {
            payloads.forEach {
                when (it) {
                    is ItemCurrencyBinder.Payloads.NameChanged -> {
                        viewHolder.setName(it.text)
                    }
                    is ItemCurrencyBinder.Payloads.SymbolChanged -> {
                        viewHolder.setSymbol(it.text)
                    }
                    is ItemCurrencyBinder.Payloads.IconChanged -> {
                        viewHolder.loadImage(it.url)
                    }
                    is ItemCurrencyBinder.Payloads.ExchangeRateChanged -> {
                        viewHolder.setExchangeRate(it.text)
                    }
                }
            }
            viewHolder.setClicks(model.item)
        }
    }

    inner class ViewHolderItemBenefitBinder(
        private val binding: ItemCurrencyBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItemCurrency) {
            loadImage(item.iconUrl)
            setName(item.name)
            setSymbol(item.symbol)
            setExchangeRate(item.price)
            setClicks(item)
            setMargin(item)
        }

        private fun setMargin(item: ItemCurrency) {
            if (item.margin != IMargin.None) {
                binding.container.setMargin(item.margin)
            } else {
                binding.container.setMargin(
                    Margin.Only(
                        top = com.intuit.sdp.R.dimen._6sdp,
                        bottom = com.intuit.sdp.R.dimen._6sdp,
                        left = com.intuit.sdp.R.dimen._17sdp,
                        right = com.intuit.sdp.R.dimen._17sdp
                    )
                )
            }
        }

        fun setClicks(item: ItemCurrency) {
            binding.root.setOnClickListener {
                action(PriceDetailsAction.Click(item.id))
            }
        }

        fun setName(title: String?) {
            binding.name.text = title
        }

        fun setSymbol(title: String?) {
            binding.symbol.text = title
        }

        fun setExchangeRate(exchangeRate: String?) {
            when {
                exchangeRate != null -> {
                    binding.exchangeRate.text = exchangeRate
                }
                else -> {
                    binding.exchangeRate.text = ""
                }
            }
        }

        fun loadImage(url: String?) {
           if (url != null) {
                binding.image.load(Image.Network(url))
            } else {
                binding.image.load(Image.Resource(DrawableResLink.image_holder_bg))
            }
        }
    }
}

sealed class PriceDetailsAction : Actions {
    data class Click(val id: Int?) : PriceDetailsAction()
}