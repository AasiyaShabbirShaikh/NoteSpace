package com.example.notespace

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.notespace.databinding.ActivityMainBinding
import com.example.notespace.databinding.GalleryDialogBoxBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var navController : NavController
    private var dialogbox : Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        Thread.sleep(2000)
        val splashScreen = installSplashScreen()
//        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = Navigation.findNavController(this,R.id.container)

        setUpDrawerLayout()
        binding.headerToolbar.drawerMenuIcon.setOnClickListener {
            binding.mainDrawer.openDrawer(GravityCompat.START)
        }

        setCustomBottomIconClickEvents()

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
                    Toast.makeText(this,"Notes Fragment",Toast.LENGTH_LONG).show()
                    binding.mainDrawer.closeDrawer(GravityCompat.START)
                    navController.navigate(R.id.dashboardFragment)
                }
                R.id.reminders -> {
                    Toast.makeText(this,"Reminders Fragment",Toast.LENGTH_LONG).show()

                    binding.mainDrawer.closeDrawer(GravityCompat.START)
                    navController.navigate(R.id.reminderFragment)
                }
                R.id.create_new_label -> {
                    Toast.makeText(this,"CreateNewLabel Fragment",Toast.LENGTH_LONG).show()

                    binding.mainDrawer.closeDrawer(GravityCompat.START)
                    navController.navigate(R.id.createNewLabelFragment)
                }
                R.id.archive -> {
                    Toast.makeText(this,"Archive Fragment",Toast.LENGTH_LONG).show()

                    binding.mainDrawer.closeDrawer(GravityCompat.START)
                    navController.navigate(R.id.archiveFragment)
                }
                R.id.trash -> {
                    Toast.makeText(this,"Trash Fragment",Toast.LENGTH_LONG).show()

                    binding.mainDrawer.closeDrawer(GravityCompat.START)
                    navController.navigate(R.id.trashFragment)
                }
                R.id.settings -> {
                    Toast.makeText(this,"Settings Fragment",Toast.LENGTH_LONG).show()

                    binding.mainDrawer.closeDrawer(GravityCompat.START)
                    navController.navigate(R.id.settingsFragment)
                }
                R.id.help_feedback -> {
                    Toast.makeText(this,"HelpFeedback Fragment",Toast.LENGTH_LONG).show()

                    binding.mainDrawer.closeDrawer(GravityCompat.START)
                    navController.navigate(R.id.helpFeedbackFragment)
                }
            }
            true
        }

    }


    private fun setCustomBottomIconClickEvents(){
        binding.bottomNavCustom.apply {
            checkboxNavIcon.setOnClickListener {
                navController.navigate(R.id.newListFragment)
            }
            paintNavIcon.setOnClickListener {

            }
            mikeNavIcon.setOnClickListener {

            }
            galleryNavIcon.setOnClickListener {
                showGalleryDialogBox()
            }

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

    private fun showGalleryDialogBox(){
        val dialogBinding = GalleryDialogBoxBinding.inflate(layoutInflater)
        dialogbox = Dialog(this@MainActivity)
        dialogbox?.setContentView(dialogBinding.root)
        dialogbox?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialogbox?.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialogbox?.setCancelable(true)

        dialogBinding.apply {
            takePhotoLayout.setOnClickListener {
                navController.navigate(R.id.dashboardFragment)
                dialogbox?.dismiss()
            }
            chooseImageLayout.setOnClickListener {
                navController.navigate(R.id.dashboardFragment)
                dialogbox?.dismiss()
            }
        }
        dialogbox?.show()
    }

}