package com.example.notespace.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.notespace.R
import com.example.notespace.databinding.NoteItemLayoutBinding
import com.example.notespace.model.Notes
import com.example.notespace.ui.DashboardFragmentDirections

class NotesAdapter(private val onNoteLongClick: (Notes) -> Unit, private val onSelectCountChange: (Int) -> Unit) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private val selectedNotes = mutableSetOf<Notes>()

    inner class NoteViewHolder(val itemBinding: NoteItemLayoutBinding, ) : RecyclerView.ViewHolder(itemBinding.root){
        init {
            itemView.setOnLongClickListener {
                val note = differ.currentList[adapterPosition]
                toggleSelection(note)
                onNoteLongClick(note)
                true
            }
        }

        private fun toggleSelection(note:Notes){
            if(selectedNotes.contains(note)){
                selectedNotes.remove(note)
            }
            else{
                selectedNotes.add(note)
            }
            notifyItemChanged(adapterPosition)
            onSelectCountChange(selectedNotes.size)
        }

        fun bind(note: Notes){
            itemBinding.noteTitleText.text =note.noteTitle
            itemBinding.noteDescriptionText.text = note.noteDescription
            itemView.background =
                if(selectedNotes.contains(note)){
                    itemView.context.getDrawable(R.drawable.note_selected_border)
                }
                else{
                    itemView.context.getDrawable(R.drawable.note_default_border)
                }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Notes>() {
        override fun areItemsTheSame(oldItem: Notes, newItem: Notes): Boolean {
            return oldItem.noteId == newItem.noteId
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

        holder.bind(currentNote)

        holder.itemView.setOnClickListener {
            holder.itemView.findNavController().navigate(
                DashboardFragmentDirections.actionDashboardFragmentToEditNoteFragment(
                    currentNote
                )
            )
        }
    }

}