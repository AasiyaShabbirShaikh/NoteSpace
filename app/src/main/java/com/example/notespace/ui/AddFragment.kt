package com.example.notespace.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notespace.databinding.BottomSheetPopUpLayoutBinding
import com.example.notespace.databinding.FragmentAddBinding
import com.google.android.material.bottomsheet.BottomSheetDialog


class AddFragment : Fragment() {

    private lateinit var binding: FragmentAddBinding
    private lateinit var addPopUpDialog : BottomSheetDialog
    private lateinit var addRemindMeDialog : BottomSheetDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentAddBinding.inflate(layoutInflater)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()

        binding.addAddOnIcon.setOnClickListener {
            Toast.makeText(requireContext(), "Clicked", Toast.LENGTH_SHORT).show()

            showAddBottomPopUpMenu()
        }
    }


    private fun showAddBottomPopUpMenu(){
        val popUpViewBinding = BottomSheetPopUpLayoutBinding.inflate(LayoutInflater.from(requireContext()))

        addPopUpDialog = BottomSheetDialog(requireContext())
        addPopUpDialog.setContentView(popUpViewBinding.root)


        popUpViewBinding.apply {
            popupTakePhotoLayout.setOnClickListener {
                Toast.makeText(requireContext(), "take photo clicked",Toast.LENGTH_LONG).show()
                addPopUpDialog.dismiss()
            }
            popupAddImageLayout.setOnClickListener {
                Toast.makeText(requireContext(), "add image clicked",Toast.LENGTH_LONG).show()
                addPopUpDialog.dismiss()
            }
            popupDrawingLayout.setOnClickListener {
                Toast.makeText(requireContext(), "drawing clicked",Toast.LENGTH_LONG).show()
                addPopUpDialog.dismiss()
            }
            popupRecordingLayout.setOnClickListener {
                Toast.makeText(requireContext(), "recording clicked",Toast.LENGTH_LONG).show()
                addPopUpDialog.dismiss()
            }
            popupCheckboxesLayout.setOnClickListener {
                Toast.makeText(requireContext(), "checkboxes clicked",Toast.LENGTH_LONG).show()
                addPopUpDialog.dismiss()
            }

        }
        addPopUpDialog.show()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as AppCompatActivity).supportActionBar?.show()
    }
}