package com.example.notespace

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.notespace.database.NotesDatabase
import com.example.notespace.databinding.ActivityMainBinding
import com.example.notespace.databinding.GalleryDialogBoxBinding
import com.example.notespace.repository.NotesRepository
import com.example.notespace.viewModel.NotesViewModel
import com.example.notespace.viewModel.NotesViewModelFactory

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var navController : NavController
    private var dialogbox : Dialog? = null

    lateinit var notesViewModel: NotesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setUpViewModel()
        navController = Navigation.findNavController(this,R.id.container)
        setUpActionBarClicks()
        setUpDrawerLayout()
        binding.headerToolbar.drawerMenuIcon.setOnClickListener {
            binding.mainDrawer.openDrawer(GravityCompat.START)
        }

        setCustomBottomIconClickEvents()
        handleFloatingButtonClick()

    }

    private fun setUpDrawerLayout() {
        setSupportActionBar(binding.headerToolbar.toolbarHome)
        supportActionBar?.title =""
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, binding.mainDrawer, R.string.start, R.string.close)
        binding.mainDrawer.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        binding.drawerNavView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.notes -> {
                    Toast.makeText(this,"notes clicked", Toast.LENGTH_LONG).show()
                    binding.mainDrawer.closeDrawer(GravityCompat.START)
                    navController.navigate(R.id.dashboardFragment)
                }
                R.id.reminders -> {
                    Toast.makeText(this,"reminders clicked", Toast.LENGTH_LONG).show()
                    binding.mainDrawer.closeDrawer(GravityCompat.START)
                    navController.navigate(R.id.reminderFragment)
                }
                R.id.create_new_label -> {
                    Toast.makeText(this,"create new label clicked", Toast.LENGTH_LONG).show()

                    binding.mainDrawer.closeDrawer(GravityCompat.START)
                    navController.navigate(R.id.createNewLabelFragment)
                }
                R.id.archive -> {
                    Toast.makeText(this,"archive clicked", Toast.LENGTH_LONG).show()

                    binding.mainDrawer.closeDrawer(GravityCompat.START)
                    navController.navigate(R.id.archiveFragment)
                }
                R.id.trash -> {
                    Toast.makeText(this,"trash clicked", Toast.LENGTH_LONG).show()

                    binding.mainDrawer.closeDrawer(GravityCompat.START)
                    navController.navigate(R.id.trashFragment)
                }
                R.id.settings -> {
                    Toast.makeText(this,"settings clicked", Toast.LENGTH_LONG).show()

                    binding.mainDrawer.closeDrawer(GravityCompat.START)
                    navController.navigate(R.id.settingsFragment)
                }
                R.id.help_feedback -> {
                    Toast.makeText(this,"help/feedback clicked", Toast.LENGTH_LONG).show()

                    binding.mainDrawer.closeDrawer(GravityCompat.START)
                    navController.navigate(R.id.helpFeedbackFragment)
                }
            }
            true
        }

    }

    private fun setUpActionBarClicks(){
        binding.headerToolbar.apply {
            searchCardView.setOnClickListener {
                navController.navigate(R.id.searchNoteFragment)
            }
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
            showBottomNavLayout()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun hideBottomNavBar(){
        binding.bottomNavFrameLayout.visibility= View.GONE
    }

    fun showBottomNavBar(){
        binding.bottomNavFrameLayout.visibility= View.VISIBLE
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

    private fun setUpViewModel(){
        val notesRepository = NotesRepository(NotesDatabase(this))
        val viewModelProviderFactory = NotesViewModelFactory(application, notesRepository)
        notesViewModel = ViewModelProvider(this, viewModelProviderFactory)[NotesViewModel::class.java]
    }

    private fun handleFloatingButtonClick(){
        binding.floatingActionButton.setOnClickListener {
            hideBottomNavLayout()
            navController.navigate(R.id.addFragment)
        }
    }

    private fun showBottomNavLayout(){
        binding.bottomNavFrameLayout.visibility = View.VISIBLE
    }

     fun showFloatingActionButton(){
        binding.floatingActionButton.visibility = View.VISIBLE
    }

     fun hideFloatingActionButton(){
        binding.floatingActionButton.visibility= View.GONE
    }

    private fun hideBottomNavLayout(){
        binding.bottomNavFrameLayout.visibility = View.GONE
    }




}