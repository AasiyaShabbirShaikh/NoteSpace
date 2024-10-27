package com.example.notespace.repository

import com.example.notespace.database.NotesDao
import com.example.notespace.model.Notes
import javax.inject.Inject

class NotesRepository @Inject constructor(private val notesDao: NotesDao) {

    suspend fun insertNote(note: Notes) : Long = notesDao.insertNote(note)

    suspend fun updateNote(note: Notes) = notesDao.updateNote(note)

    suspend fun deleteNote(note: Notes) = notesDao.deleteNote(note)

    suspend fun deleteNoteById(noteId: Long) = notesDao.deleteNoteById(noteId)

    fun getAllNotes() = notesDao.getAllNotes()

    fun searchNote(keySearch :String?) = notesDao.searchNote(keySearch)
}