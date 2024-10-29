package com.example.notespace.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.notespace.model.Notes

@Database(entities = [Notes::class], version = 2, exportSchema = false)
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

        val migrationFrom1To2 = object : Migration(1,2){
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE notes ADD COLUMN trashed INTEGER NOT NULL DEFAULT 0")
            }
        }

        private fun createDatabse(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                NotesDatabase::class.java,
                "note_db"
            )
                .addMigrations(migrationFrom1To2)
                .build()

    }



}