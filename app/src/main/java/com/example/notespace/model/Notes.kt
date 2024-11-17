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
    var noteTitle : String,
    var noteDescription: String,
    var timestamp: Long,
    var trashed: Boolean = false,
    var noteImageUri : String? =null,
    var reminderTime : Long?
):Parcelable