package com.test_crypto.core_ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.gson.Gson
import com.test_crypto.core_ui.extentions.StringResLink
import com.test_crypto.core_ui.extentions.mapApiErrorKeyToResourceString
import com.test_crypto.core_ui.mvi.*
import com.test_crypto.domain.Failure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject


abstract class BaseFragment<T : ViewBinding, STATE: UiState,  V : BaseViewModel<STATE, out UiIntent, out UiIntentResult, out Reducer<*, STATE>>>: Fragment(),
    ViewBindingHolder<T> {
    //local use
    private var _binding: T? = null

    //lifecycle aware
    override var binding: T? = _binding

    abstract val viewModel: V

    abstract fun inflate(inflater: LayoutInflater, container: ViewGroup?): T?

    abstract fun render(state : STATE)

    var navController: NavController? = null

    @Inject
    lateinit var gson: Gson

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
            viewModel.state.collectLatest { state ->
                render(state)
            }
        }
    }

    /*
      override this to provide custom implementation for body errors
     */
    protected open fun processValidationError(errors : HashMap<String, List<String>>){
        var text = ""
         errors.values.forEach {
             text += it.joinToString()
        }
        if(text.isNotEmpty())
        showError(text)
    }

  /*  fun navigateToExtraInfo(destination: NavDirections, extraInfo: FragmentNavigator.Extras) =
        with(findNavController()) {
            currentDestination?.getAction(destination.actionId)
                ?.let { navigate(destination, extraInfo) }
        }

    fun navigateToMain(@IdRes destinationIdRes: Int) {
        getNavController().navigate(destinationIdRes)
    }*/

    /*fun navigateToAnimated(navDirections: NavDirections) {
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

    */
    fun navigatePopBackStack(): Boolean {
        return navController?.popBackStack() ?: false
    }

    fun navigatePopBackStack(
        @IdRes destinationIdRes: Int,
        popInclusive: Boolean = false
    ): Boolean {
        return navController?.popBackStack(destinationIdRes, popInclusive) ?: false
    }

    fun navigateUp() {
        navController?.navigateUp()
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

    private fun showNotAvailableExpired() {
        showError("NotAvailableExpired")
    }

    private fun showNoInternetConnection() {
        showError("No Internet Connection")
    }

    fun processApiError(error: Failure) {
        when (error) {
            is Failure.NoInternetConnectionError -> {
                showNoInternetConnection()
            }
            is Failure.CertificateExpirationError -> {
                val errorMessage = error.mapApiErrorKeyToResourceString(
                    gson = gson,
                    requireActivity()
                ) ?: ""
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

                val bodyErrors = error.body?.errors
                Log.d("RequestHttpError", bodyErrors.toString())
                if (bodyErrors.isNullOrEmpty()) {
                    showExceptionDialog()
                }

                if(!bodyErrors.isNullOrEmpty()){
                    processValidationError(bodyErrors)
                }
            }

            is Failure.RequestGeneralServerError -> {
                showExceptionDialog()
            }
            is Failure.FeatureError -> {
                error.e?.let { it.message?.let { message -> showError(message = message) } }
            }
            is Failure.None -> {

            }
            is Failure.HttpError.RequestRestricted ->  showNotAvailableExpired()
            is Failure.HttpError.RequestTooManyRequests -> {}
        }
    }

    protected open fun showHttpError(error: Failure) {
        /*   val errorMessage = error.mapApiErrorKeyToResourceString(
               gson = gson,
               requireActivity()
           ) ?: ""
           showError(errorMessage)*/
    }

    open fun showExceptionDialog() {
        showError("Exception")
    }

    open fun showKeyboard() {
        val view = activity?.currentFocus
        if (view != null) {
            val imm =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    open fun hideKeyboard() {
        val view = activity?.currentFocus
        if (view != null) {
            val imm =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    open fun toggleKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive) {
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0) // hide
        } else {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY) // show
        }
    }

}