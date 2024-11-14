package com.example.notespace.ui

import android.net.Uri
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

    private var imageUri: Uri? = null

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

        // Use the same key "image" as in MainActivity
        imageUri = arguments?.getParcelable("image")
        println("Received: $imageUri")

        if (imageUri != null) {
            binding.addImageView.visibility = View.VISIBLE
            binding.addImageView.setImageURI(imageUri)
            (activity as MainActivity).hideBottomNavBar()
        } else {
            Toast.makeText(context, "Image URI is null", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onPause() {
        super.onPause()
        saveNote()
    }

    private fun saveNote() {
        val title = binding.addTitleEditText.text.toString()
        val description = binding.addDescriptionEditText.text.toString()

        val note = Notes(0, title, description, System.currentTimeMillis(),false, imageUri?.toString())

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val currentId = notesViewModel.addNote(note)

                if (title.isEmpty() && description.isEmpty() && imageUri == null) {
                    delay(3000)
                    if (isAdded) {
                        notesViewModel.deleteNoteById(currentId)
//                        Toast.makeText(requireContext(), getString(R.string.note_discarded), Toast.LENGTH_LONG).show()
                    }
                } else {
                    findNavController().navigate(R.id.action_addFragment_to_dashboardFragment)
                }
            } catch (e: Exception) {
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