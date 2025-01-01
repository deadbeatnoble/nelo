package com.example.nelo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "library")
data class LibraryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val mangaTitle: String,
    val mangaThumbnail: String,
    val mangaUrl: String
)
