package com.example.quizapp.data.repositoryImpl

import com.example.quizapp.data.networking.RetrofitClient
import com.example.quizapp.domain.repository.GetAPIResponseRepository
import com.example.quizapp.domain.Question

class GetAPIResponseRepositoryImpl(
    private val retrofitClient: RetrofitClient
): GetAPIResponseRepository {
    override suspend fun getQuestions(): List<Question> {
        return retrofitClient.getQuizQuestionsApiService().getQuestions()
    }
}