package com.example.notespace.ui

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notespace.MainActivity
import com.example.notespace.R
import com.example.notespace.adapter.NotesAdapter
import com.example.notespace.databinding.ActivityMainBinding
import com.example.notespace.databinding.FragmentDashboardBinding
import com.example.notespace.databinding.GalleryDialogBoxBinding
import com.example.notespace.model.Notes
import com.example.notespace.viewModel.NotesViewModel


class DashboardFragment : Fragment(){

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var notesViewModel: NotesViewModel
    private lateinit var notesAdapter: NotesAdapter

    private var isNoteGrid = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return(binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewModel()
        setUpDashboardRecyclerView()



    }

    private fun setUpViewModel(){
        notesViewModel = (activity as MainActivity).notesViewModel

    }

    private fun setUpDashboardRecyclerView(){
        notesAdapter = NotesAdapter()
        binding.recyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = notesAdapter
        }

        notesViewModel.getAllNotes().observe(viewLifecycleOwner){note ->
            notesAdapter.differ.submitList(note)
            updateDashboardUI(note)
        }
    }

    private fun updateDashboardUI(notes : List<Notes>?){
        if(notes.isNullOrEmpty()){
            binding.emptyNoteLayout.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
        }
        else{
            binding.emptyNoteLayout.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding= null
    }


}
