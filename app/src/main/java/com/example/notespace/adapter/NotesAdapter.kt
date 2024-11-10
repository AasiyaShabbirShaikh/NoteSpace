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
import com.example.notespace.viewModel.NotesViewModel

class NotesAdapter(
    private val onNoteLongClick: (Notes) -> Unit,
    private val onNoteClick: (Notes) -> Unit,
    private val onSelectCountChange: (Int) -> Unit
//    private val notesViewModel: NotesViewModel
) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private val selectedNotes = mutableSetOf<Notes>()
    var isSelectionModeOn = false

    inner class NoteViewHolder(val itemBinding: NoteItemLayoutBinding, ) : RecyclerView.ViewHolder(itemBinding.root){
        init {
            itemView.setOnLongClickListener {
                val note = differ.currentList[adapterPosition]
//                handleNoteSelection()
                toggleSelection(note)
                isSelectionModeOn = true
                onNoteLongClick(note)
                true
            }

            itemView.setOnClickListener {
                val note = differ.currentList[adapterPosition]
                if(isSelectionModeOn){
//                    val note = differ.currentList[adapterPosition]
                    toggleSelection(note)
                }
                else{
                    itemView.findNavController().navigate(
                        DashboardFragmentDirections.actionDashboardFragmentToEditNoteFragment(note)
                    )
                }
            }
        }

         fun toggleSelection(note:Notes){
             println("notes , ${note}")
            if(selectedNotes.contains(note)){
                selectedNotes.remove(note)
            }
            else{
                selectedNotes.add(note)
            }
            isSelectionModeOn = selectedNotes.isNotEmpty()
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


    fun clearNoteSelection(){
        selectedNotes.clear()
        isSelectionModeOn = false
        notifyDataSetChanged()
//        onSelectCountChange(0)
        if (selectedNotes.isNotEmpty()) {
            onSelectCountChange(selectedNotes.size)
        }
    }

    fun getSelectedNoteIds(): List<Long> {
        return selectedNotes.map {it.noteId}
    }

    fun deleteSelectedNote() {
        if (selectedNotes.isNotEmpty()) {
            val notesToDelete = selectedNotes.toList()
//            notesViewModel.moveToTrash(notesToDelete.map { it.noteId }) // Move notes to trash.

            val currentNotesList = differ.currentList.toMutableList()
            currentNotesList.removeAll(notesToDelete)
            differ.submitList(currentNotesList)

            selectedNotes.clear()
            isSelectionModeOn = false
            onSelectCountChange(0)
        }
    }


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

//        holder.itemView.setOnClickListener {
//            holder.itemView.findNavController().navigate(
//                DashboardFragmentDirections.actionDashboardFragmentToEditNoteFragment(
//                    currentNote
//                )
//            )
//        }
    }



}