package com.example.notespace.di

import android.content.Context
import androidx.room.Room
import com.example.notespace.database.NotesDao
import com.example.notespace.database.NotesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) : NotesDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            NotesDatabase::class.java,
            "notesdb"
        ).build()
    }

    @Singleton
    @Provides
    fun provideNotesDao(database: NotesDatabase): NotesDao {
        return database.getNoteDao()
    }
}