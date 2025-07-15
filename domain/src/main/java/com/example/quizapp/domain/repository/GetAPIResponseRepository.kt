package com.example.quizapp.domain.repository

import com.example.quizapp.domain.Question

interface GetAPIResponseRepository {
    suspend fun getQuestions(): List<Question>
}