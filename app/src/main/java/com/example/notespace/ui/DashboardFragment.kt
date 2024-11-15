package com.example.notespace.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notespace.MainActivity
import com.example.notespace.NoteInterface
import com.example.notespace.R
import com.example.notespace.adapter.NotesAdapter
import com.example.notespace.databinding.FragmentDashboardBinding
import com.example.notespace.model.Notes
import com.example.notespace.viewModel.NotesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : Fragment(), NoteInterface{

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val notesViewModel: NotesViewModel by viewModels()
    private lateinit var notesAdapter: NotesAdapter

    private val selectedIds = mutableListOf<Long>()
    private var isSelectionMode = false
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
        setUpDashboardRecyclerView()
        observeNotes()
    }

    private fun setUpDashboardRecyclerView(){
        notesAdapter = NotesAdapter(
            onNoteLongClick = {note ->
                if (!isSelectionMode) {
                    isSelectionMode = true
                    toggleNoteSelection(note)
                }
            },
            onNoteClick = { note ->
//                if (isSelectionMode) {
//                    toggleNoteSelection(note)
//                }
//                else{
//                println("passed note ${note}")
//                val action =
//                    DashboardFragmentDirections.actionDashboardFragmentToEditNoteFragment(note)
//                findNavController().navigate(action)
//                }
                val bundle = Bundle().apply {
                    putParcelable("note", note)  // Pass the note ID or other necessary data
                }
                findNavController().navigate(R.id.action_dashboardFragment_to_editNoteFragment, bundle)
            },
            onSelectCountChange = { selectedCount ->
                handleNoteSelectCount(selectedCount)
            }
//            notesViewModel = notesViewModel
        )

        binding.recyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = notesAdapter
        }
    }

    private fun observeNotes(){
        notesViewModel.getAllNotes().observe(viewLifecycleOwner){note ->
            notesAdapter.differ.submitList(note)
            Log.d("NotesViewModel", "Notes moved : $note")
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

    private fun onNoteSelection(){
//        val mainActivity = activity as? MainActivity
//        mainActivity?.apply {
//            hideHeaderToolbar()
//            showCustomToolbar()
//        }
        (activity as MainActivity).hideHeaderToolbar()
        (activity as MainActivity).showCustomToolbar()
    }


    private fun toggleNoteSelection(note: Notes) {
        if (selectedIds.contains(note.noteId)) {
            selectedIds.remove(note.noteId)
        } else {
            selectedIds.add(note.noteId)
        }
//        notesAdapter.updateSelectedNotes(selectedIds)
        handleNoteSelectCount(selectedIds.size)
    }

    private fun handleNoteSelectCount(selectedCount : Int){
        if(selectedCount > 0){
            onNoteSelection()
            (activity as MainActivity).updateNotesCount(selectedCount)
        }
        else{
            clearSelectionAndToolbar()
        }
    }

    override fun getSelectedNoteIds(): List<Long> = selectedIds

    override fun deleteSelectedNotes() {
        if (selectedIds.isNotEmpty()) {
            notesViewModel.moveToTrash(selectedIds)
            Log.e("NotesViewModel", "Notes moved to trash: $selectedIds")
//            notesAdapter.removeSelectedNotes(selectedIds)
//            (activity as? MainActivity)?.getAllNotes()
            clearSelectionAndToolbar()
//            notesViewModel.getAllNotes()
        }

    }

    private fun clearSelectionAndToolbar(){
        selectedIds.clear()
        isSelectionMode = false
        notesAdapter.clearNoteSelection()
        (activity as? MainActivity)?.hideCustomToolbar()
        (activity as? MainActivity)?.showHeaderToolbar()
        (activity as? MainActivity)?.updateNotesCount(0)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding= null
    }


}
