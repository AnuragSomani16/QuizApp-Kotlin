package com.example.quizapp.domain.useCases

import com.example.quizapp.domain.Question
import com.example.quizapp.domain.repository.GetAPIResponseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

private const val END_POINT = "https://gist.githubusercontent.com/dr-samrat/53846277a8fcb034e482906ccc0d12b2/"

class GetListOfQuestionsUseCase(
    private val getAPIResponseRepository: GetAPIResponseRepository
) {
    suspend operator fun invoke(): List<Question> = withContext(Dispatchers.IO){
        delay(1000)
        getAPIResponseRepository.getQuestions(END_POINT)
    }
}