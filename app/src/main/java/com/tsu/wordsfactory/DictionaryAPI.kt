package com.tsu.wordsfactory

import retrofit2.http.GET
import retrofit2.http.Path

interface DictionaryAPI {
    @GET("{word}")
    suspend fun getWord(@Path("word") word: String): ArrayList<Word>
}