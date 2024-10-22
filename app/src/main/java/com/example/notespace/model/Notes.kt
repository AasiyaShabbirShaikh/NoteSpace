package com.example.notespace.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "notes")
@Parcelize
data class Notes(
    @PrimaryKey(autoGenerate = true)
    val noteId: Int,
    val noteTitle : String,
    val noteDescription: String
):Parcelable