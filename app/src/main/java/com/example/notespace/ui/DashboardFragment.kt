package com.example.notespace.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notespace.MainActivity
import com.example.notespace.NoteInterface
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
            {
                selectedNote ->
                Toast.makeText(requireContext(), "selected note", Toast.LENGTH_LONG).show()
                onNoteSelection()
            },
            {
                selectedCount ->
                Toast.makeText(requireContext(), "selected note", Toast.LENGTH_LONG).show()
                handleNoteSelectCount(selectedCount)
            }
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
        val mainActivity = activity as? MainActivity
        mainActivity?.apply {
            hideHeaderToolbar()
            showCustomToolbar()
        }
    }

    private fun handleNoteSelectCount(selectedCount : Int){
        if(selectedCount > 0){
            onNoteSelection()
            (activity as MainActivity).updateNotesCount(selectedCount)

        }
        else{
            (activity as MainActivity).hideCustomToolbar()
            (activity as MainActivity).showHeaderToolbar()
            notesAdapter.clearNoteSelection()
        }
    }


    override fun getSelectedNoteIds(): List<Long> {
        return notesAdapter.getSelectedNoteIds()
    }

    override fun deleteSelectedNotes() {
//        val selectedNoteIds = notesAdapter.getSelectedNoteIds()
//        if (selectedNoteIds.isNotEmpty()) {
//            notesViewModel.moveToTrash(selectedNoteIds)
//            notesAdapter.deleteSelectedNote()
//        } else {
//            Toast.makeText(requireContext(), "No notes selected", Toast.LENGTH_SHORT).show()
//        }

        val selectedNoteIds = getSelectedNoteIds()
        if (selectedNoteIds.isNotEmpty()) {
            notesViewModel.deleteNoteByIds(selectedNoteIds)
            notesAdapter.deleteSelectedNote(selectedNoteIds)
            clearSelectionAndToolbar()
        }
    }

    private fun clearSelectionAndToolbar(){
        notesAdapter.clearNoteSelection()
        (activity as? MainActivity)?.hideCustomToolbar()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding= null
    }

}
