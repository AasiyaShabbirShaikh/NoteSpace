package com.example.notespace.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.joinIntoString
import com.example.notespace.model.Notes
import com.example.notespace.repository.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(private val notesRepository: NotesRepository): ViewModel() {

        suspend fun addNote(note: Notes) : Long {
            return notesRepository.insertNote(note)
        }

        fun deleteNote(note:Notes) = viewModelScope.launch {
            notesRepository.deleteNote(note)
        }

        fun updateNote(note:Notes) = viewModelScope.launch {
            notesRepository.updateNote(note)
        }

        fun deleteNoteById(noteId: Long) = viewModelScope.launch {
            notesRepository.deleteNoteById(noteId)
        }

        fun getAllNotes(): LiveData<List<Notes>>
        {
            return notesRepository.getAllNotes()
        }

//        fun moveNoteToTrash(noteId: Long) = viewModelScope.launch {
//            notesRepository.moveToTrash(noteId)
//        }

        fun moveToTrash(noteIds: List<Long>) = viewModelScope.launch {
                notesRepository.moveToTrash(noteIds)
        }

        fun searchNote(keySearch : String?) = notesRepository.searchNote(keySearch)

    fun getNonTrashNotes(): LiveData<List<Notes>>
    {
        return notesRepository.getNonTrashNotes()
    }
}