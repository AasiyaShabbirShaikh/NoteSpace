package com.example.notespace.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.notespace.databinding.NoteItemLayoutBinding
import com.example.notespace.model.Notes
import com.example.notespace.ui.DashboardFragmentDirections

class NotesAdapter :
    RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {
    class NoteViewHolder(
        val itemBinding: NoteItemLayoutBinding,
    ) : RecyclerView.ViewHolder(itemBinding.root) {
    }


    private val differCallback = object : DiffUtil.ItemCallback<Notes>() {
        override fun areItemsTheSame(oldItem: Notes, newItem: Notes): Boolean {
            return oldItem.noteId == newItem.noteId &&
                    oldItem.noteTitle == newItem.noteTitle &&
                    oldItem.noteDescription == newItem.noteDescription
        }

        override fun areContentsTheSame(oldItem: Notes, newItem: Notes): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            NoteItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    }

    override fun getItemCount(): Int {
        return (differ.currentList.size)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = differ.currentList[position]

        holder.itemBinding.noteTitleText.text = currentNote.noteTitle
        holder.itemBinding.noteDescriptionText.text = currentNote.noteDescription

        holder.itemView.setOnClickListener {
            holder.itemView.findNavController().navigate(
                DashboardFragmentDirections.actionDashboardFragmentToEditNoteFragment(
                    currentNote
                )
            )
        }
    }

}