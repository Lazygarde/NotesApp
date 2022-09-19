package com.example.notesapp

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import com.example.notesapp.databinding.ActivityMainBinding
import com.example.notesapp.model.User
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bnTab: BottomNavigationView
    lateinit var btAppBar: BottomAppBar
    lateinit var addFAB: FloatingActionButton
    private var _user = User("Your name", "Phone number", "Email")
    val user get() = _user

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        addFAB = binding.fab
        btAppBar = binding.bottomAppBar
        bnTab = binding.bnTab

        transaction(UncheckFragment())

        btAppBar.visibility = View.VISIBLE
        showFloatingActionButton(addFAB)

        setUpButtonNav()
        addFAB.setOnClickListener {
            transaction(AddEditNoteFragment())
        }
        setContentView(binding.root)
    }

    private fun setUpButtonNav() {
        bnTab.background = null
        bnTab.menu.getItem(2).isEnabled = false
        bnTab.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.uncheckFragment -> {
                    replaceFragmentBottomNavigation(UncheckFragment())
                    true
                }
                R.id.checkFragment -> {
                    replaceFragmentBottomNavigation(CheckFragment())
                    true
                }
                R.id.deleteFragment -> {
                    replaceFragmentBottomNavigation(DeleteFragment())
                    true
                }
                else -> {
                    replaceFragmentBottomNavigation(SettingFragment())
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

    private fun replaceFragmentBottomNavigation(tabFragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragmentContent, tabFragment)
            addToBackStack(null)
            commit()
        }
    }

    fun hideFloatingActionButton(fab: FloatingActionButton) {
        val params = fab.layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior as FloatingActionButton.Behavior?
        if (behavior != null) {
            behavior.isAutoHideEnabled = false
        }
        fab.hide()
    }

    fun showFloatingActionButton(fab: FloatingActionButton) {
        fab.show()
        val params = fab.layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior as FloatingActionButton.Behavior?
        if (behavior != null) {
            behavior.isAutoHideEnabled = true
        }
    }

    fun hideKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun updateUser(newUser: User) {
        _user = newUser
    }
}