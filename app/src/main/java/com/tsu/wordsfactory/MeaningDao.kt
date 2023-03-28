package com.tsu.wordsfactory

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MeaningDao {
    @Insert
    fun insertMeaning(meaning: MeaningEntity)

    @Query("SELECT * FROM meaningentity WHERE word LIKE :word")
    fun findMeanings(word: String): List<MeaningEntity>
}