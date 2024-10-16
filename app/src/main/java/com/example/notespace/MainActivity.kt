package com.example.notespace

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.notespace.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(2000)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = Navigation.findNavController(this,R.id.container)
        setUpDrawerLayout()
        binding.headerToolbar.drawerMenuIcon.setOnClickListener {
            binding.mainDrawer.openDrawer(GravityCompat.START)
        }
    }

    private fun setUpDrawerLayout() {
        setSupportActionBar(binding.headerToolbar.toolbar)
        supportActionBar?.title =""
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, binding.mainDrawer, R.string.start, R.string.close)
        binding.mainDrawer.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        binding.drawerNavView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.notes -> {
                    binding.mainDrawer.closeDrawer(GravityCompat.START)
                    navController.navigate(R.id.dashboardFragment)
                }
                R.id.reminders -> {
                    binding.mainDrawer.closeDrawer(GravityCompat.START)
                    navController.navigate(R.id.reminderFragment)
                }
                R.id.create_new_label -> {
                    binding.mainDrawer.closeDrawer(GravityCompat.START)
                    navController.navigate(R.id.createNewLabelFragment)
                }
                R.id.archive -> {
                    binding.mainDrawer.closeDrawer(GravityCompat.START)
                    navController.navigate(R.id.archiveFragment)
                }
                R.id.trash -> {
                    binding.mainDrawer.closeDrawer(GravityCompat.START)
                    navController.navigate(R.id.trashFragment)
                }
                R.id.settings -> {
                    binding.mainDrawer.closeDrawer(GravityCompat.START)
                    navController.navigate(R.id.settingsFragment)
                }
                R.id.help_feedback -> {
                    binding.mainDrawer.closeDrawer(GravityCompat.START)
                    navController.navigate(R.id.helpFeedbackFragment)
                }
            }
            true
        }

    }

    override fun onBackPressed() {
        if(binding.mainDrawer.isDrawerOpen(GravityCompat.START)){
            binding.mainDrawer.closeDrawer(GravityCompat.START)
        }else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}