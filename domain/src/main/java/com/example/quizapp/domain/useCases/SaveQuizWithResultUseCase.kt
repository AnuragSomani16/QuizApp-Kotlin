package com.example.quizapp.domain.useCases

import com.example.quizapp.domain.model.QuestionAttempt
import com.example.quizapp.domain.model.QuizResult
import com.example.quizapp.domain.repository.QuizResultRepository

class SaveQuizWithResultUseCase(
    private val quizResultRepository: QuizResultRepository
) {
    suspend operator fun invoke(category: String, score: Int, listOfQuestions: List<QuestionAttempt>) {
        quizResultRepository.saveQuizResult(
            QuizResult(
                category, score, listOfQuestions
            )
        )
    }
}