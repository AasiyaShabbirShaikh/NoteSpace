package com.example.notespace.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.notespace.MainActivity
import com.example.notespace.R
import com.example.notespace.databinding.FragmentNewListBinding
import com.example.notespace.databinding.FragmentSearchNoteBinding

class searchNoteFragment : Fragment() {

    private lateinit var binding: FragmentSearchNoteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchNoteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
        (activity as MainActivity).hideFloatingActionButton()
        (activity as MainActivity).hideBottomNavBar()

        setUpSearchToolbar()

        binding.apply {
            musicLayout.setOnClickListener {
//                getAllNotesFromDashboard()
            }

            colorDropLayout.setOnClickListener {
//                getAllAppNotes()
            }

        }

    }


    private fun setUpSearchToolbar(){
        binding.searchIncludeToolbar.apply {
            searchBackArrowIcon.setOnClickListener {
                requireActivity().onBackPressed()
            }
            searchEditText.setOnClickListener {
//                searchNote()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as AppCompatActivity).supportActionBar?.show()
        (activity as MainActivity).showFloatingActionButton()
        (activity as MainActivity).showBottomNavBar()

    }

}