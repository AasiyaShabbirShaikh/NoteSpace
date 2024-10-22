package com.example.notespace.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.notespace.R
import com.example.notespace.databinding.FragmentReminderBinding
import androidx.core.view.GravityCompat

class ReminderFragment : Fragment() {

    private lateinit var binding: FragmentReminderBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReminderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the toolbar directly from the included layout
//        val toolbar = binding.fragmentToolbar.findViewById<androidx.appcompat.widget.Toolbar>(R.id.fragment_toolbar)
//        (activity as AppCompatActivity).setSupportActionBar(toolbar)
//
//        // Set the title of the toolbar
//        (activity as AppCompatActivity).supportActionBar?.title = "Reminders"

        // Set up toolbar icon click listeners
//        setupToolbarIcons()

        // Update UI for empty reminders
        updateEmptyReminderUi()
    }

//    private fun setupToolbarIcons() {
//        // Handle the drawer menu icon click
//        binding.fragmentToolbar.findViewById<ImageView>(R.id.fragment_drawer_menu_icon).setOnClickListener {
//            openDrawer()
//        }
//
//        // Handle the search icon click
//        binding.fragmentToolbar.findViewById<ImageView>(R.id.fragment_search_icon).setOnClickListener {
//            // Handle search action (e.g., launch search dialog or activity)
//        }
//
//        // Handle the grid icon click
//        binding.fragmentToolbar.findViewById<ImageView>(R.id.fragment_grid_icon).setOnClickListener {
//            // Handle grid layout action (e.g., switch to grid view)
//        }
//    }

//    private fun openDrawer() {
//        // Get the DrawerLayout from the parent activity
//        val drawerLayout = activity?.findViewById<DrawerLayout>(R.id.main_drawer)
//        if (drawerLayout != null && !drawerLayout.isDrawerOpen(GravityCompat.START)) {
//            drawerLayout.openDrawer(GravityCompat.START)
//        }
//    }

    private fun updateEmptyReminderUi() {
        binding.emptyReminderLayout.visibility = View.VISIBLE
    }
}
