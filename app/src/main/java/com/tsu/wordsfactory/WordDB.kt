package com.tsu.wordsfactory

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WordEntity::class, MeaningEntity::class], version = 2)
abstract class WordDB: RoomDatabase() {
    abstract fun wordDao(): WordDao
    abstract fun meaningDao(): MeaningDao
}