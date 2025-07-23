package com.example.quizapp.domain.model

data class QuizResult(
    val category: String,
    val score: Int = 0,
    val questions: List<QuestionAttempt>
)

data class QuestionAttempt(
    val questionText: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val selectedAnswerIndex: Int
)