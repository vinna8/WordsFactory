package com.tsu.wordsfactory

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MeaningEntity (
    @PrimaryKey(autoGenerate = true) val Id: Int = 0,
    val word: String,
    val definition: String,
    val example: String
)