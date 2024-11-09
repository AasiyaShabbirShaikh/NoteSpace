package com.example.notespace

import android.app.Dialog
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.notespace.adapter.NotesAdapter
import com.example.notespace.databinding.ActivityMainBinding
import com.example.notespace.databinding.AddReminderDialogBoxBinding
import com.example.notespace.databinding.GalleryDialogBoxBinding
import com.example.notespace.viewModel.NotesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NoteInterface{

    lateinit var binding: ActivityMainBinding

    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var navController : NavController
    private var dialogbox : Dialog? = null
    private lateinit var notesAdapter : NotesAdapter

    private val notesViewModel: NotesViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.container)
        setSupportActionBar(binding.headerToolbar.toolbarHome)

        setUpActionBarClicks()
        setUpDrawerLayout()

        binding.headerToolbar.drawerMenuIcon.setOnClickListener {
            binding.mainDrawer.openDrawer(GravityCompat.START)
        }

        setCustomBottomIconClickEvents()
        handleFloatingButtonClick()
        handleCustomToolbarIconClicks()

        binding.customToolbarLayout.toolbar.visibility = View.GONE
    }

    private fun setUpDrawerLayout() {
        actionBarDrawerToggle = ActionBarDrawerToggle(this, binding.mainDrawer, R.string.start, R.string.close)
        binding.mainDrawer.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        binding.drawerNavView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.notes -> {
//                    Toast.makeText(this,"notes clicked", Toast.LENGTH_LONG).show()
                    binding.mainDrawer.closeDrawer(GravityCompat.START)
                    navController.navigate(R.id.dashboardFragment)
                }
                R.id.reminders -> {
//                    Toast.makeText(this,"reminders clicked", Toast.LENGTH_LONG).show()
                    binding.mainDrawer.closeDrawer(GravityCompat.START)
                    navController.navigate(R.id.reminderFragment)
                }
                R.id.create_new_label -> {
//                    Toast.makeText(this,"create new label clicked", Toast.LENGTH_LONG).show()
                    binding.mainDrawer.closeDrawer(GravityCompat.START)
                    navController.navigate(R.id.createNewLabelFragment)
                }
                R.id.archive -> {
//                    Toast.makeText(this,"archive clicked", Toast.LENGTH_LONG).show()
                    binding.mainDrawer.closeDrawer(GravityCompat.START)
                    navController.navigate(R.id.archiveFragment)
                }
                R.id.trash -> {
//                    Toast.makeText(this,"trash clicked", Toast.LENGTH_LONG).show()
                    binding.mainDrawer.closeDrawer(GravityCompat.START)
                    navController.navigate(R.id.trashFragment)
                }
                R.id.settings -> {
//                    Toast.makeText(this,"settings clicked", Toast.LENGTH_LONG).show()
                    binding.mainDrawer.closeDrawer(GravityCompat.START)
                    navController.navigate(R.id.settingsFragment)
                }
                R.id.help_feedback -> {
//                    Toast.makeText(this,"help/feedback clicked", Toast.LENGTH_LONG).show()
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
            gridIcon.setOnClickListener {

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
        when{
            binding.mainDrawer.isDrawerOpen(GravityCompat.START) -> {
                binding.mainDrawer.closeDrawer(GravityCompat.START)
            }
            binding.customToolbarLayout.toolbar.visibility == View.VISIBLE -> {
                notesAdapter.clearNoteSelection()  // Clear selection
                hideCustomToolbar()
                showHeaderToolbar()
            }
            else -> {
                super.onBackPressed()
                showBottomNavLayout()
            }
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

        dialogbox?.apply {
            setContentView(dialogBinding.root)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            window?.setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setCancelable(true)
        }

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


    private fun handleFloatingButtonClick(){
        binding.floatingActionButton.setOnClickListener {
            hideBottomNavLayout()
            navController.navigate(R.id.addFragment)
        }
    }

    private fun handleCustomToolbarIconClicks() {
        binding.customToolbarLayout.apply {
            backArrowIcon.setOnClickListener {
                onBackPressed()
            }
            pinIcon.setOnClickListener {

            }
            remindMeIcon.setOnClickListener {
                showAddReminderDialogBox()
            }
            colorIcon.setOnClickListener {

            }
            labelsIcon.setOnClickListener {

            }
            menuDotsIcon.setOnClickListener {
                showCustomMenuOptions()
            }
        }
    }

    private fun showCustomMenuOptions(){
        val popupMenu = PopupMenu(this@MainActivity, binding.customToolbarLayout.menuDotsIcon)
        popupMenu.menuInflater.inflate(R.menu.on_select_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            val dashboardFragment = supportFragmentManager.findFragmentById(R.id.container) as? NoteInterface

            when(menuItem.itemId){
                R.id.menu_archive -> {
                    Toast.makeText(this@MainActivity, "archive clicked", Toast.LENGTH_SHORT).show()
                }
                R.id.menu_delete -> {
                    dashboardFragment?.deleteSelectedNotes()
                    hideCustomToolbar()
                    showHeaderToolbar()
                }
                R.id.menu_make_copy -> {
                    Toast.makeText(this@MainActivity, "makeACopy clicked", Toast.LENGTH_SHORT).show()
                }
                R.id.menu_send -> {
                    Toast.makeText(this@MainActivity, "Send clicked", Toast.LENGTH_SHORT).show()
                }
                R.id.menu_copy_to_google_docs -> {
                    Toast.makeText(this@MainActivity, "CopyToGoogleDocs clicked", Toast.LENGTH_SHORT).show()
                }
            }
            true

        }
        popupMenu.show()
    }

    private fun showAddReminderDialogBox(){
        val addReminderBinding = AddReminderDialogBoxBinding.inflate(layoutInflater)

        dialogbox = Dialog(this@MainActivity)

        dialogbox?.apply {
            setContentView(addReminderBinding.root)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            window?.setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setCancelable(true)
        }

        addReminderBinding.apply {
            timeTabText.setOnClickListener {
//                Toast.makeText(this@MainActivity, "datelayout clicked",Toast.LENGTH_SHORT).show()
                dialogbox?.dismiss()
            }
            placeTabText.setOnClickListener {
//                Toast.makeText(this@MainActivity, "datelayout clicked",Toast.LENGTH_SHORT).show()
                timeTabDisplayLayout.visibility = View.GONE
                placeTabDisplayLayout.visibility = View.VISIBLE
                dialogbox?.dismiss()
            }
            dateLayout.setOnClickListener {
//                Toast.makeText(this@MainActivity, "timeLayout clicked",Toast.LENGTH_SHORT).show()
                dialogbox?.dismiss()
            }
            doesNotRepeatLayout.setOnClickListener {
//                Toast.makeText(this@MainActivity, "doesNotRepeat clicked",Toast.LENGTH_SHORT).show()
                dialogbox?.dismiss()
            }
            addReminderCancelTextButton.setOnClickListener {
//                Toast.makeText(this@MainActivity, "cancel clicked",Toast.LENGTH_SHORT).show()
                dialogbox?.dismiss()
            }
            saveButton.setOnClickListener {
//                Toast.makeText(this@MainActivity, "save clicked",Toast.LENGTH_SHORT).show()
                dialogbox?.dismiss()
            }

        }
        dialogbox?.show()
    }

    private fun showBottomNavLayout(){
        binding.bottomNavFrameLayout.visibility = View.VISIBLE
    }

     fun showFloatingActionButton(){
        binding.floatingActionButton.visibility = View.VISIBLE
    }

    fun hideHeaderToolbar() {
        binding.headerToolbar.root.visibility = View.GONE
    }

    fun showHeaderToolbar() {
        binding.headerToolbar.root.visibility = View.VISIBLE
    }

     fun hideFloatingActionButton(){
        binding.floatingActionButton.visibility= View.GONE
    }

    private fun hideBottomNavLayout(){
        binding.bottomNavFrameLayout.visibility = View.GONE
    }

    fun showCustomToolbar() {
        binding.customToolbarLayout.toolbar.visibility = View.VISIBLE
    }

    fun hideCustomToolbar() {
        binding.customToolbarLayout.toolbar.visibility = View.GONE
    }

    fun updateNotesCount(count: Int) {
        binding.customToolbarLayout.toolbar.findViewById<TextView>(R.id.count_textview).text = count.toString()
    }

    fun hideBottomNavBar(){
        binding.bottomNavFrameLayout.visibility= View.GONE
    }

    fun showBottomNavBar(){
        binding.bottomNavFrameLayout.visibility= View.VISIBLE
    }

    override fun getSelectedNoteIds(): List<Long> {
        return notesAdapter.getSelectedNoteIds()
    }

    override fun deleteSelectedNotes() {
        val selectedIds = getSelectedNoteIds()
        if (selectedIds.isNotEmpty()) {
            notesViewModel.moveToTrash(selectedIds)
            notesAdapter.deleteSelectedNote()
        }
    }

}