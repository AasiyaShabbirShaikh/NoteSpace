package com.example.notespace.ui

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.notespace.CameraHelper
import com.example.notespace.MainActivity
import com.example.notespace.R
import com.example.notespace.adapter.ImagesAdapter
import com.example.notespace.databinding.BottomMenuPopUpLayoutBinding
import com.example.notespace.databinding.BottomRemindPopUpLayoutBinding
import com.example.notespace.databinding.BottomSheetPopUpLayoutBinding
import com.example.notespace.databinding.FragmentAddBinding
import com.example.notespace.databinding.FragmentDashboardBinding
import com.example.notespace.model.Notes
import com.example.notespace.viewModel.NotesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddFragment: Fragment() {

    private var _binding : FragmentAddBinding? = null
    private val binding get() = _binding!!

    private lateinit var addOnDialog: BottomSheetDialog
    private lateinit var menuDialog: BottomSheetDialog
    private lateinit var remindMeDialog: BottomSheetDialog

    private val notesViewModel: NotesViewModel by viewModels()

    private val imageUris = mutableListOf<Uri>()
    private val imagesAdapter by lazy { ImagesAdapter(imageUris) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddBinding.inflate(layoutInflater)
        return(binding.root)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
        (activity as MainActivity).hideFloatingActionButton()

        setUpActionBar()
        setUpBottomNavBar()


//        binding.imagesRecyclerview.apply {
//            layoutManager = GridLayoutManager(context, 3)
//            adapter = imagesAdapter
//        }

//        arguments?.getParcelable<Uri>("image")?.let { addImageUri(it) }
    }

    fun setUpActionBar() {
        binding.addToolbar.apply {
            backArrowIcon.setOnClickListener {
                requireActivity().onBackPressed()
            }
            pinIcon.setOnClickListener {
//                Toast.makeText(requireContext(), "Pinned Clicked", Toast.LENGTH_LONG).show()
            }
            remindMeIcon.setOnClickListener {
//                Toast.makeText(requireContext(), "Remind Clicked", Toast.LENGTH_LONG).show()
                showRemindMeBottomPopUpDialog()
            }
            archiveIcon.setOnClickListener {
//                Toast.makeText(requireContext(), "Archive Clicked", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun setUpBottomNavBar() {
        binding.apply {
            addAddOnIcon.setOnClickListener {
//                Toast.makeText(requireContext(), "add on Clicked", Toast.LENGTH_SHORT).show()
                showAddOnBottomPopUpDialog()
            }
            addColorPaleteIcon.setOnClickListener {
//                Toast.makeText(requireContext(), "color palete Clicked", Toast.LENGTH_SHORT).show()

            }
            addTextStylePickerIcon.setOnClickListener {
//                Toast.makeText(requireContext(), "text style Clicked", Toast.LENGTH_SHORT).show()

            }
            addMenuIcon.setOnClickListener {
//                Toast.makeText(requireContext(), "menu Clicked", Toast.LENGTH_SHORT).show()
                showMenuBottomPopUpDialog()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        saveNote()
    }

    private fun saveNote() {
        val title = binding.addTitleEditText.text.toString()
        val description = binding.addDescriptionEditText.text.toString()

        val note = Notes(0, title, description, System.currentTimeMillis(),false, imageUris.map { it.toString() })

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                println("${note}")
                val currentId = notesViewModel.addNote(note)

                if (title.isEmpty() && description.isEmpty() && imageUris.isEmpty()) {
                    delay(3000)
                    if (isAdded) {
                        notesViewModel.deleteNoteById(currentId)
//                        Toast.makeText(requireContext(), getString(R.string.note_discarded), Toast.LENGTH_LONG).show()
                    }
                } else {
                    findNavController().navigate(R.id.action_addFragment_to_dashboardFragment)
                }
            } catch (e: Exception) {
            }
        }
    }

    fun showRemindMeBottomPopUpDialog() {
        val remindMeViewBinding =
            BottomRemindPopUpLayoutBinding.inflate(LayoutInflater.from(requireContext()))

        remindMeDialog = BottomSheetDialog(requireContext())
        remindMeDialog.setContentView(remindMeViewBinding.root)

        remindMeViewBinding.apply {
            remindTomorrowOneLayout.setOnClickListener {
//                Toast.makeText(requireContext(), "tomorrow one clicked",Toast.LENGTH_LONG).show()
                remindMeDialog.dismiss()
            }
            remindTomorrowTwoLayout.setOnClickListener {
//                Toast.makeText(requireContext(), "tomorrow two clicked",Toast.LENGTH_LONG).show()
                remindMeDialog.dismiss()
            }
            remindTomorrowThreeLayout.setOnClickListener {
//                Toast.makeText(requireContext(), "tomorrow three clicked",Toast.LENGTH_LONG).show()
                remindMeDialog.dismiss()
            }
            remindPickDateTimeLayout.setOnClickListener {
//                Toast.makeText(requireContext(), "pick date time clicked",Toast.LENGTH_LONG).show()
                remindMeDialog.dismiss()
            }
            remindPickPlaceLayout.setOnClickListener {
//                Toast.makeText(requireContext(), "pick place clicked",Toast.LENGTH_LONG).show()
                remindMeDialog.dismiss()
            }
        }
        remindMeDialog.show()
    }

    fun showAddOnBottomPopUpDialog() {
        val addOnViewBinding =
            BottomSheetPopUpLayoutBinding.inflate(LayoutInflater.from(requireContext()))

        addOnDialog = BottomSheetDialog(requireContext())
        addOnDialog.setContentView(addOnViewBinding.root)
        addOnViewBinding.popupCheckboxesLayout.visibility = View.GONE

        addOnViewBinding.apply {
            popupTakePhotoLayout.setOnClickListener {
//                Toast.makeText(requireContext(), "take photo clicked",Toast.LENGTH_LONG).show()
//                CameraHelper.handleCameraAction(requireContext(), requireActivity().contentResolver, cameraResultLauncher, permissionLauncher)
                addOnDialog.dismiss()
            }
            popupAddImageLayout.setOnClickListener {
//                Toast.makeText(requireContext(), "add image clicked",Toast.LENGTH_LONG).show()
                addOnDialog.dismiss()
            }
            popupDrawingLayout.setOnClickListener {
//                Toast.makeText(requireContext(), "drawing clicked",Toast.LENGTH_LONG).show()
                addOnDialog.dismiss()
            }
            popupRecordingLayout.setOnClickListener {
//                Toast.makeText(requireContext(), "recording clicked",Toast.LENGTH_LONG).show()
                addOnDialog.dismiss()
            }
        }
        addOnDialog.show()
    }

    fun showMenuBottomPopUpDialog() {
        val menuViewBinding =
            BottomMenuPopUpLayoutBinding.inflate(LayoutInflater.from(requireContext()))

        menuDialog = BottomSheetDialog(requireContext())
        menuDialog.setContentView(menuViewBinding.root)

        menuViewBinding.apply {
            menuDeleteLayout.setOnClickListener {
//                Toast.makeText(requireContext(), "delete clicked",Toast.LENGTH_LONG).show()
                menuDialog.dismiss()
            }
            menuMakeACopyLayout.setOnClickListener {
//                Toast.makeText(requireContext(), "make a copy clicked",Toast.LENGTH_LONG).show()
                menuDialog.dismiss()
            }
            menuSendLayout.setOnClickListener {
//                Toast.makeText(requireContext(), "send clicked",Toast.LENGTH_LONG).show()
                menuDialog.dismiss()
            }
            menuCollaboratorLayout.setOnClickListener {
//                Toast.makeText(requireContext(), "collaborator clicked",Toast.LENGTH_LONG).show()
                menuDialog.dismiss()
            }
            menuLabelsLayout.setOnClickListener {
//                Toast.makeText(requireContext(), "labels clicked",Toast.LENGTH_LONG).show()
                menuDialog.dismiss()
            }
            menuHelpFeedbackLayout.setOnClickListener {
//                Toast.makeText(requireContext(), "helpFeedback clicked",Toast.LENGTH_LONG).show()
                menuDialog.dismiss()
            }
        }
        menuDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("AddFragment", "Fragment is destroyed")
        (activity as AppCompatActivity).supportActionBar?.show()
        (activity as MainActivity).showFloatingActionButton()
        _binding = null
    }
}