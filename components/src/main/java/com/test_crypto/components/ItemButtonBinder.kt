package com.test_crypto.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.test_crypto.components.databinding.ItemButtonBinding
import com.test_crypto.core_ui.adapter.Actions
import com.test_crypto.core_ui.adapter.DelegateAdapterItem
import com.test_crypto.core_ui.adapter.DelegateBinder
import com.test_crypto.core_ui.adapter.Item
import com.test_crypto.core_ui.styles.*


data class ItemButton(
    val tag: String,
    val text: Text,
    @DrawableRes val buttonBackground: Int? = null,
    @DrawableRes val iconLeft: Int? = null,
    @DrawableRes val iconCenter: Int? = null,
    val textColor: ItemColor,
    val margin: IMargin = IMargin.None,
    val isSelected: Boolean = false,
    var isEnable: Boolean = true,
) : Item


data class ItemButtonBinder(val item: ItemButton) : DelegateAdapterItem(item) {
    override fun id(): Any = item.tag
    override fun isContentTheSame(other: Item): Boolean {
        if (other is ItemButton) {
            return other.text == item.text
                    && item.buttonBackground == other.buttonBackground
                    && item.textColor == other.textColor
                    && item.iconLeft == other.iconLeft
                    && item.iconCenter == other.iconCenter
                    && item.isEnable == other.isEnable
        }

        return false
    }

    override fun payload(other: Item): List<Payloadable> {
        val payloads = mutableListOf<Payloadable>()
        if (other is ItemButton) {
            payloads.apply {
                if (item.text != other.text)
                    add(ButtonPayload.ButtonTextChanged(other.text))
                if (item.buttonBackground != other.buttonBackground)
                    add(ButtonPayload.BackgroundChanged(other.buttonBackground))
                if (item.textColor != other.textColor)
                    add(ButtonPayload.TextColorChanged(other.textColor))
                if (item.iconLeft != other.iconLeft)
                    add(ButtonPayload.IconLeftChanged(other.iconLeft))
                if (item.iconCenter != other.iconCenter)
                    add(ButtonPayload.IconCenterChanged(other.iconCenter))
                if (item.isEnable != other.isEnable)
                    add(ButtonPayload.EnableChanged(other.isEnable))
            }
        }
        return payloads
    }

    sealed class ButtonPayload : Payloadable {
        class ButtonTextChanged(var text: Text) : ButtonPayload()
        class BackgroundChanged(@DrawableRes val buttonBackground: Int?) : ButtonPayload()
        class TextColorChanged(val textColor: ItemColor) : ButtonPayload()
        class IconLeftChanged(@DrawableRes val iconLeft: Int?) : ButtonPayload()
        class IconCenterChanged(@DrawableRes val iconRight: Int?) : ButtonPayload()
        class EnableChanged(val isEnable: Boolean) : ButtonPayload()
    }
}


class ItemButtonDelegate :
    DelegateBinder<ItemButtonBinder, ItemButtonDelegate.ButtonViewHolder>(ItemButtonBinder::class.java) {
    override fun createViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): RecyclerView.ViewHolder {
        return ButtonViewHolder(
            ItemButtonBinding.inflate(
                inflater,
                parent,
                false
            )
        )
    }

    override fun bindViewHolder(
        model: ItemButtonBinder,
        viewHolder: ButtonViewHolder,
        payloads: List<DelegateAdapterItem.Payloadable>
    ) {
        if (payloads.isEmpty())
            viewHolder.bind(model.item)
        else
            payloads.forEach {
                when (it) {
                    is ItemButtonBinder.ButtonPayload.ButtonTextChanged -> viewHolder.setButtonText(
                        it.text
                    )
                    is ItemButtonBinder.ButtonPayload.BackgroundChanged -> viewHolder.setBackground(
                        it.buttonBackground
                    )
                    is ItemButtonBinder.ButtonPayload.TextColorChanged -> viewHolder.setTextColor(it.textColor)
                    is ItemButtonBinder.ButtonPayload.IconLeftChanged -> viewHolder.setIconLeft(it.iconLeft)
                    is ItemButtonBinder.ButtonPayload.IconCenterChanged -> viewHolder.setIconCenter(
                        it.iconRight
                    )
                    is ItemButtonBinder.ButtonPayload.EnableChanged -> viewHolder.setEnable(it.isEnable)
                }
            }
    }

    inner class ButtonViewHolder(
        private val binding: ItemButtonBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItemButton) {
            binding.button.setOnClickListener {
                action(ButtonAction.Click(item.tag))
            }
            setIconLeft(item.iconLeft)
            setIconCenter(item.iconCenter)
            setButtonText(item.text)
            setBackground(item.buttonBackground)
            setTextColor(item.textColor)
            setEnable(item.isEnable)
            binding.button.setMargin(item.margin)
        }

        fun setButtonText(text: Text) {
            binding.buttonLabel.text = text.getStringText(itemView.context)
        }

        fun setBackground(@DrawableRes buttonBackground: Int?) {
            if (buttonBackground != null) {
                binding.button.setBackgroundResource(buttonBackground)
            } else {
                binding.button.background = null
            }
        }

        fun setTextColor(textColor: ItemColor) {
            binding.buttonLabel.setTextColor(textColor, itemView.context)
        }

        fun setIconLeft(@DrawableRes iconLeft: Int?) {
            val icon = if (iconLeft != null)
                AppCompatResources.getDrawable(itemView.context, iconLeft)
            else
                null
            binding.iconLeft.setImageDrawable(icon)
        }

        fun setIconCenter(@DrawableRes iconLeft: Int?) {
            val icon = if (iconLeft != null)
                AppCompatResources.getDrawable(itemView.context, iconLeft)
            else
                null
            binding.iconCenter.setImageDrawable(icon)
        }

        fun setEnable(isEnable: Boolean){
            binding.button.isEnabled = isEnable
            binding.buttonLabel.isEnabled = isEnable
        }
    }
}

sealed class ButtonAction : Actions {
    data class Click(val tag:String) : ButtonAction()
}


