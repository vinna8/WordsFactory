package com.tsu.wordsfactory

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WordEntity::class], version = 1)
abstract class WordDB: RoomDatabase() {
    abstract fun wordDao(): WordDao
}