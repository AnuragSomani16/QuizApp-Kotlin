package com.example.quizapp.data.networking

import com.example.quizapp.domain.Question
import retrofit2.http.GET
import retrofit2.http.Url

interface QuizQuestionsApiService {
    @GET("raw")
    suspend fun getQuestions(): List<Question>
}