package com.tsu.wordsfactory

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WordDao {
    @Insert
    fun insertWord(word: WordEntity)
    @Query("SELECT * FROM wordentity WHERE word LIKE :word")
    fun findWord(word: String): WordEntity
}