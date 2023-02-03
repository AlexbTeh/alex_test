package com.test_crypto.core_ui

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding

interface ViewBindingHolder<B : ViewBinding> {
    var binding: B?

    // Only valid between onCreateView and onDestroyView.
    fun requireBinding() = checkNotNull(binding)

    fun requireBinding(lambda: (B) -> Unit) {
        binding?.let {
            lambda(it)
        }}

    /**
     * Make sure to use this with Fragment.viewLifecycleOwner
     */
    fun registerBinding(binding: B, lifecycleOwner: LifecycleOwner) {
        this.binding = binding
        lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                owner.lifecycle.removeObserver(this)
                this@ViewBindingHolder.binding = null
            }
        })
    }
}
