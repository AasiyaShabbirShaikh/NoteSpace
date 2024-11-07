package com.example.notespace.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.notespace.model.Notes

@Dao
interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(notes:Notes) : Long

    @Update
    suspend fun updateNote(notes:Notes)


    //delete
    @Delete
    suspend fun deleteNote(notes: Notes)

    @Query("DELETE FROM notes WHERE noteId = :noteId")
    suspend fun deleteNoteById(noteId: Long)

    //
    @Query("SELECT * FROM NOTES ORDER BY noteId DESC")
    fun getAllNotes(): LiveData<List<Notes>>

    //trash
    @Query("UPDATE notes SET trashed = 1 where noteId IN (:noteIds)")
    suspend fun updateNotesAsTrashed(noteIds: List<Long>)

    //search
    @Query("SELECT * FROM notes WHERE noteTitle LIKE:searchKey OR noteDescription LIKE:searchKey")
    fun searchNote(searchKey:String?): LiveData<List<Notes>>


    @Query("SELECT * FROM notes WHERE trashed = 0 ORDER BY noteId DESC")
    fun getNonTrashNotes() : LiveData<List<Notes>>

    @Query("DELETE FROM notes WHERE noteId IN (:noteIds)")
    suspend fun deleteNoteByIds(noteIds: List<Long>)
}