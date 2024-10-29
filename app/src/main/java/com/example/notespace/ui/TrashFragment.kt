package com.example.notespace.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.notespace.R
import com.example.notespace.databinding.FragmentTrashBinding
import com.example.notespace.model.Notes


class TrashFragment : Fragment() {

    private lateinit var binding: FragmentTrashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTrashBinding.inflate(layoutInflater)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun updateTrashUI(notes : List<Notes>?){
        if(notes.isNullOrEmpty()){
            binding.emptyTrashLayout.visibility = View.VISIBLE
            binding.trashRecyclerView.visibility = View.GONE
        }
        else{
            binding.emptyTrashLayout.visibility = View.GONE
            binding.trashRecyclerView.visibility = View.VISIBLE
        }
    }
}