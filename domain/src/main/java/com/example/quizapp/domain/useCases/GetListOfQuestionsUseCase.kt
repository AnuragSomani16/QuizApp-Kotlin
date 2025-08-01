package com.example.quizapp.domain.useCases

import com.example.quizapp.domain.Question
import com.example.quizapp.domain.model.QuestionAttempt
import com.example.quizapp.domain.model.QuizResult
import com.example.quizapp.domain.repository.GetAPIResponseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

private const val END_POINT = "https://gist.githubusercontent.com/dr-samrat/53846277a8fcb034e482906ccc0d12b2/"

class GetListOfQuestionsUseCase(
    private val getAPIResponseRepository: GetAPIResponseRepository
) {
    suspend operator fun invoke(endPoint: String, category: String): QuizResult = withContext(Dispatchers.IO) {
        delay(3000) // Simulate network delay for progress bar

        val originalQuestions = getAPIResponseRepository.getQuestions(endPoint)

        val shuffledQuestions = originalQuestions.map { question ->
            // Pair each option with its original index
            val indexedOptions = question.options.mapIndexed { index, option -> index to option }

            // Shuffle the options
            val shuffled = indexedOptions.shuffled()

            // Find the new index of the original correct option
            val newCorrectIndex = shuffled.indexOfFirst { it.first == question.correctOptionIndex }

            // Extract just the shuffled options
            val newOptions = shuffled.map { it.second }

            // Return a new Question with updated options and correct index
            question.copy(
                options = newOptions,
                correctOptionIndex = newCorrectIndex
            )
        }.shuffled() // Finally shuffle the order of questions themselves

        val modifiedQuestions: List<QuestionAttempt> = shuffledQuestions.map { question ->
            QuestionAttempt(
                questionText = question.question,
                options = question.options,
                correctAnswerIndex = question.correctOptionIndex,
                selectedAnswerIndex = -1 // or set if known
            )
        }

        QuizResult(
            category = category,
            questions = modifiedQuestions
        )
    }
}