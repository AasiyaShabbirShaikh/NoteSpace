package com.example.notespace.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.notespace.repository.NotesRepository

class NotesViewModelFactory(val application: Application, private val notesRepository: NotesRepository): ViewModelProvider.Factory
{
    override fun <T: ViewModel> create(modelClass: Class<T>): T{
        return NotesViewModel(application, notesRepository) as T
    }
}