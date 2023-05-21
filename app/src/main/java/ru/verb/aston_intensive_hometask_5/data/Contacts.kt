package ru.verb.aston_intensive_hometask_5.data

object Contacts {
    const val KEY_NAME = "KEY_NAME"
    const val KEY_SURNAME = "KEY_SURNAME"
    const val KEY_PHONE_NUMBER = "KEY_PHONE_NUMBER"
    const val KEY_POSITION = "KEY_POSITION"

    val contactList = mutableListOf(
        mutableMapOf(
            KEY_NAME to "Ivan",
            KEY_SURNAME to "Ivanov",
            KEY_PHONE_NUMBER to "+79991680472"
        ),
        mutableMapOf(
            KEY_NAME to "Sergei",
            KEY_SURNAME to "Sergeev",
            KEY_PHONE_NUMBER to "+79892281380"
        ),
        mutableMapOf(
            KEY_NAME to "Andrei",
            KEY_SURNAME to "Andreev",
            KEY_PHONE_NUMBER to "+79892281380"
        )
    )
}
