package com.example.notespace.repository

import com.example.notespace.database.NotesDatabase
import com.example.notespace.model.Notes

class NotesRepository(private val db:NotesDatabase) {

    suspend fun insertNote(note: Notes) = db.getNoteDao().insertNote(note)

    suspend fun updateNote(note: Notes) = db.getNoteDao().updateNote(note)

    suspend fun deleteNote(note: Notes) = db.getNoteDao().deleteNote(note)

    fun getAllNotes() = db.getNoteDao().getAllNotes()

    fun searchNote(keySearch :String?) = db.getNoteDao().searchNote(keySearch)
}