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
    suspend fun insertNote(notes:Notes)

    @Update
    suspend fun updateNote(notes:Notes)

    @Delete
    suspend fun deleteNote(notes: Notes)

    @Query("SELECT * FROM NOTES ORDER BY noteId DESC")
    fun getAllNotes(): LiveData<List<Notes>>

    @Query("SELECT * FROM NOTES WHERE noteTitle LIKE:searchKey OR noteDescription LIKE:searchKey")
    fun searchNote(searchKey:String?): LiveData<List<Notes>>


}