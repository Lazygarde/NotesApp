package com.example.notesapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
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

        askActivityToUpdateUser(username, telephone, email)
        setFragmentResult(Keys.USER_UPDATED, bundleOf())
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.flFragmentContent, SettingFragment())
            commit()
        }
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