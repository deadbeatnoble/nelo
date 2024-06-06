package com.example.nelo.data.history

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HistoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addHistory(historyEntity: HistoryEntity)

    @Query("SELECT * FROM history ORDER BY id DESC")
    fun getAllHistory(): LiveData<List<HistoryEntity>>

    @Query("DELETE FROM history WHERE mangaChapterUrl = :mangaChapterUrl")
    suspend fun deleteHistory(mangaChapterUrl: String)

    @Query("DELETE FROM history")
    suspend fun clearHistory()

    @Query("SELECT EXISTS (SELECT * FROM history WHERE mangaChapterUrl = :mangaChapterUrl)")
    suspend fun existHistory(mangaChapterUrl: String): Boolean

}