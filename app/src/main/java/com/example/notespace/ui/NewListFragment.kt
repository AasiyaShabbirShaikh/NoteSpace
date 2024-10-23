package com.example.notespace.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import com.example.notespace.MainActivity
import com.example.notespace.R
import com.example.notespace.databinding.BottomMenuPopUpLayoutBinding
import com.example.notespace.databinding.BottomSheetPopUpLayoutBinding
import com.example.notespace.databinding.FragmentAddBinding
import com.example.notespace.databinding.FragmentNewListBinding
import com.google.android.material.bottomsheet.BottomSheetDialog


class NewListFragment : AddNewListBaseFragment() {

    private var _binding : FragmentNewListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentNewListBinding.inflate(inflater, container, false)
        setUpActionBar()
        setUpBottomNavBar()
        (requireActivity() as MainActivity).hideBottomNavBar()
        baseBinding.baseContainer.removeAllViews()
        baseBinding.baseContainer.addView(binding.root)
        return (baseBinding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
        (activity as MainActivity).hideFloatingActionButton()

        binding.hideMenuDotsIcon.setOnClickListener {
            checkboxHideMenuOption()
        }
    }

    private fun checkboxHideMenuOption(){
        val popupMenu =PopupMenu(requireContext(), binding.hideMenuDotsIcon)
        popupMenu.menu.add("Hide checkboxes")

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                0 -> {
                    Toast.makeText(requireContext(), "hide checkboxes", Toast.LENGTH_LONG).show()
                }
            }
            false

        }
        popupMenu.show()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        (activity as AppCompatActivity).supportActionBar?.show()
        (activity as MainActivity).showFloatingActionButton()
        (requireActivity() as MainActivity).showBottomNavBar()
        _binding = null
    }

}