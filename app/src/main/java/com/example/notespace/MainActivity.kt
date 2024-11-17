package com.example.notespace

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.notespace.adapter.NotesAdapter
import com.example.notespace.databinding.ActivityMainBinding
import com.example.notespace.databinding.BottomMenuPopUpLayoutBinding
import com.example.notespace.databinding.BottomSheetPopUpLayoutBinding
import com.example.notespace.databinding.GalleryDialogBoxBinding
import com.example.notespace.ui.AddFragment
import com.example.notespace.viewModel.NotesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(){

    lateinit var binding: ActivityMainBinding

    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var navController : NavController
    private var dialogbox : Dialog? = null
    private lateinit var addOnDialog : BottomSheetDialog
    private lateinit var menuDialog: BottomSheetDialog

    private lateinit var cameraResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        navController = navHostFragment.navController

        setupToolbarVisibility()
        setUpMainBarVisibility()
        setSupportActionBar(binding.headerToolbar.toolbarHome)

        setUpActionBarClicks()
        setUpDrawerLayout()

        binding.headerToolbar.drawerMenuIcon.setOnClickListener {
            binding.mainDrawer.openDrawer(GravityCompat.START)
        }

        setCustomBottomIconClickEvents()
        handleFloatingButtonClick()
        handleCustomToolbarIconClicks()


        cameraResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
            if (result.resultCode == RESULT_OK) {
                val capturedImageUri = CameraHelper.getImageUri()
                if(capturedImageUri != null){
                    val bundle = Bundle().apply{
                        putParcelable("image",capturedImageUri)
                    }
                    navController.navigate(R.id.addFragment, bundle)
                }
            } else {
                Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show()
            }
        }

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                CameraHelper.handleCameraAction(this, contentResolver, cameraResultLauncher, permissionLauncher)
            } else {
                Toast.makeText(this, "Camera permission is required to take photos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupToolbarVisibility() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
           if(destination.id == R.id.editFragment){
               hideMainHeaderToolbar()
               hideMainBottomNavLayout()
               showAddEditCustomToolbar()
               showAddEditCustomBottomBar()
               hideFloatingActionButton()
           }
        }
    }

    private fun setUpMainBarVisibility(){
        showMainHeaderToolbar()
        hideAddEditCustomToolbar()
        showMainBottomNavLayout()
        hideAddEditCustomBottomBar()
        showFloatingActionButton()
    }

    private fun setUpDrawerLayout() {
        actionBarDrawerToggle = ActionBarDrawerToggle(this, binding.mainDrawer, R.string.start, R.string.close)
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

    //toolbar for main activity (base)
    private fun setUpActionBarClicks(){
        binding.headerToolbar.apply {
            searchCardView.setOnClickListener {
                navController.navigate(R.id.searchNoteFragment)
            }
            gridIcon.setOnClickListener {

            }
        }
    }

    //bottombar for main activity (base)
    private fun setCustomBottomIconClickEvents(){
        binding.bottomNavCustom.apply {
            checkboxNavIcon.setOnClickListener {
//                navController.navigate(R.id.newListFragment)
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
            navController.currentDestination?.id == R.id.editFragment -> {
                setUpMainBarVisibility()
                super.onBackPressed()
            }
            else -> {
                super.onBackPressed()
                showMainBottomNavLayout()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    //dialogbox for clicking photo
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
                CameraHelper.handleCameraAction(this@MainActivity, contentResolver, cameraResultLauncher, permissionLauncher)
                dialogbox?.dismiss()
            }
            chooseImageLayout.setOnClickListener {
                navController.navigate(R.id.dashboardFragment)
                dialogbox?.dismiss()
            }
        }
        dialogbox?.show()
    }

    //floating action button on main activity
    private fun handleFloatingButtonClick(){
        binding.floatingActionButton.setOnClickListener {
            hideMainBottomNavLayout()
            navController.navigate(R.id.addFragment)
        }
    }


    private fun handleCustomToolbarIconClicks() {
        binding.customToolbarLayout.apply {
            backArrowIcon.setOnClickListener {

            }
            pinIcon.setOnClickListener {
            }
            remindMeIcon.setOnClickListener {
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
                }
                R.id.menu_delete -> {
                    hideAddEditCustomToolbar()
                    showMainHeaderToolbar()
                }
                R.id.menu_make_copy -> {
                }
                R.id.menu_send -> {
                }
                R.id.menu_copy_to_google_docs -> {
                }
            }
            true
        }
        popupMenu.show()
    }

    private fun handleCustomBottomBar(){
        binding.addEditBottomBar.apply{
            editAddOnIcon.setOnClickListener {
                showAddOnBottomPopUpDialog()
            }
            editColorPaleteIcon.setOnClickListener {
            }
            editTextStylePickerIcon.setOnClickListener {
            }
            editMenuIcon.setOnClickListener {
                showMenuBottomPopUpDialog()
            }
        }
    }

    fun showAddOnBottomPopUpDialog(){
        val addOnViewBinding = BottomSheetPopUpLayoutBinding.inflate(LayoutInflater.from(this))
        addOnDialog = BottomSheetDialog(this)
        addOnDialog.setContentView(addOnViewBinding.root)
        addOnViewBinding.popupCheckboxesLayout.visibility = View.GONE

        addOnViewBinding.apply {
            popupTakePhotoLayout.setOnClickListener {
                addOnDialog.dismiss()
            }
            popupAddImageLayout.setOnClickListener {
                addOnDialog.dismiss()
            }
            popupDrawingLayout.setOnClickListener {
                addOnDialog.dismiss()
            }
            popupRecordingLayout.setOnClickListener {
                addOnDialog.dismiss()
            }
        }
        addOnDialog.show()
    }

    fun showMenuBottomPopUpDialog(){
        val menuViewBinding = BottomMenuPopUpLayoutBinding.inflate(LayoutInflater.from(this))
        menuDialog = BottomSheetDialog(this)
        menuDialog.setContentView(menuViewBinding.root)

        menuViewBinding.apply {
            menuDeleteLayout.setOnClickListener {
                menuDialog.dismiss()
            }
            menuMakeACopyLayout.setOnClickListener {
                menuDialog.dismiss()
            }
            menuSendLayout.setOnClickListener {
                menuDialog.dismiss()
            }
            menuCollaboratorLayout.setOnClickListener {
                menuDialog.dismiss()
            }
            menuLabelsLayout.setOnClickListener {
                menuDialog.dismiss()
            }
            menuHelpFeedbackLayout.setOnClickListener {
                menuDialog.dismiss()
            }
        }
        menuDialog.show()
    }

    fun showMainBottomNavLayout(){
        binding.bottomNavCustom.bottomNavCustomLayout.visibility = View.VISIBLE
    }
    fun hideMainBottomNavLayout(){
        binding.bottomNavCustom.bottomNavCustomLayout.visibility = View.GONE
    }

    fun showFloatingActionButton() {
         binding.floatingActionButton.visibility = View.VISIBLE
     }
    fun hideFloatingActionButton(){
        binding.floatingActionButton.visibility= View.GONE
    }

    fun hideMainHeaderToolbar() {
        binding.headerToolbar.root.visibility = View.GONE
    }
    fun showMainHeaderToolbar() {
        binding.headerToolbar.root.visibility = View.VISIBLE
    }

    fun showAddEditCustomToolbar() {
        binding.customToolbarLayout.toolbar.visibility = View.VISIBLE
    }
    fun hideAddEditCustomToolbar() {
        binding.customToolbarLayout.toolbar.visibility = View.GONE
    }

    fun showAddEditCustomBottomBar(){
        binding.addEditBottomBar.customBottomBarLayout.visibility = View.VISIBLE
    }
    fun hideAddEditCustomBottomBar(){
        binding.addEditBottomBar.customBottomBarLayout.visibility = View.GONE

    }
    fun updateNotesCount(count: Int) {
        binding.customToolbarLayout.toolbar.findViewById<TextView>(R.id.count_textview).text = count.toString()
    }


}