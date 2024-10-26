package com.example.notespace.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.notespace.MainActivity
import com.example.notespace.database.NotesDatabase
import com.example.notespace.databinding.BottomSheetPopUpLayoutBinding
import com.example.notespace.databinding.FragmentAddBinding
import com.example.notespace.model.Notes
import com.example.notespace.repository.NotesRepository
import com.example.notespace.viewModel.NotesViewModel
import com.example.notespace.viewModel.NotesViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog


class AddFragment : AddNewListBaseFragment() {

    private var _binding : FragmentAddBinding? = null
    private val binding get() = _binding!!

    private lateinit var notesViewModel: NotesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentAddBinding.inflate(inflater, container, false)


        val notesRepository =NotesRepository(NotesDatabase(requireContext()))
        val factory = NotesViewModelFactory(requireActivity().application, notesRepository)
        notesViewModel = ViewModelProvider(this, factory)[NotesViewModel::class.java]

        setUpActionBar()
        setUpBottomNavBar()

        baseBinding.baseContainer.removeAllViews()
        baseBinding.baseContainer.addView(binding.root)
        return (baseBinding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
        (activity as MainActivity).hideFloatingActionButton()
    }

    override fun onPause() {
        super.onPause()
        saveNote()
    }

    private fun saveNote(){
        val title = binding.addTitleEditText.text.toString()
        val description = binding.addDescriptionEditText.text.toString()

        val note = Notes(0, title, description, System.currentTimeMillis())
        notesViewModel.addNote(note)
        Toast.makeText(requireContext(),"Note Saved", Toast.LENGTH_LONG).show()

        if(title.isEmpty() && description.isEmpty()){
            Handler(Looper.getMainLooper()).postDelayed({
                notesViewModel.deleteNote(note)
                Toast.makeText(requireContext(),"Noted discarded", Toast.LENGTH_LONG).show()
            }, 5000)
        }

    }

//    private fun saveNote() {
//        val title = binding.addTitleEditText.text.toString()
//        val description = binding.addDescriptionEditText.text.toString()
//
//        // Create the note object with title, description, and timestamp
//        val note = Notes(0, title, description, System.currentTimeMillis())
//
//        // Save the note to database
//        notesViewModel.addNote(note)
//
//        if (isAdded) {
//            Toast.makeText(requireContext(), "Note Saved", Toast.LENGTH_LONG).show()
//        }
//
//        // Check if both title and description are empty
//        if (title.isEmpty() && description.isEmpty()) {
//            // Schedule deletion after 5 seconds if the note is empty
//            Handler(Looper.getMainLooper()).postDelayed({
//                // Confirm fragment is still attached before attempting deletion
//                if (isAdded) {
//                    // Delete the note if it's still in the database
//                    notesViewModel.deleteNote(note)
//                    Toast.makeText(requireContext(), "Note discarded", Toast.LENGTH_LONG).show()
//                }
//            }, 5000)
//        }
//    }


    override fun onDestroyView() {
        super.onDestroyView()
        (activity as AppCompatActivity).supportActionBar?.show()
        (activity as MainActivity).showFloatingActionButton()
        _binding = null
    }
}