package com.tsu.wordsfactory

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WordEntity (
    @PrimaryKey val word: String,
    val phonetic: String,
    val audio: String,
    val partOfSpeech: String,
    @ColumnInfo(defaultValue = "0") val learningSpeed: Int
)