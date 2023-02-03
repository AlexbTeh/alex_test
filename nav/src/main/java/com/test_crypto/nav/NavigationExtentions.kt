package com.test_crypto.nav

import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions

fun NavController.horizontal(navDirections: NavDirections) {
    navigate(navDirections, getNavOptionsRightToLeft(getNavBuilder()))
}

fun NavController.horizontal(navDeepLinkRequest: NavDeepLinkRequest) {
    navigate(navDeepLinkRequest, getNavOptionsRightToLeft(getNavBuilder()))
}

fun NavController.vertical(navDirections: NavDirections) {
    navigate(navDirections, getNavOptionsUp(getNavBuilder()))
}

fun NavController.vertical(navDeepLinkRequest: NavDeepLinkRequest) {
    navigate(navDeepLinkRequest, getNavOptionsUp(getNavBuilder()))
}

fun NavController.verticalPop(
    navDirections: NavDirections, @IdRes destinationIdRes: Int,
    popInclusive: Boolean = false
) {
    val builder = getNavBuilder().setPopUpTo(destinationIdRes, popInclusive)
    navigate(navDirections, getNavOptionsUp(builder))
}

fun NavController.verticalPop(
    id: Int?, @IdRes destinationIdRes: Int,
    popInclusive: Boolean = false
) {
    val builder = getNavBuilder().setPopUpTo(destinationIdRes, popInclusive)
    id?.let {
        navigate(id, null, getNavOptionsUp(builder))
    }
}

fun NavController.vertical(id: Int?) {
    id?.let {
        navigate(id, null, getNavOptionsUp(getNavBuilder()))
    }
}

fun NavController.verticalPop(
    navDeepLinkRequest: NavDeepLinkRequest,
    @IdRes destinationIdRes: Int,
    popInclusive: Boolean = false
) {
    val builder = getNavBuilder().setPopUpTo(destinationIdRes, popInclusive)
    navigate(navDeepLinkRequest, getNavOptionsUp(builder))
}

fun NavController.horizontalPop(
    navDirections: NavDirections, @IdRes destinationIdRes: Int,
    popInclusive: Boolean = false
) {
    val builder = getNavBuilder().setPopUpTo(destinationIdRes, popInclusive)
    navigate(navDirections, getNavOptionsRightToLeft(builder))
}

fun NavController.horizontalPop(
    navDeepLinkRequest: NavDeepLinkRequest, @IdRes destinationIdRes: Int,
    popInclusive: Boolean = false
) {
    val builder = getNavBuilder().setPopUpTo(destinationIdRes, popInclusive)
    navigate(navDeepLinkRequest, getNavOptionsRightToLeft(builder))
}

fun NavController.horizontalPopReverse(
    navDirections: NavDirections, @IdRes destinationIdRes: Int,
    popInclusive: Boolean = false
) {
    val builder = getNavBuilder().setPopUpTo(destinationIdRes, popInclusive)
    navigate(navDirections, getHorizontalAnimationReverse(builder))
}

fun NavController.horizontalPopReverse(
    navDeepLinkRequest: NavDeepLinkRequest, @IdRes destinationIdRes: Int,
    popInclusive: Boolean = false
) {
    val builder = getNavBuilder().setPopUpTo(destinationIdRes, popInclusive)
    navigate(navDeepLinkRequest, getHorizontalAnimationReverse(builder))
}

fun NavController.navigateToAnimatedLeft(navDirections: NavDirections) {
    navigate(navDirections, getNavOptionsLeftToRight(getNavBuilder()))
}

fun NavController.navigate(
    navDeepLinkRequest: NavDeepLinkRequest, @IdRes destinationIdRes: Int,
    popInclusive: Boolean = false
) {
    val builder = getNavBuilder().setPopUpTo(destinationIdRes, popInclusive)
    navigate(navDeepLinkRequest, builder.build())
}

fun NavController.navigatePop(
    directions: NavDirections, @IdRes destinationIdRes: Int,
    popInclusive: Boolean = false
) {
    val builder = getNavBuilder().setPopUpTo(destinationIdRes, popInclusive)
    navigate(directions, builder.build())
}

fun NavController.navigateSingleTop(
    navDeepLinkRequest: NavDeepLinkRequest, @IdRes destinationIdRes: Int,
    popInclusive: Boolean = false
) {
    val builder = getNavBuilder().setPopUpTo(destinationIdRes, popInclusive).setLaunchSingleTop(true)
    navigate(navDeepLinkRequest, builder.build())
}

fun NavController.navigateSingleTop(
    navDeepLinkRequest: NavDeepLinkRequest,
) {
    val builder = getNavBuilder().setLaunchSingleTop(true)
    navigate(navDeepLinkRequest, builder.build())
}


private fun getNavBuilder(): NavOptions.Builder {
    return NavOptions.Builder()

}

private fun getNavOptionsRightToLeft(builder: NavOptions.Builder): NavOptions {
    return builder
        .setEnterAnim(R.anim.h_fragment_enter)
        .setExitAnim(R.anim.h_fragment_pop_exit)
        .setPopEnterAnim(R.anim.h_fragment_pop_enter)
        .setPopExitAnim(R.anim.h_fragment_exit)
        .build()
}

private fun getHorizontalAnimationReverse(builder: NavOptions.Builder): NavOptions {
    return builder
        .setEnterAnim(R.anim.h_fragment_pop_enter)
        .setExitAnim(R.anim.h_fragment_exit)
        .setPopEnterAnim(R.anim.h_fragment_enter)
        .setPopExitAnim(R.anim.h_fragment_pop_exit)
        .build()
}


private fun getNavOptionsLeftToRight(builder: NavOptions.Builder): NavOptions {
    return builder
        .setEnterAnim(R.anim.h_fragment_enter_left)
        .setExitAnim(R.anim.h_fragment_pop_exit_left)
        .setPopEnterAnim(R.anim.h_fragment_pop_enter)
        .setPopExitAnim(R.anim.h_fragment_exit)
        .build()
}

private fun getNavOptionsUp(builder: NavOptions.Builder): NavOptions {
    return builder
        .setEnterAnim(R.anim.fragment_slide_in_bottom)
        .setExitAnim(R.anim.v_fragment_pop_exit)
        .setPopEnterAnim(R.anim.v_fragment_pop_enter)
        .setPopExitAnim(R.anim.fragment_slide_out_bottom)
        .build()
}

