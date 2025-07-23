package com.example.quizapp.data.repositoryImpl

import com.example.quizapp.data.entity.QuizResultDao
import com.example.quizapp.data.entity.toEntities
import com.example.quizapp.data.entity.toQuizResult
import com.example.quizapp.domain.model.QuizResult
import com.example.quizapp.domain.repository.QuizResultRepository

class QuizResultRepositoryImpl(
    private val quizResultDao: QuizResultDao
) : QuizResultRepository {

    override suspend fun saveQuizResult(result: QuizResult) {
        val (resultEntity, questionEntities) = result.toEntities()
        quizResultDao.insertCompleteQuiz(resultEntity, questionEntities)
    }

    override suspend fun getQuizResult(category: String): QuizResult? {
        val result = quizResultDao.getQuizResult(category) ?: return null
        val questions = quizResultDao.getQuestionAttempts(category)
        return toQuizResult(result, questions)
    }

    override suspend fun doesCategoryExist(category: String): Boolean = quizResultDao.quizCategoryExists(category)
}