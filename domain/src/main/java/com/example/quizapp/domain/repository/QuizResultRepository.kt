package com.example.quizapp.domain.repository

import com.example.quizapp.domain.model.QuizResult

interface QuizResultRepository {
    suspend fun saveQuizResult(result: QuizResult)
    suspend fun getQuizResult(category: String): QuizResult?
    suspend fun doesCategoryExist(category: String): Boolean
}