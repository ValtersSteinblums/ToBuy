package com.example.tobuy.ui

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.tobuy.R
import com.example.tobuy.arch.ToBuyViewModel
import com.example.tobuy.database.AppDatabase
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel: ToBuyViewModel by viewModels()
        viewModel.init(AppDatabase.getDatabase(this))

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController

        // define top-level fragments
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.homeFragment,
            R.id.profileFragment
        ))
        // setup top app bar
        setupActionBarWithNavController(navController, appBarConfiguration)
        // setup bottom nav bar
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        // add our destination change listener to show/hide the bottom nav bar
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if (appBarConfiguration.topLevelDestinations.contains(destination.id)) {
                bottomNavigationView.isVisible = true
            } else {
                bottomNavigationView.isGone = true
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun hideKeyboard(view: View) {
        val imm: InputMethodManager = application.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showKeyboard() {
        val imm: InputMethodManager = application.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }
}