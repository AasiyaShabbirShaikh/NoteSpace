package com.example.notespace.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import com.example.notespace.MainActivity
import com.example.notespace.R
import com.example.notespace.databinding.BottomMenuPopUpLayoutBinding
import com.example.notespace.databinding.BottomSheetPopUpLayoutBinding
import com.example.notespace.databinding.FragmentNewListBinding
import com.google.android.material.bottomsheet.BottomSheetDialog


class NewListFragment : Fragment() {

    private lateinit var binding: FragmentNewListBinding

    private lateinit var newListAddOnDialog: BottomSheetDialog
    private lateinit var newListMenuDialog : BottomSheetDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewListBinding.inflate(layoutInflater)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
        (requireActivity() as MainActivity).hideBottomNavBar()

        handleHeaderToolbarIconClick()
        handleBottomBarIconClick()
    }

    private fun handleHeaderToolbarIconClick(){
        binding.headerToolbar.apply {
            backArrowIcon.setOnClickListener {
                requireActivity().onBackPressed()
            }
            pinIcon.setOnClickListener {
                Toast.makeText(requireContext(), "Pinned Clicked", Toast.LENGTH_LONG).show()
            }
            remindMeIcon.setOnClickListener {
                Toast.makeText(requireContext(), "Remind Clicked", Toast.LENGTH_LONG).show()
            }
            archiveIcon.setOnClickListener {
                Toast.makeText(requireContext(), "Archive Clicked", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun handleBottomBarIconClick(){
        binding.apply{
            addOnIcon.setOnClickListener {
                Toast.makeText(requireContext(), "add on Clicked", Toast.LENGTH_SHORT).show()
                showNewAddOnPopUp()
            }
            colorPaleteIcon.setOnClickListener {
                Toast.makeText(requireContext(), "color palete Clicked", Toast.LENGTH_SHORT).show()

            }
            textStylePickerIcon.setOnClickListener {
                Toast.makeText(requireContext(), "color palete Clicked", Toast.LENGTH_SHORT).show()

            }
            menuIcon.setOnClickListener {
                Toast.makeText(requireContext(), "color palete Clicked", Toast.LENGTH_SHORT).show()
                showNewMenuPopUp()
            }
        }

    }


    private fun showNewAddOnPopUp(){
        val addOnViewBinding = BottomSheetPopUpLayoutBinding.inflate(LayoutInflater.from(requireContext()))

        newListAddOnDialog = BottomSheetDialog(requireContext())
        newListAddOnDialog.setContentView(addOnViewBinding.root)
        addOnViewBinding.popupCheckboxesLayout.visibility = View.GONE

        addOnViewBinding.apply {
            popupTakePhotoLayout.setOnClickListener {
                Toast.makeText(requireContext(), "take photo clicked",Toast.LENGTH_LONG).show()
                newListAddOnDialog.dismiss()
            }
            popupAddImageLayout.setOnClickListener {
                Toast.makeText(requireContext(), "add image clicked",Toast.LENGTH_LONG).show()
                newListAddOnDialog.dismiss()
            }
            popupDrawingLayout.setOnClickListener {
                Toast.makeText(requireContext(), "drawing clicked",Toast.LENGTH_LONG).show()
                newListAddOnDialog.dismiss()
            }
            popupRecordingLayout.setOnClickListener {
                Toast.makeText(requireContext(), "recording clicked",Toast.LENGTH_LONG).show()
                newListAddOnDialog.dismiss()
            }


        }
        newListAddOnDialog.show()

    }

    private fun showNewMenuPopUp(){
        val menuViewBinding = BottomMenuPopUpLayoutBinding.inflate(LayoutInflater.from(requireContext()))

        newListMenuDialog = BottomSheetDialog(requireContext())
        newListMenuDialog.setContentView(menuViewBinding.root)

        menuViewBinding.apply {
            menuDeleteLayout.setOnClickListener {
                Toast.makeText(requireContext(), "delete clicked",Toast.LENGTH_LONG).show()
                newListMenuDialog.dismiss()
            }
            menuMakeACopyLayout.setOnClickListener {
                Toast.makeText(requireContext(), "make a copy clicked",Toast.LENGTH_LONG).show()
                newListMenuDialog.dismiss()
            }
            menuSendLayout.setOnClickListener {
                Toast.makeText(requireContext(), "send clicked",Toast.LENGTH_LONG).show()
                newListMenuDialog.dismiss()
            }
            menuCollaboratorLayout.setOnClickListener {
                Toast.makeText(requireContext(), "collaborator clicked",Toast.LENGTH_LONG).show()
                newListMenuDialog.dismiss()
            }
            menuLabelsLayout.setOnClickListener {
                Toast.makeText(requireContext(), "labels clicked",Toast.LENGTH_LONG).show()
                newListMenuDialog.dismiss()
            }
            menuHelpFeedbackLayout.setOnClickListener {
                Toast.makeText(requireContext(), "helpFeedback clicked",Toast.LENGTH_LONG).show()
                newListMenuDialog.dismiss()
            }


        }
        newListMenuDialog.show()

    }


    override fun onDestroyView() {
        super.onDestroyView()
        (activity as AppCompatActivity).supportActionBar?.show()
        (requireActivity() as MainActivity).showBottomNavBar()
    }

}