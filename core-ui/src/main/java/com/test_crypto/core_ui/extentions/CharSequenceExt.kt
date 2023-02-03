package com.test_crypto.core_ui.extentions

import java.util.regex.Matcher
import java.util.regex.Pattern

fun CharSequence.hasLowerCase(): Boolean{
    val pattern: Pattern = Pattern.compile("[a-z]")
    val hasLowerCase: Matcher = pattern.matcher(this)
    return hasLowerCase.find()
}

fun CharSequence.hasUpperCase(): Boolean{
    val pattern: Pattern = Pattern.compile("[A-Z]")
    val hasUpperCase: Matcher = pattern.matcher(this)
    return hasUpperCase.find()
}

fun CharSequence.hasDigit(): Boolean{
    val pattern: Pattern = Pattern.compile("[0-9]")
    val hasDigit: Matcher = pattern.matcher(this)
    return hasDigit.find()
}

fun CharSequence.hasSpecialChar(): Boolean{
    val pattern: Pattern = Pattern.compile("[!@#$%^&*()_=+{}/.<>|\\[\\]~-]")
    val hasSpecialChar: Matcher = pattern.matcher(this)
    return hasSpecialChar.find()
}