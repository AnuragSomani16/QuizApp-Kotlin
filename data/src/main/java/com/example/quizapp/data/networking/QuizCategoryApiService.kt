package com.example.quizapp.data.networking

import com.example.quizapp.domain.Question
import com.example.quizapp.domain.QuizCategory
import retrofit2.http.GET

interface QuizCategoryApiService {
    @GET("raw")
    suspend fun getQuizCategory(): List<QuizCategory>
}