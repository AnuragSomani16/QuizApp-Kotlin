package com.example.quizapp.data.repositoryImpl

import android.util.Log
import com.example.quizapp.data.networking.RetrofitClient
import com.example.quizapp.domain.repository.GetAPIResponseRepository
import com.example.quizapp.domain.Question
import com.example.quizapp.domain.QuizCategory

class GetAPIResponseRepositoryImpl(
    private val retrofitClient: RetrofitClient
): GetAPIResponseRepository {
    override suspend fun getQuestions(urlEndPoint: String): List<Question> {
        try {
            return retrofitClient.getQuizQuestionsApiService(urlEndPoint).getQuestions()
        }
        catch (e : Exception){
            Log.e("GetAPIResponseRepositoryImpl", "Error while getting getQuestions: ${e.message}")
            return emptyList()
        }
    }

    override suspend fun getQuizCategories(urlEndPoint: String): List<QuizCategory> {
        try {
            return retrofitClient.getQuizCategoryApiService(urlEndPoint).getQuizCategory()
        }
        catch (e : Exception){
            Log.e("GetAPIResponseRepositoryImpl", "Error while getting getQuestions: ${e.message}")
            return emptyList()
        }
    }
}