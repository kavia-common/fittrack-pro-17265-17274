package org.example.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.example.app.data.AuthRepository
import org.example.app.notifications.ReminderScheduler
import org.example.app.ui.ActivitiesFragment
import org.example.app.ui.GoalsFragment
import org.example.app.ui.HomeFragment
import org.example.app.ui.ProfileFragment
import org.example.app.ui.WorkoutsFragment

/**
 * PUBLIC_INTERFACE
 * MainActivity
 *
 * The entry activity for the fitness tracker app. Displays a BottomNavigationView and hosts
 * the primary fragments for Home, Activities, Workouts, Goals, and Profile.
 *
 * Parameters: None
 * Returns: None. Manages the UI lifecycle and navigation.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Redirect to login if not authenticated
        if (!AuthRepository(this).isLoggedIn()) {
            startActivity(Intent(this, org.example.app.ui.LoginActivity::class.java))
            finish()
            return
        }

        setTheme(R.style.Theme_FitTrack_Light_NoActionBar)
        setContentView(R.layout.activity_main)

        bottomNav = findViewById(R.id.bottom_nav)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> switchFragment(HomeFragment(), "home")
                R.id.nav_activities -> switchFragment(ActivitiesFragment(), "activities")
                R.id.nav_workouts -> switchFragment(WorkoutsFragment(), "workouts")
                R.id.nav_goals -> switchFragment(GoalsFragment(), "goals")
                R.id.nav_profile -> switchFragment(ProfileFragment(), "profile")
                else -> false
            }
        }

        if (savedInstanceState == null) {
            bottomNav.selectedItemId = R.id.nav_home
        }

        // Schedule default reminder at 8 PM if not set
        ReminderScheduler(this).ensureDefaultReminderScheduled()
    }

    private fun switchFragment(fragment: Fragment, tag: String): Boolean {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment, tag)
            .commit()
        return true
    }

    override fun onResume() {
        super.onResume()
        // Hide bottom nav when login activity is visible - safety
        bottomNav.isVisible = true
    }
}
