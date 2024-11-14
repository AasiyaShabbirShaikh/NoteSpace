package com.example.notespace.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "notes")
@Parcelize
data class Notes(
    @PrimaryKey(autoGenerate = true)
    val noteId: Long,
    val noteTitle : String,
    val noteDescription: String,
    val timestamp: Long,
    val trashed: Boolean = false,
    val noteImageUri : String? =null,
    val reminderTime : Long?
):Parcelable