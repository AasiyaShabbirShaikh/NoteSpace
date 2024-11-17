package com.example.notespace.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.notespace.R
import com.example.notespace.databinding.FragmentArchiveBinding


class ArchiveFragment : Fragment() {

    private lateinit var binding: FragmentArchiveBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArchiveBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateEmptyArchiveUi()
    }

    private fun updateEmptyArchiveUi(){
        binding.emptyArchiveLayout.visibility = View.VISIBLE
    }

}