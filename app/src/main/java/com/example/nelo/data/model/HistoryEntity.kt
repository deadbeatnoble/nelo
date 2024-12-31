package com.example.nelo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val mangaTitle: String,
    val mangaThumbnail: String,
    val mangaChapter: String,
    val mangaUrl: String,
    val mangaChapterUrl: String
)
