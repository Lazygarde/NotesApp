package com.example.notesapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.example.notesapp.MainActivity
import com.example.notesapp.R
import com.example.notesapp.constants.Keys
import com.example.notesapp.databinding.FragmentEditProfileBinding
import com.example.notesapp.model.User

class EditUserFragment : Fragment() {
    private lateinit var binding: FragmentEditProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(inflater)

        setUpClickListeners()
        setUpUserDetail()

        return binding.root
    }

    private fun setUpClickListeners() {
        binding.saveBT.setOnClickListener {
            saveButtonClick()
        }
    }

    private fun saveButtonClick() {
        val username = binding.yourNameEDT.text.toString()
        val telephone = binding.phoneNumberEDT.text.toString()
        val email = binding.mailEDT.text.toString()

        if (username.isEmpty()) {
            Toast.makeText(context, "Your name must not be empty", Toast.LENGTH_SHORT).show()
        } else if (telephone.isEmpty()) {
            Toast.makeText(context, "Phone number must not be empty", Toast.LENGTH_SHORT).show()
        } else if (email.isEmpty()) {
            Toast.makeText(context, "Email must not be empty", Toast.LENGTH_SHORT).show()

        } else if (!isPhoneNumberValid(telephone)) {
            Toast.makeText(
                context,
                "You did not enter your phone number in the right way!",
                Toast.LENGTH_LONG
            ).show()
        } else if (!isEmailValid(email)) {
            Toast.makeText(context, "You did not enter email in the right way!", Toast.LENGTH_LONG)
                .show()
        } else {
            Toast.makeText(context, "Saved!", Toast.LENGTH_SHORT).show()
            askActivityToUpdateUser(username, telephone, email)
            setFragmentResult(Keys.USER_UPDATED, bundleOf())
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.flFragmentContent, SettingFragment())
                commit()
            }
        }
    }

    private fun isPhoneNumberValid(phoneNumber: String): Boolean {
        for (i in phoneNumber) {
            if (i < '0' || i > '9') return false
        }
        return true
    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun setUpUserDetail() {
        val user = arguments?.getSerializable(Keys.USER_INSTANCE) as User
        binding.apply {
            if (yourNameEDT.hint.toString() != user.name) {
                yourNameEDT.setText(user.name)
                phoneNumberEDT.setText(user.telephone)
                mailEDT.setText(user.email)
            }
        }
    }

    private fun askActivityToUpdateUser(username: String, telephone: String, email: String) {
        (activity as MainActivity).updateUser(
            User(username, telephone, email)
        )
    }
}