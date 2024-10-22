package com.example.notespace.ui

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.notespace.R
import com.example.notespace.databinding.ActivityMainBinding
import com.example.notespace.databinding.FragmentDashboardBinding
import com.example.notespace.databinding.GalleryDialogBoxBinding


class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(inflater)
        return(binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateEmptyNotesUi()
    }

    private fun updateEmptyNotesUi(){
        binding.recyclerView.visibility= View.GONE
        binding.emptyNoteLayout.visibility = View.VISIBLE
    }





}
