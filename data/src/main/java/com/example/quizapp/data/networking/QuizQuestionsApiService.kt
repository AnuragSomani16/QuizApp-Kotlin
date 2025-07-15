package com.example.quizapp.data.networking

import com.example.quizapp.domain.Question
import retrofit2.http.GET
import retrofit2.http.Url

interface QuizQuestionsApiService {
    @GET
    suspend fun getQuestions(
        @Url apiUrl: String = "https://gist.githubusercontent.com/dr-samrat/53846277a8fcb034e482906ccc0d12b2/raw",
    ): List<Question>
}