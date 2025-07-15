package com.example.quizapp.domain.useCases

import com.example.quizapp.domain.Question
import com.example.quizapp.domain.repository.GetAPIResponseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetListOfQuestionsUseCase(
    private val getAPIResponseRepository: GetAPIResponseRepository
) {
    suspend operator fun invoke(): List<Question> = withContext(Dispatchers.IO){
        getAPIResponseRepository.getQuestions()
    }
}