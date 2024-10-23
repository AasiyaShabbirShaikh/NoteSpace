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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return(binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateEmptyNotesUi()

//        val menuHost = requireActivity()
//        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
//
//        notesViewModel = (activity as MainActivity).notesViewModel
//        setUpDashboardRecyclerView()
    }

    private fun updateEmptyNotesUi(){
        binding.recyclerView.visibility= View.GONE
        binding.emptyNoteLayout.visibility = View.VISIBLE
    }

//    private fun updateDashboardUI(note : List<Notes>?){
//        if(note != null){
//            if(note.isNotEmpty()){
//                binding.emptyNoteLayout.visibility = View.GONE
//                binding.recyclerView.visibility = View.VISIBLE
//            }
//            else{
//                binding.emptyNoteLayout.visibility = View.VISIBLE
//                binding.recyclerView.visibility = View.GONE
//            }
//        }
//    }

//    private fun setUpDashboardRecyclerView(){
//        notesAdapter = NotesAdapter()
//        binding.recyclerView.apply {
//            layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
//            setHasFixedSize(true)
//
//            adapter = notesAdapter
//        }
//
//        activity?.let{
//            notesViewModel.getAllNotes().observe(viewLifecycleOwner){note ->
//                notesAdapter.differ.submitList(note)
//                updateDashboardUI(note)
//
//            }
//        }
//    }
//
//    override fun onQueryTextSubmit(p0: String?): Boolean {
//        return false
//    }
//
//    override fun onQueryTextChange(searchText: String?): Boolean {
//        if(searchText != null){
//            searchNote(searchText)
//        }
//        return true
//    }
//
//    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
//        menu.clear()
//        menuInflater.inflate(R.menu.add_on_menu,menu)
//
//        val menuSearch = menu.findItem(R.id.search_text).actionView as SearchView
//        menuSearch.isSubmitButtonEnabled = false
//        menuSearch.setOnQueryTextListener(this)
//    }
//
//    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
//        return false
//    }
//
//    private fun searchNote(keySearch: String?){
//        val searchQuery = "%$keySearch"
//
//        notesViewModel.searchNote(searchQuery).observe(this){ list ->
//            notesAdapter.differ.submitList(list)
//
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding= null
    }


}
