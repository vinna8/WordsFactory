package com.tsu.wordsfactory

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WordEntity (
    @PrimaryKey val word: String,
    val phonetic: String,
    val audio: String,
    val partOfSpeech: String,
)