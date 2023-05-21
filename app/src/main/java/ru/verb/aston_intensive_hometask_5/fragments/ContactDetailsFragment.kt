package ru.verb.aston_intensive_hometask_5.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import ru.verb.aston_intensive_hometask_5.activity.navigator
import ru.verb.aston_intensive_hometask_5.data.Contacts
import ru.verb.aston_intensive_hometask_5.databinding.FragmentContactDetailsBinding
import java.io.File

class ContactDetailsFragment : Fragment(), HasAction {
    private lateinit var binding: FragmentContactDetailsBinding
    private lateinit var imageUri: Uri
    private val activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            data?.let { intent ->
                intent.data?.let { uri ->
                    imageUri = uri
                }
                binding.avatar.setImageURI(imageUri)
            }
        }
    }

    companion object {
        const val CONTACT_DETAILS_RESULT = "CONTACT_DETAILS_RESULT"
        const val EDITED_NAME = "EDITED_NAME"
        const val EDITED_SURNAME = "EDITED_SURNAME"
        const val EDITED_PHONE_NUMBER = "EDITED_PHONE_NUMBER"
        const val EDITED_POSITION = "EDITED_POSITION"
        const val EDITED_AVATAR = "EDITED_AVATAR"
        const val KEY_AVATAR = "KEY_AVATAR"
    }

    @RequiresApi(33)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageUri = if (savedInstanceState != null) {
            savedInstanceState.getParcelable(KEY_AVATAR, Uri::class.java) ?: throw Exception("Empty URI")
        } else {
            Uri.fromFile(File("app/src/main/res/drawable/ic_baseline_person_24.xml"))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactDetailsBinding.inflate(inflater, container, false)

        val name = requireArguments().getString(Contacts.KEY_NAME) ?: "John"
        val surname = requireArguments().getString(Contacts.KEY_SURNAME) ?: "Doe"
        val phoneNumber = requireArguments().getString(Contacts.KEY_PHONE_NUMBER) ?: "+7900000000"
        val position = requireArguments().getInt(Contacts.KEY_POSITION)

        with(binding) {
            nameTextEdit.setText(name)
            surnameTextEdit.setText(surname)
            phoneNumberTextEdit.setText(phoneNumber)

            avatar.setOnClickListener {
                chooseImage()
            }
            buttonApplyChanges.setOnClickListener {
                parentFragmentManager.setFragmentResult(CONTACT_DETAILS_RESULT, bundleOf(
                    EDITED_NAME to nameTextEdit.text.toString(),
                    EDITED_SURNAME to surnameTextEdit.text.toString(),
                    EDITED_PHONE_NUMBER to phoneNumberTextEdit.text.toString(),
                    EDITED_POSITION to position,
                    EDITED_AVATAR to imageUri
                ))
                navigator().goBack()
                KeyboardService.hideKeyboard(requireView())
            }
        }
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (imageUri != null) {
            outState.putParcelable(KEY_AVATAR, imageUri)
        }
        super.onSaveInstanceState(outState)
    }

    private fun chooseImage() {
        Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT

            activityResultLauncher.launch(this)
        }
    }
}