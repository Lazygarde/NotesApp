package com.example.notesapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.example.notesapp.constants.Keys
import com.example.notesapp.databinding.FragmentSettingBinding
import com.example.notesapp.model.User

class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding
    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(inflater)
        user = (activity as MainActivity).user

        setUpClickListeners()
        setUpUserDetail()
        setUpResultListeners()

        return binding.root
    }

    private fun setUpResultListeners() {
        setFragmentResultListener(Keys.USER_UPDATED) { _, _ ->
            setUpUserDetail()
        }
    }

    private fun setUpClickListeners() {
        binding.editBT.setOnClickListener {
            editButtonClick()
        }
    }

    private fun editButtonClick() {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.flFragmentContent, EditUserFragment::class.java, Bundle().apply {
                putSerializable(Keys.USER_INSTANCE, user)
            })
            commit()
        }
    }

    private fun setUpUserDetail() {
        binding.apply {
            yourNameTV.text = user.name
            phoneNumberTV.text = user.telephone
            mailTV.text = user.email
        }
    }

}