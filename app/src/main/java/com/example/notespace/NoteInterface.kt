package com.example.notespace

import com.example.notespace.model.Notes

interface NoteInterface {
    fun getSelectedNoteIds(): List<Long>
    fun deleteSelectedNotes()
}