package com.example.notespace.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
            Log.d("NotesViewModel", "Attempting to delete note with ID: $noteId")
            notesRepository.deleteNoteById(noteId)
            Log.d("NotesViewModel", "Note deleted with ID: $noteId")
        }

        fun getAllNotes(): LiveData<List<Notes>>
        {
            return notesRepository.getAllNotes()
        }

        fun searchNote(keySearch : String?) = notesRepository.searchNote(keySearch)

}