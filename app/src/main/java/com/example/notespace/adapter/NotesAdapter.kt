package com.example.notespace.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.notespace.R
import com.example.notespace.databinding.ImageItemLayoutBinding
import com.example.notespace.databinding.NoteItemLayoutBinding
import com.example.notespace.model.Notes
import com.example.notespace.ui.DashboardFragmentDirections
import com.example.notespace.viewModel.NotesViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class NotesAdapter(
    private val onNoteLongClick: (Notes) -> Unit,
    private val onNoteClick: (note:Notes) -> Unit ,
    private val onSelectCountChange: (Int) -> Unit,
) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private val selectedNotes = mutableSetOf<Notes>()

    inner class NoteViewHolder(val itemBinding: NoteItemLayoutBinding) : RecyclerView.ViewHolder(itemBinding.root){

        fun bind(note: Notes){
            itemBinding.noteTitleText.text =note.noteTitle
            itemBinding.noteDescriptionText.text = note.noteDescription

            if(!note.noteImageUri.isNullOrEmpty()){
                itemBinding.photoImage.visibility = View.VISIBLE
                itemBinding.photoImage.setImageURI(Uri.parse(note.noteImageUri))
            }
            else{
                itemBinding.photoImage.visibility = View.GONE
            }

            if (note.reminderTime != null) {
                itemBinding.itemReminderCardview.visibility = View.VISIBLE
                itemBinding.itemDayText.text = formatReminderDate(note.reminderTime!!)
                itemBinding.itemTimeText.text = formatReminderTime(note.reminderTime!!)
            } else {
                itemBinding.itemReminderCardview.visibility = View.GONE
            }

            itemView.background =
                if(selectedNotes.contains(note)){
                    itemView.context.getDrawable(R.drawable.note_selected_border)
                }
                else{
                    itemView.context.getDrawable(R.drawable.note_default_border)
                }
        }
    }

    private fun formatReminderTime(timeInMillis: Long): String {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val date = Date(timeInMillis)
        return sdf.format(date)
    }

    private fun formatReminderDate(timeInMillis: Long): String {
        val currentDate = Calendar.getInstance()
        val reminderDate = Calendar.getInstance()
        reminderDate.timeInMillis = timeInMillis

        return when {
            isToday(reminderDate, currentDate) -> {
                "Today,"
            }
            isTomorrow(reminderDate, currentDate) -> {
                "Tomorrow,"
            }
            else -> {
                val dateFormat = SimpleDateFormat("MMM dd,", Locale.getDefault())
                dateFormat.format(reminderDate.time)
            }
        }
    }

    private fun isToday(reminderDate: Calendar, currentDate: Calendar): Boolean {
        return reminderDate.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR) &&
                reminderDate.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH) &&
                reminderDate.get(Calendar.DAY_OF_MONTH) == currentDate.get(Calendar.DAY_OF_MONTH)
    }

    private fun isTomorrow(reminderDate: Calendar, currentDate: Calendar): Boolean {
        currentDate.add(Calendar.DAY_OF_YEAR, 1)
        return isToday(reminderDate, currentDate)
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
        val binding = NoteItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return (differ.currentList.size)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = differ.currentList[position]
        holder.bind(currentNote)

    }



}