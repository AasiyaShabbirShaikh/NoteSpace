package com.example.notespace.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.notespace.MainActivity
import com.example.notespace.R
import com.example.notespace.databinding.FragmentEditNoteBinding

class EditNoteFragment : AddNewListBaseFragment() {

    private var _binding: FragmentEditNoteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentEditNoteBinding.inflate(inflater, container, false)

        setUpActionBar()
        setUpBottomNavBar()

        baseBinding.baseContainer.removeAllViews()
        baseBinding.baseContainer.addView(binding.root)
        return (binding.root)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
        (activity as MainActivity).hideFloatingActionButton()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        (activity as AppCompatActivity).supportActionBar?.show()
        (activity as MainActivity).showFloatingActionButton()
        _binding = null
    }

}