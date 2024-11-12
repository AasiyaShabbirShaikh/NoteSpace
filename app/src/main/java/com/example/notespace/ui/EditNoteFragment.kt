package com.example.notespace.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.notespace.MainActivity
import com.example.notespace.R
import com.example.notespace.databinding.FragmentEditNoteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditNoteFragment : AddNewListBaseFragment() {

    private var _binding: FragmentEditNoteBinding? = null
    private val binding get() = _binding!!

    private var noteId: Long? = null
    private var noteTitle: String? = null
    private var noteDescription: String? = null

    private var isNoteModified = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentEditNoteBinding.inflate(inflater, container, false)

//        setUpActionBar()
//        setUpBottomNavBar()

        baseBinding.baseContainer.removeAllViews()
        baseBinding.baseContainer.addView(binding.root)

        arguments?.let {
            noteId= it.getLong("noteId")
            noteTitle = it.getString("noteTitle")
            noteDescription = it.getString("noteDescription")
        }

        binding.apply {
            addTitleEditText.setText(noteTitle)
            addDescriptionEditText.setText(noteDescription)

            addTitleEditText.addTextChangedListener {
                isNoteModified = true
            }

            addDescriptionEditText.addTextChangedListener {
                isNoteModified = true
            }
        }

        return (binding.root)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
        (activity as MainActivity).hideFloatingActionButton()
    }

//    override fun onResume() {
//        super.onResume()
//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
//            if (isNoteModified) {
//                saveNote()
//            } else {
//                requireActivity().onBackPressed()
//            }
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as AppCompatActivity).supportActionBar?.show()
        (activity as MainActivity).showFloatingActionButton()
        _binding = null
    }

}