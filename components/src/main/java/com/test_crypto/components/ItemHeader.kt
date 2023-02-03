package com.test_crypto.components

import android.annotation.SuppressLint
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.test_crypto.components.databinding.ItemHeaderBinding
import com.test_crypto.core_ui.adapter.Actions
import com.test_crypto.core_ui.adapter.DelegateAdapterItem
import com.test_crypto.core_ui.adapter.DelegateBinder
import com.test_crypto.core_ui.adapter.Item
import com.test_crypto.core_ui.extentions.ColorResLink
import com.test_crypto.core_ui.extentions.FontResLink
import com.test_crypto.core_ui.styles.*


data class ItemHeader(
    var tag: String,
    var title: Text? = null,
    var description: Text? = null,
    var margin: IMargin = IMargin.None,
    val textColor: ItemColor = ItemColor.Resource(ColorResLink.black),
    @FontRes val titleFont: Int = FontResLink.mulish_semi_bold,
    val titleSize: Int = com.intuit.ssp.R.dimen._19ssp,
    val gravity: Int = Gravity.START,
    @DrawableRes var iconRight: Int? = null,
    @DrawableRes var iconEnd: Int? = null,
    val iconTint: ItemColor? = null
) : Item

data class ItemHeaderBinder(val item: ItemHeader) : DelegateAdapterItem(item) {
    override fun id(): Any = item.tag
    override fun isContentTheSame(other: Item): Boolean {
        if (other is ItemHeader) {
            return other.title == item.title
                    && other.description == item.description
                    && other.textColor == item.textColor
                    && other.titleFont == item.titleFont
                    && other.gravity == item.gravity
                    && other.iconRight == item.iconRight
                    && other.iconTint == item.iconTint
                    && other.iconEnd == item.iconEnd
        }
        return false
    }

    override fun payload(other: Item): List<Payloadable> {
        val payloads = mutableListOf<Payloadable>()
        if (other is ItemHeader) {
            payloads.apply {
                if (item.title != other.title)
                    add(HeaderPayloads.TitleChanged(other.title))
                if (item.description != other.description)
                    add(HeaderPayloads.DescriptionChanged(other.description))
                if (item.textColor != other.textColor)
                    add(HeaderPayloads.TextColorChanged(other.textColor))
                if (item.titleFont != other.titleFont)
                    add(HeaderPayloads.TitleFontChanged(other.titleFont))
                if (item.gravity != other.gravity)
                    add(HeaderPayloads.GravityChanged(other.gravity))
                if (item.iconTint != other.iconTint || item.iconRight != other.iconRight)
                    add(HeaderPayloads.IconRightChanged(other.iconRight, other.iconTint))
                if (item.iconEnd != other.iconEnd)
                    add(HeaderPayloads.IconEndChanged(other.iconEnd))
            }
        }
        return payloads
    }
}

class ItemHeaderDelegete(val onClick: (key: String) -> Unit = {}) :
    DelegateBinder<ItemHeaderBinder, ItemHeaderDelegete.ImageViewHolder>(ItemHeaderBinder::class.java) {
    override fun createViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): RecyclerView.ViewHolder {
        val view = ItemHeaderBinding.inflate(inflater, parent, false)
        return ImageViewHolder(view)
    }

    override fun bindViewHolder(
        model: ItemHeaderBinder,
        viewHolder: ImageViewHolder,
        payloads: List<DelegateAdapterItem.Payloadable>
    ) {
        if (payloads.isNullOrEmpty())
            viewHolder.bind(model.item)
        else {
            payloads.forEach {
                when (it) {
                    is HeaderPayloads.TitleChanged -> viewHolder.setTitle(it.title)
                    is HeaderPayloads.DescriptionChanged -> viewHolder.setDescription(it.description)
                    is HeaderPayloads.GravityChanged -> viewHolder.setTitleGravity(it.gravity)
                    is HeaderPayloads.IconRightChanged -> viewHolder.setRightIcon(
                        it.iconRight,
                        it.iconTint
                    )
                    is HeaderPayloads.IconEndChanged -> viewHolder.setEndIcon(it.iconEnd)
                }
            }
            viewHolder.setClicks(model.item)
        }
    }

    inner class ImageViewHolder(
        private val binding: ItemHeaderBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItemHeader) {
            setTitle(item.title)
            setDescription(item.description)
            setTitleSize(item.titleSize)
            setTitleFont(item.titleFont)
            setMargin(item.margin)
            setTextColor(item.textColor)
            setTitleGravity(item.gravity)
            setRightIcon(item.iconRight, item.iconTint)
            setEndIcon((item.iconEnd))
            setClicks(item)
        }

        @SuppressLint("ClickableViewAccessibility")
        fun setClicks(item: ItemHeader) {

            binding.container.setOnClickListener {
                onClick.invoke(item.tag)
            }

            binding.iconRight.setOnClickListener {
                action(HeaderAction.RightClick)
                it.isHovered = !it.isHovered
            }
        }

        private fun setMargin(margin: IMargin) {
            itemView.setMargin(margin)
        }

        fun setTitle(title: Text?) {
            if (title != null) {
                binding.title.text = title.getStringText(itemView.context)
            } else {
                binding.title.text = ""
            }
        }

        fun setDescription(description: Text?) {
            if (description != null) {
                binding.description.visibility = View.VISIBLE
                binding.description.text = description.getStringText(itemView.context)
            } else {
                binding.description.visibility = View.GONE
                binding.description.text = ""
            }
        }

        private fun setTitleFont(@FontRes fontRes: Int) {
            val font = ResourcesCompat.getFont(itemView.context, fontRes)
            binding.title.typeface = font
        }

        fun setTextColor(textColor: ItemColor) {
            binding.title.setTextColor(textColor, itemView.context)
        }

        private fun setTitleSize(size: Int) {
            binding.title.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                itemView.context.resources.getDimension(size)
            )
        }

        fun setTitleGravity(gravity: Int) {
            binding.title.gravity = gravity
        }

        fun setRightIcon(@DrawableRes iconRight: Int?, iconTint: ItemColor?) {
            if (iconRight != null) {
                binding.title.layoutParams = ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            } else {
                binding.title.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            }
            binding.iconRight.isVisible = iconRight != null
            iconRight?.let {
                binding.iconRight.setImageDrawable(ContextCompat.getDrawable(itemView.context, it))
            }
        }

        fun setEndIcon(@DrawableRes iconRight: Int?) {
            if (iconRight != null) {
                binding.title.layoutParams = ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            } else {
                binding.title.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            }
            binding.iconEnd.isVisible = iconRight != null
            iconRight?.let {
                binding.iconEnd.setImageDrawable(ContextCompat.getDrawable(itemView.context, it))
            }
        }
    }
}

private sealed class HeaderPayloads : DelegateAdapterItem.Payloadable {
    data class TitleChanged(val title: Text?) : HeaderPayloads()
    data class DescriptionChanged(val description: Text?) : HeaderPayloads()
    data class TextColorChanged(val textColor: ItemColor) : HeaderPayloads()
    data class TitleFontChanged(@FontRes val titleFont: Int) : HeaderPayloads()
    data class GravityChanged(val gravity: Int) : HeaderPayloads()
    data class IconRightChanged(@DrawableRes val iconRight: Int?, val iconTint: ItemColor?) : HeaderPayloads()
    data class IconEndChanged(@DrawableRes val iconEnd: Int?) : HeaderPayloads()
}

sealed class HeaderAction : Actions {
    object RightClick : HeaderAction()
}
