package com.example.quizapp.domain.useCases

import com.example.quizapp.domain.QuizCategory
import com.example.quizapp.domain.model.QuizResult
import com.example.quizapp.domain.repository.QuizResultRepository

class GetSavedQuestionsFromDBUseCase(
    private val quizResultRepository: QuizResultRepository
) {
    suspend operator fun invoke(category: String): QuizResult? {
        val quizResult = quizResultRepository.getQuizResult(category)
        return quizResult
    }
}