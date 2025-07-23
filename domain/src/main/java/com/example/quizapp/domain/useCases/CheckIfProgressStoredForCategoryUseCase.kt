package com.example.quizapp.domain.useCases

import com.example.quizapp.domain.repository.QuizResultRepository

class CheckIfProgressStoredForCategoryUseCase(
    private val quizResultRepository: QuizResultRepository
) {
    suspend operator fun invoke(category: String): Boolean {
        return quizResultRepository.doesCategoryExist(category)
    }
}