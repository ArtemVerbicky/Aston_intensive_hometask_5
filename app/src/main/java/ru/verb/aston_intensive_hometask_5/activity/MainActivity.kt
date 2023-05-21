package ru.verb.aston_intensive_hometask_5.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import ru.verb.aston_intensive_hometask_5.R
import ru.verb.aston_intensive_hometask_5.data.Contacts
import ru.verb.aston_intensive_hometask_5.databinding.ActivityMainBinding
import ru.verb.aston_intensive_hometask_5.fragments.ContactDetailsFragment
import ru.verb.aston_intensive_hometask_5.fragments.ContactsFragment
import ru.verb.aston_intensive_hometask_5.fragments.HasAction

class MainActivity : AppCompatActivity(), Navigator {
    private lateinit var binding: ActivityMainBinding
    private val fragment: Fragment?
        get() = supportFragmentManager.findFragmentById(R.id.fragment_container)
    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(
            fm: FragmentManager,
            f: Fragment,
            v: View,
            savedInstanceState: Bundle?
        ) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            updateUi()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, ContactsFragment())
                .commit()
        }
        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
    }

    override fun goBack() {
        onBackPressedDispatcher.onBackPressed()
    }

    override fun showContactDetailsScreen(name: String, surname: String, phoneNumber: String, position: Int) {
        val contactDetailsFragment = ContactDetailsFragment().apply {
            val bundle = Bundle().apply {
                putInt(Contacts.KEY_POSITION, position)
                putString(Contacts.KEY_NAME, name)
                putString(Contacts.KEY_SURNAME, surname)
                putString(Contacts.KEY_PHONE_NUMBER, phoneNumber)
            }
            arguments = bundle
        }
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .setCustomAnimations(
                com.google.android.material.R.anim.abc_slide_in_top,
                com.google.android.material.R.anim.abc_slide_out_bottom,
                com.google.android.material.R.anim.abc_fade_in,
                com.google.android.material.R.anim.abc_fade_out
            )
            .replace(R.id.fragment_container, contactDetailsFragment)
            .commit()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun updateUi() {
        if (fragment is HasAction) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        } else {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }
    }
}

