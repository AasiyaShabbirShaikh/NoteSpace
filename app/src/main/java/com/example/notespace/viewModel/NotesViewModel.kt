package com.example.notespace.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.notespace.model.Notes
import com.example.notespace.repository.NotesRepository
import kotlinx.coroutines.launch

class NotesViewModel(
    application: Application, private val noteRepository: NotesRepository): AndroidViewModel(application) {

        fun addNote(note:Notes) =
            viewModelScope.launch {
            noteRepository.insertNote(note)
        }

        fun deleteNote(note:Notes) =
            viewModelScope.launch {
            noteRepository.deleteNote(note)
        }

        fun updateNote(note:Notes) =
            viewModelScope.launch {
            noteRepository.updateNote(note)
        }

        fun getAllNotes() = noteRepository.getAllNotes()

        fun searchNote(keySearch : String?) = noteRepository.searchNote(keySearch)

}