package com.example.quizapp.domain.repository

import com.example.quizapp.domain.Question
import com.example.quizapp.domain.QuizCategory

interface GetAPIResponseRepository {
    suspend fun getQuestions(urlEndPoint: String): List<Question>
    suspend fun getQuizCategories(urlEndPoint: String): List<QuizCategory>
}