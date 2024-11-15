package com.example.notespace.ui

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import com.example.notespace.MainActivity
import com.example.notespace.R
import com.example.notespace.databinding.ChooseThemeDialogBoxBinding
import com.example.notespace.databinding.FragmentSettingsBinding
import com.example.notespace.databinding.GalleryDialogBoxBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var navController : NavController
    private var themeDialogBox : Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
        (activity as MainActivity).hideFloatingActionButton()
        (activity as MainActivity).hideMainBottomNavLayout()

        binding.systemDefaultText.setOnClickListener {
            showChooseThemesDialogBox()
        }

        binding.includeSettingsToolbar.settingsBackArrowIcon.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun showChooseThemesDialogBox(){
        val themeDialogBinding = ChooseThemeDialogBoxBinding.inflate(layoutInflater)
        themeDialogBox = Dialog(requireContext())
        themeDialogBox?.setContentView(themeDialogBinding.root)
        themeDialogBox?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        themeDialogBox?.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        themeDialogBox?.setCancelable(true)

        themeDialogBinding.apply {
            cancelTextButton.setOnClickListener {
                themeDialogBox?.dismiss()
            }
        }
        themeDialogBox?.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as AppCompatActivity).supportActionBar?.show()
        (activity as MainActivity).showFloatingActionButton()
        (activity as MainActivity).showMainBottomNavLayout()
    }


}