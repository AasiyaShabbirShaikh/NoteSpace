package com.example.notespace.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notespace.model.Notes

@Database(entities = [Notes::class], version = 1)
abstract class NotesDatabase: RoomDatabase() {

    abstract fun getNoteDao():NotesDao

    companion object{
        @Volatile
        private var instance:NotesDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context:Context) = instance ?:
        synchronized(LOCK){
            instance ?:
            createDatabse(context).also{
                instance =it
            }
        }

        private fun createDatabse(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                NotesDatabase::class.java,
                "note_db"
            ).build()

    }



}