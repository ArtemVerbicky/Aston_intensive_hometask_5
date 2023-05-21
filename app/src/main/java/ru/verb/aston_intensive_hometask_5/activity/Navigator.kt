package ru.verb.aston_intensive_hometask_5.activity

import androidx.fragment.app.Fragment

fun Fragment.navigator() = requireActivity() as Navigator

interface Navigator {
    fun goBack()
    fun showContactDetailsScreen(name: String, surname: String, phoneNumber: String, position: Int)
}
