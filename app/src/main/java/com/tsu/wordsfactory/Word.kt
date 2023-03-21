package com.tsu.wordsfactory

data class Word (
    val word: String,
    val phonetic: String,
    val phonetics: ArrayList<Phonetics>,
    val meanings: ArrayList<Meanings>
)

data class Phonetics (
    val audio: String
)

data class Meanings (
    val partOfSpeech: String,
    val definitions: ArrayList<Definitions>
)

data class Definitions (
    val definition: String,
    val example: String
)