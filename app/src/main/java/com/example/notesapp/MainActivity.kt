package com.example.notesapp

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.notesapp.databinding.ActivityMainBinding
import com.example.notesapp.fragment.CalendarFragment
import com.example.notesapp.fragment.DeleteFragment
import com.example.notesapp.fragment.NotesFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var bnTab: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        bnTab = binding.bnTab
        transaction(NotesFragment())
        setUpButtonNav()
        setContentView(binding.root)
    }

    private fun setUpButtonNav() {
        bnTab.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.uncheckFragment -> {
                    transaction(NotesFragment())
                    true
                }
                R.id.deleteFragment -> {
                    transaction(DeleteFragment())
                    true
                }
                else -> {
                    transaction(CalendarFragment())
                    true
                }
            }
        }
    }

    private fun transaction(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragmentContent, fragment)
            addToBackStack(null)
            commit()
        }
    }


    fun hideKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

}