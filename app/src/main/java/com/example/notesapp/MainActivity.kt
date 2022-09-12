package com.example.notesapp

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import com.example.notesapp.databinding.ActivityMainBinding
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bnTab : BottomNavigationView
    lateinit var btAppBar : BottomAppBar
    lateinit var addFAB : FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val unCheckFragment = UncheckFragment()
        val checkFragment = CheckFragment()
        val deleteFragment = DeleteFragment()

//        val btTab = binding.bnTab
//        val iconView = btTab.getChildAt(2)
//        iconView.scaleY = 1.5f
//        iconView.scaleX = 1.5f
//        final View iconView =
//            menuView.getChildAt(2).findViewById(android.support.design.R.id.icon);
//        iconView.setScaleY(1.5f);
//        iconView.setScaleX(1.5f);
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragmentContent, unCheckFragment)
            commit()
        }
        btAppBar = binding.bottomAppBar
        bnTab = binding.bnTab

        bnTab.background = null
        bnTab.menu.getItem(2).isEnabled = false
        bnTab.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.uncheckFragment -> {
                    replaceFragmentBottomNavigation(unCheckFragment)
                    true
                }
                R.id.checkFragment -> {
                    replaceFragmentBottomNavigation(checkFragment)
                    true
                }
                else -> {
                    replaceFragmentBottomNavigation(deleteFragment)
                    true
                }
            }
        }
        addFAB = binding.fab
        addFAB.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.flFragmentContent, AddEditNoteFragment())
                .commit()
            //hideFloatingActionButton(addFAB)
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
}