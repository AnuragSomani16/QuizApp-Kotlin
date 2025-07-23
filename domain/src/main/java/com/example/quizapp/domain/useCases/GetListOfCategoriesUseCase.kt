package com.example.quizapp.domain.useCases

import com.example.quizapp.domain.Question
import com.example.quizapp.domain.QuizCategory
import com.example.quizapp.domain.repository.GetAPIResponseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

private const val END_POINT = "https://gist.githubusercontent.com/dr-samrat/ee986f16da9d8303c1acfd364ece22c5/raw"

class GetListOfCategoriesUseCase(
    private val getAPIResponseRepository: GetAPIResponseRepository
) {
    suspend operator fun invoke(): List<QuizCategory> = withContext(Dispatchers.IO) {
        delay(1000) // Simulate network delay for progress bar
        val categories = getAPIResponseRepository.getQuizCategories(END_POINT)
        categories
    }
}