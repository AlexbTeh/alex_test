package com.test_crypto.core_ui

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.annotation.IdRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.test_crypto.core_ui.extentions.mapApiErrorKeyToResourceString
import com.test_crypto.core_ui.mvi.*
import com.test_crypto.domain.Failure
import javax.inject.Inject


abstract class BaseBottomSheetDialogFragment<T : ViewBinding, STATE : UiState, V : BaseViewModel<STATE, out UiIntent, out UiIntentResult, out Reducer<*, STATE>>> :
    BottomSheetDialogFragment(), ViewBindingHolder<T> {
    //local use
    private var _binding: T? = null

    //lifecycle aware
    override var binding: T? = _binding

    abstract val viewModel: V

    abstract fun inflate(inflater: LayoutInflater, container: ViewGroup?): T?

    abstract fun render(state: STATE)

    private lateinit var navController: NavController

    @Inject
    lateinit var gson: Gson

    private lateinit var behavior: BottomSheetBehavior<View>

    private lateinit var dialog: BottomSheetDialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener {
            val d = it as BottomSheetDialog
            val sheet =
                d.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            sheet?.setBackgroundColor(Color.parseColor("#00000000"))
            sheet?.background = null
            sheet?.let {
                behavior = BottomSheetBehavior.from(sheet)
            }
            sheet?.let { it1 -> setupFullHeight(it1) }
            behavior.isHideable = true
            behavior.isDraggable = true
            behavior.isFitToContents = true
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        return dialog
    }

    private fun setupFullHeight(bottomSheet: FrameLayout) {
        val layoutParams = bottomSheet.layoutParams
        val windowHeight = getWindowHeight()
        if (layoutParams != null) {
            layoutParams.height = windowHeight
        }
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun getWindowHeight(): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    override fun onResume() {
        super.onResume()
        val window = dialog?.window
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate(inflater, container)
        binding = _binding
        binding?.let {
            registerBinding(it, viewLifecycleOwner)
        }
        return requireBinding().root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            navController = findNavController()
        } catch (E: Exception) {

        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.state.collect { state ->
                render(state)
            }
        }
    }

    protected open fun showErrors(message: String) {
        showError(message)
    }

    fun getNavController(): NavController {
        return navController
    }

    fun navigateToExtraInfo(destination: NavDirections, extraInfo: FragmentNavigator.Extras) =
        with(findNavController()) {
            currentDestination?.getAction(destination.actionId)
                ?.let { navigate(destination, extraInfo) }
        }

    fun navigateToMain(@IdRes destinationIdRes: Int) {
        getNavController().navigate(destinationIdRes)
    }

    fun navigateToAnimated(navDirections: NavDirections) {
        getNavController().navigate(navDirections, getNavOptionsRightToLeft(getNavBuilder()))
    }

    fun navigateToAnimatedLeft(navDirections: NavDirections) {
        getNavController().navigate(navDirections, getNavOptionsLeftToRight(getNavBuilder()))
    }

    fun navigateToAnimatedPopBackStack(
        navDirections: NavDirections,
        @IdRes destinationIdRes: Int,
        popInclusive: Boolean = false
    ) {
        val builder = getNavBuilder()
        builder.setPopUpTo(destinationIdRes, popInclusive)
        getNavController().navigate(
            navDirections,
            getNavOptionsRightToLeft(builder)
        )
    }

    fun navigateToAnimatedTop(navDirections: NavDirections) {
        getNavController().navigate(navDirections, getNavOptionsUp(getNavBuilder()))
    }

    fun clearBackStack(navDirections: NavDirections) {
        getNavController().navigate(navDirections);
    }

    fun navigatePopBackStack(): Boolean {
        return getNavController().popBackStack()
    }

    fun navigatePopBackStack(
        @IdRes destinationIdRes: Int,
        popInclusive: Boolean = false
    ): Boolean {
        return getNavController().popBackStack(destinationIdRes, popInclusive)
    }

    fun navigateUp() {
        getNavController().navigateUp()
    }

    private fun getNavBuilder(): NavOptions.Builder {
        return NavOptions.Builder()

    }

    private fun getNavOptionsRightToLeft(builder: NavOptions.Builder): NavOptions? {
        return builder
            .setEnterAnim(R.anim.h_fragment_enter)
            .setExitAnim(R.anim.h_fragment_pop_exit)
            .setPopEnterAnim(R.anim.h_fragment_pop_enter)
            .setPopExitAnim(R.anim.h_fragment_exit)
            .build()
    }

    private fun getNavOptionsLeftToRight(builder: NavOptions.Builder): NavOptions? {
        return builder
            .setEnterAnim(R.anim.h_fragment_enter_left)
            .setExitAnim(R.anim.h_fragment_pop_exit_left)
            .setPopEnterAnim(R.anim.h_fragment_pop_enter)
            .setPopExitAnim(R.anim.h_fragment_exit)
            .build()
    }

    private fun getNavOptionsUp(builder: NavOptions.Builder): NavOptions? {
        return builder
            .setEnterAnim(R.anim.fragment_slide_in_bottom)
            .setExitAnim(R.anim.v_fragment_pop_exit)
            .setPopEnterAnim(R.anim.v_fragment_pop_enter)
            .setPopExitAnim(R.anim.fragment_slide_out_bottom)
            .build()
    }

    private fun getNavOptionsTop(): NavOptions? {
        return NavOptions.Builder()
            .build()
    }

    fun getNavigationResult(key: String = "result") =
        try {
            findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(key)
        } catch (e: java.lang.Exception) {
            MutableLiveData(key)
        }

    fun setNavigationResult(result: String?, key: String = "result") {
        try {
            findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
        } catch (e: java.lang.Exception) {
        }
    }

    fun processApiError(error: Failure) {
        when (error) {
            is Failure.NoInternetConnectionError -> {
                Log.d("getRestError", "NoInternetConnectionError" + error.toString())
            }
            is Failure.CertificateExpirationError -> {
                val errorMessage = error.mapApiErrorKeyToResourceString(
                    gson = gson,
                    requireActivity()
                )
                showError(errorMessage)

            }
            is Failure.HttpError.RequestHttpUnauthorizedError -> {
                val errorMessage = error.mapApiErrorKeyToResourceString(
                    gson = gson,
                    requireActivity()
                ) ?: ""
                showError(errorMessage)

            }
            is Failure.HttpError.RequestHttpNotFoundError -> {

            }

            is Failure.HttpError.RequestHttpError -> {
                showHttpError(error)
            }

            is Failure.RequestGeneralServerError -> {
                val errorMessage = error.mapApiErrorKeyToResourceString(
                    gson = gson,
                    requireActivity()
                ) ?: ""
                showError(errorMessage)
            }
        }
    }

    protected open fun showHttpError(error: Failure) {
        val errorMessage = error.mapApiErrorKeyToResourceString(
            gson = gson,
            requireActivity()
        ) ?: ""
        showError(errorMessage)
    }

    open fun showKeyboard() {
        val view = activity?.currentFocus
        if (view != null) {
            val imm =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}