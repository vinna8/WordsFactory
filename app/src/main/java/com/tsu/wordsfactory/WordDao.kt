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

    @Query("SELECT COUNT(*) FROM wordentity")
    fun getCount(): Int

    @Query("SELECT word FROM wordentity")
    fun getAllWords(): List<String>

    @Query("SELECT * FROM wordentity ORDER BY learningspeed ASC LIMIT 10")
    fun getWords(): List<WordEntity>

    @Query("UPDATE wordentity SET learningspeed = learningspeed + 1 WHERE word = :word")
    fun plusLearningSpeed(word: String)

    @Query("UPDATE wordentity SET learningspeed = learningspeed - 1 WHERE word = :word")
    fun minusLearningSpeed(word: String)
}