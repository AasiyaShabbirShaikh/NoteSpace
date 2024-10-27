package com.example.notespace.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.notespace.MainActivity
import com.example.notespace.R
import com.example.notespace.databinding.FragmentAddBinding
import com.example.notespace.model.Notes
import com.example.notespace.viewModel.NotesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddFragment : AddNewListBaseFragment() {

    private var _binding : FragmentAddBinding? = null
    private val binding get() = _binding!!

    private val notesViewModel: NotesViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentAddBinding.inflate(inflater, container, false)

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

    override fun onResume() {
        super.onResume()
        Log.d("AddFragment", "Fragment is resumed")
    }

    override fun onPause() {
        super.onPause()
        saveNote()
        Log.d("AddFragment", "Fragment is paused")
    }

//    private fun saveNote() {
//        val title = binding.addTitleEditText.text.toString()
//        val description = binding.addDescriptionEditText.text.toString()
//
//        val note = Notes(0, title, description, System.currentTimeMillis())
//
//        viewLifecycleOwner.lifecycleScope.launch {
//            val currentId = notesViewModel.addNote(note)
//
//            // Log the title and description before the condition check
//            Log.d("AddFragment", "Title: '$title', Description: '$description', Inserted note ID: $currentId")
//
//            if (title.isEmpty() && description.isEmpty()) {
//                Log.d("AddFragment", "Inserted note ID for empty note: $currentId")
//                Handler(Looper.getMainLooper()).postDelayed({
//                    notesViewModel.deleteNoteById(currentId)
//                    Toast.makeText(requireContext(), "Note discarded", Toast.LENGTH_LONG).show()
//                }, 3000)
//            } else {
//                Log.d("AddFragment", "Note saved with title: '$title' and description: '$description'")
//            }
//        }
//    }


//    private fun saveNote() {
//        val title = binding.addTitleEditText.text.toString()
//        val description = binding.addDescriptionEditText.text.toString()
//
//        val note = Notes(0, title, description, System.currentTimeMillis())
//
//        viewLifecycleOwner.lifecycleScope.launch {
//            try {
//                val currentId = notesViewModel.addNote(note)
//
//                // Log the title and description before the condition check
//                Log.d("AddFragment", "Title: '$title', Description: '$description', Inserted note ID: $currentId")
//
//                if (title.isEmpty() && description.isEmpty()) {
//                    Log.d("AddFragment", "Inserted note ID for empty note: $currentId")
//                    Handler(Looper.getMainLooper()).postDelayed({
//                        notesViewModel.deleteNoteById(currentId)
//                        Toast.makeText(requireContext(), "Note discarded", Toast.LENGTH_LONG).show()
//                    }, 3000)
//                } else {
//                    Log.d("AddFragment", "Note saved with title: '$title' and description: '$description'")
//                }
//            } catch (e: Exception) {
//                Log.e("AddFragment", "Error saving note: ${e.message}")
//            }
//        }
//    }

//    private fun saveNote() {
//        val title = binding.addTitleEditText.text.toString()
//        val description = binding.addDescriptionEditText.text.toString()
//
//        val note = Notes(0, title, description, System.currentTimeMillis())
//
//        viewLifecycleOwner.lifecycleScope.launch {
//            try {
//                val currentId = notesViewModel.addNote(note)
//
//                // Log the title and description before the condition check
//                Log.d("AddFragment", "Title: '$title', Description: '$description', Inserted note ID: $currentId")
//
//                if (title.isEmpty() && description.isEmpty()) {
//                    Log.d("AddFragment", "Inserted note ID for empty note: $currentId")
//
//                    // Delay for 3 seconds
//                    delay(3000)
//
//                    // Delete the empty note
//                    notesViewModel.deleteNoteById(currentId)
//                    Toast.makeText(requireContext(), "Note discarded", Toast.LENGTH_LONG).show()
//                } else {
//                    Log.d("AddFragment", "Note saved with title: '$title' and description: '$description'")
//                    // Navigate or perform other actions after saving is successful
//                    // For example, navigating to the dashboard
//                    findNavController().navigate(R.id.action_addFragment_to_dashboardFragment)
//                }
//            } catch (e: Exception) {
//                Log.e("AddFragment", "Error saving note: ${e.message}")
//            }
//        }
//    }


    private fun saveNote() {
        val title = binding.addTitleEditText.text.toString()
        val description = binding.addDescriptionEditText.text.toString()

        val note = Notes(0, title, description, System.currentTimeMillis())

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val currentId = notesViewModel.addNote(note)
                Log.d("AddFragment", "Inserted note ID: $currentId")

                if (title.isEmpty() && description.isEmpty()) {
                    Log.d("AddFragment", "Inserted note ID for empty note: $currentId")

                    // Delay before deletion
                    delay(3000)

                    // Check if the fragment is still added before attempting to delete
                    if (isAdded) {
                        notesViewModel.deleteNoteById(currentId)
                        Toast.makeText(requireContext(), "Note discarded", Toast.LENGTH_LONG).show()
                    }
                } else {
                    // Navigate to another fragment (e.g., DashboardFragment)
                    findNavController().navigate(R.id.action_addFragment_to_dashboardFragment)
                }
            } catch (e: Exception) {
                Log.e("AddFragment", "Error saving note: ${e.message}")
            }
        }
    }






    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("AddFragment", "Fragment is destroyed")
        (activity as AppCompatActivity).supportActionBar?.show()
        (activity as MainActivity).showFloatingActionButton()
        _binding = null
    }
}