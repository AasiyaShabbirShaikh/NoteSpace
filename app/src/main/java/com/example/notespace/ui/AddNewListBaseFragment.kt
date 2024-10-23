package com.example.notespace.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.notespace.databinding.BottomMenuPopUpLayoutBinding
import com.example.notespace.databinding.BottomRemindPopUpLayoutBinding
import com.example.notespace.databinding.BottomSheetPopUpLayoutBinding
import com.example.notespace.databinding.FragmentAddNewListBaseBinding
import com.google.android.material.bottomsheet.BottomSheetDialog


abstract class AddNewListBaseFragment : Fragment() {

    private lateinit var binding : FragmentAddNewListBaseBinding
    protected val baseBinding get() = binding

    private lateinit var addOnDialog : BottomSheetDialog
    private lateinit var menuDialog: BottomSheetDialog
    private lateinit var remindMeDialog : BottomSheetDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddNewListBaseBinding.inflate(layoutInflater)
        setUpActionBar()
        setUpBottomNavBar()
        return binding.root
    }

     fun setUpActionBar(){
        binding.baseToolbar.apply {
            backArrowIcon.setOnClickListener {
                requireActivity().onBackPressed()
            }
            pinIcon.setOnClickListener {
                Toast.makeText(requireContext(), "Pinned Clicked", Toast.LENGTH_LONG).show()
            }
            remindMeIcon.setOnClickListener {
                Toast.makeText(requireContext(), "Remind Clicked", Toast.LENGTH_LONG).show()
                showRemindMeBottomPopUpDialog()
            }
            archiveIcon.setOnClickListener {
                Toast.makeText(requireContext(), "Archive Clicked", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun setUpBottomNavBar(){
        binding.apply{
            baseAddOnIcon.setOnClickListener {
                Toast.makeText(requireContext(), "add on Clicked", Toast.LENGTH_SHORT).show()
                showAddOnBottomPopUpDialog()
            }
            baseColorPaleteIcon.setOnClickListener {
                Toast.makeText(requireContext(), "color palete Clicked", Toast.LENGTH_SHORT).show()

            }
            baseTextStylePickerIcon.setOnClickListener {
                Toast.makeText(requireContext(), "text style Clicked", Toast.LENGTH_SHORT).show()

            }
            baseMenuIcon.setOnClickListener {
                Toast.makeText(requireContext(), "menu Clicked", Toast.LENGTH_SHORT).show()
                showMenuBottomPopUpDialog()
            }
        }
    }

     fun showAddOnBottomPopUpDialog(){
        val addOnViewBinding = BottomSheetPopUpLayoutBinding.inflate(LayoutInflater.from(requireContext()))

        addOnDialog = BottomSheetDialog(requireContext())
        addOnDialog.setContentView(addOnViewBinding.root)
        addOnViewBinding.popupCheckboxesLayout.visibility = View.GONE

        addOnViewBinding.apply {
            popupTakePhotoLayout.setOnClickListener {
                Toast.makeText(requireContext(), "take photo clicked",Toast.LENGTH_LONG).show()
                addOnDialog.dismiss()
            }
            popupAddImageLayout.setOnClickListener {
                Toast.makeText(requireContext(), "add image clicked",Toast.LENGTH_LONG).show()
                addOnDialog.dismiss()
            }
            popupDrawingLayout.setOnClickListener {
                Toast.makeText(requireContext(), "drawing clicked",Toast.LENGTH_LONG).show()
                addOnDialog.dismiss()
            }
            popupRecordingLayout.setOnClickListener {
                Toast.makeText(requireContext(), "recording clicked",Toast.LENGTH_LONG).show()
                addOnDialog.dismiss()
            }


        }
        addOnDialog.show()

    }

     fun showMenuBottomPopUpDialog(){
        val menuViewBinding = BottomMenuPopUpLayoutBinding.inflate(LayoutInflater.from(requireContext()))

        menuDialog = BottomSheetDialog(requireContext())
        menuDialog.setContentView(menuViewBinding.root)

        menuViewBinding.apply {
            menuDeleteLayout.setOnClickListener {
                Toast.makeText(requireContext(), "delete clicked",Toast.LENGTH_LONG).show()
                menuDialog.dismiss()
            }
            menuMakeACopyLayout.setOnClickListener {
                Toast.makeText(requireContext(), "make a copy clicked",Toast.LENGTH_LONG).show()
                menuDialog.dismiss()
            }
            menuSendLayout.setOnClickListener {
                Toast.makeText(requireContext(), "send clicked",Toast.LENGTH_LONG).show()
                menuDialog.dismiss()
            }
            menuCollaboratorLayout.setOnClickListener {
                Toast.makeText(requireContext(), "collaborator clicked",Toast.LENGTH_LONG).show()
                menuDialog.dismiss()
            }
            menuLabelsLayout.setOnClickListener {
                Toast.makeText(requireContext(), "labels clicked",Toast.LENGTH_LONG).show()
                menuDialog.dismiss()
            }
            menuHelpFeedbackLayout.setOnClickListener {
                Toast.makeText(requireContext(), "helpFeedback clicked",Toast.LENGTH_LONG).show()
                menuDialog.dismiss()
            }


        }
        menuDialog.show()
    }

     fun showRemindMeBottomPopUpDialog(){
        val remindMeViewBinding = BottomRemindPopUpLayoutBinding.inflate(LayoutInflater.from(requireContext()))

        remindMeDialog = BottomSheetDialog(requireContext())
        remindMeDialog.setContentView(remindMeViewBinding.root)

        remindMeViewBinding.apply {
            remindTomorrowOneLayout.setOnClickListener {
                Toast.makeText(requireContext(), "tomorrow one clicked",Toast.LENGTH_LONG).show()
                remindMeDialog.dismiss()
            }
            remindTomorrowTwoLayout.setOnClickListener {
                Toast.makeText(requireContext(), "tomorrow two clicked",Toast.LENGTH_LONG).show()
                remindMeDialog.dismiss()
            }
            remindTomorrowThreeLayout.setOnClickListener {
                Toast.makeText(requireContext(), "tomorrow three clicked",Toast.LENGTH_LONG).show()
                remindMeDialog.dismiss()
            }
            remindPickDateTimeLayout.setOnClickListener {
                Toast.makeText(requireContext(), "pick date time clicked",Toast.LENGTH_LONG).show()
                remindMeDialog.dismiss()
            }
            remindPickPlaceLayout.setOnClickListener {
                Toast.makeText(requireContext(), "pick place clicked",Toast.LENGTH_LONG).show()
                remindMeDialog.dismiss()
            }
        }
        remindMeDialog.show()
    }


}