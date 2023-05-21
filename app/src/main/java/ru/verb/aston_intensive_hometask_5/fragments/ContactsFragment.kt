package ru.verb.aston_intensive_hometask_5.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.SimpleAdapter
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import ru.verb.aston_intensive_hometask_5.R
import ru.verb.aston_intensive_hometask_5.activity.navigator
import ru.verb.aston_intensive_hometask_5.data.Contacts
import ru.verb.aston_intensive_hometask_5.databinding.FragmentContactsBinding
import ru.verb.aston_intensive_hometask_5.fragments.ContactDetailsFragment.Companion.CONTACT_DETAILS_RESULT
import ru.verb.aston_intensive_hometask_5.fragments.ContactDetailsFragment.Companion.EDITED_AVATAR
import ru.verb.aston_intensive_hometask_5.fragments.ContactDetailsFragment.Companion.EDITED_NAME
import ru.verb.aston_intensive_hometask_5.fragments.ContactDetailsFragment.Companion.EDITED_PHONE_NUMBER
import ru.verb.aston_intensive_hometask_5.fragments.ContactDetailsFragment.Companion.EDITED_POSITION
import ru.verb.aston_intensive_hometask_5.fragments.ContactDetailsFragment.Companion.EDITED_SURNAME

class ContactsFragment : Fragment() {
    private lateinit var binding: FragmentContactsBinding
    private var resultPosition: Int = 0
    private lateinit var resultName: String
    private lateinit var resultSurname: String
    private lateinit var resultPhoneNumber: String
    private  var resultUri: Uri = Uri.parse("app/src/main/res/drawable/ic_baseline_person_24.xml")

    companion object {
        const val KEY_RESULT_URI = "KEY_RESULT_URI"
    }

    @RequiresApi(33)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentContactsBinding.inflate(requireActivity().layoutInflater)
        if (savedInstanceState != null) {
            resultPosition = savedInstanceState.getInt(Contacts.KEY_POSITION)
            resultName = savedInstanceState.getString(Contacts.KEY_NAME, "John")
            resultSurname = savedInstanceState.getString(Contacts.KEY_SURNAME, "Doe")
            resultPhoneNumber = savedInstanceState.getString(Contacts.KEY_PHONE_NUMBER, "+7900000000")
            resultUri = savedInstanceState.getParcelable(KEY_RESULT_URI, Uri::class.java) ?: throw Exception("Empty URI")

            updateData(resultName, resultSurname, resultPhoneNumber, resultPosition)
            updateImage(resultUri)
        } else {
            resultName = Contacts.contactList[resultPosition][Contacts.KEY_NAME] ?: "John"
            resultSurname = Contacts.contactList[resultPosition][Contacts.KEY_SURNAME] ?: "Doe"
            resultPhoneNumber = Contacts.contactList[resultPosition][Contacts.KEY_PHONE_NUMBER] ?: "+7900000000"
        }
    }

    @RequiresApi(33)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        parentFragmentManager.setFragmentResultListener(CONTACT_DETAILS_RESULT, viewLifecycleOwner, FragmentResultListener { requestKey, result ->
            resultPosition = result.getInt(EDITED_POSITION)
            resultName = result.getString(EDITED_NAME) ?: "John"
            resultSurname = result.getString(EDITED_SURNAME) ?: "Doe"
            resultPhoneNumber = result.getString(EDITED_PHONE_NUMBER) ?: "+7900000000"
            resultUri = result.getParcelable(EDITED_AVATAR, Uri::class.java) ?: throw Exception("Empty URI")

            updateData(resultName, resultSurname, resultPhoneNumber, resultPosition)
            updateImage(resultUri)
        })

        val adapter = SimpleAdapter(
            requireContext(),
            Contacts.contactList,
            R.layout.card_contact,
            arrayOf(Contacts.KEY_NAME, Contacts.KEY_SURNAME, Contacts.KEY_PHONE_NUMBER),
            intArrayOf(R.id.contact_name, R.id.contact_surname, R.id.contact_phone_number)
        )
        binding.contactsList.adapter = adapter
        binding.contactsList.onItemClickListener = provideItemClickListener()

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.apply {
            putInt(Contacts.KEY_POSITION, resultPosition)
            putString(Contacts.KEY_NAME, resultName)
            putString(Contacts.KEY_SURNAME, resultSurname)
            putString(Contacts.KEY_PHONE_NUMBER, resultPhoneNumber)
            putParcelable(KEY_RESULT_URI, resultUri)
        }
        super.onSaveInstanceState(outState)
    }

    private fun provideItemClickListener() = AdapterView.OnItemClickListener { parent, view, position, id ->
        val selectedName = Contacts.contactList[position][Contacts.KEY_NAME] ?: "John"
        val selectedSurname = Contacts.contactList[position][Contacts.KEY_SURNAME] ?: "Doe"
        val selectedPhoneNumber = Contacts.contactList[position][Contacts.KEY_PHONE_NUMBER] ?: "+7000000000"

        navigator().showContactDetailsScreen(selectedName, selectedSurname, selectedPhoneNumber, position)
    }

    private fun updateData(name: String, surname: String, phoneNumber: String, position: Int) {
        Contacts.contactList[position][Contacts.KEY_NAME] = name
        Contacts.contactList[position][Contacts.KEY_SURNAME] = surname
        Contacts.contactList[position][Contacts.KEY_PHONE_NUMBER] = phoneNumber
    }

    private fun updateImage(uri: Uri?) {
        uri?.let {
            binding.avatar.setImageURI(it)
        }
    }

}