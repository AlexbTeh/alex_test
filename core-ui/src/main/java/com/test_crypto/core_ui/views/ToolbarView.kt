package com.test_crypto.core_ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.test_crypto.core_ui.databinding.ToolbarViewBinding
import com.test_crypto.core_ui.extentions.DrawableResLink
import com.test_crypto.core_ui.styles.Image
import com.test_crypto.core_ui.styles.load

class ToolbarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    val binding  = ToolbarViewBinding.inflate(
        LayoutInflater.from(context), this,
        true
    )


    private var leftClickListener: (() -> Unit)? = null
    private var rightClickListener: (() -> Unit)? = null

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        binding.back.setOnClickListener {
            leftClickListener?.invoke()
        }

        binding.backText.setOnClickListener {
            leftClickListener?.invoke()
        }
        binding.next.setOnClickListener {
            rightClickListener?.invoke()
        }

        binding.next.isSelected = true
    }

    fun setRightText(idRes: Int, clickListener: (() -> Unit)) {
        binding.next.isVisible = true
        binding.next.text = context.getString(idRes)
        binding.next.setOnClickListener {
            clickListener.invoke()
        }
    }

    fun setRightTextBackground(idRes: Int){
        binding.next.background = ContextCompat.getDrawable(context,idRes)
    }

    fun setSelectedRightIcon(isSelected: Boolean) {
        binding.rightIcon.isSelected = isSelected
    }

    fun setLeftClickListener(onClickListener: () -> Unit) {
        this.leftClickListener = onClickListener
    }

    fun setRightClickListener(onClickListener: () -> Unit) {
        this.rightClickListener = onClickListener
    }


    fun setRightIcon(idRes: Int, clickListener: (() -> Unit)) {
        binding.rightIcon.setImageResource(idRes)
        binding.rightIcon.visibility = View.VISIBLE
        binding.rightIcon.setOnClickListener {
            clickListener.invoke()
        }
    }

    fun setTitle(text: String) {
        binding.title.text = text
    }

    fun setTitleColor(color: Int) {
        binding.title.setTextColor(color)
    }

    fun setSubTitle(text: String) {
        binding.subTitle.isVisible = true
        binding.subTitle.text = text
    }

    fun setLeftIcon(idRes: Int, clickListener: (() -> Unit)) {
        binding.back.setImageResource(idRes)
        binding.back.visibility = View.VISIBLE
        binding.back.setOnClickListener {
            clickListener.invoke()
        }
    }


    fun setLeftTitle(text: String?) {
        binding.leftTitle.isVisible = text!=null
        binding.leftTitle.text = text
    }

    fun setTitle(idRes: Int) {
        binding.title.text = context.getString(idRes)
    }

    fun showRightIcon(isShow: Boolean) {
        binding.rightIcon.visibility = when {
            isShow -> View.VISIBLE
            else -> View.GONE
        }
    }

    fun loadAvatar(url: String?) {
        showAvatar(true)
        if (url != null) {
            binding.image.load(Image.Network(url))
        } else {
            binding.image.load(Image.Resource(DrawableResLink.image_holder_bg))
        }
    }

    fun showAvatar(isShow: Boolean) {
        binding.image.isVisible = isShow
    }
}