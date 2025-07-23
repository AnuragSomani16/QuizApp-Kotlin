package com.example.quizapp.data.entity

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface QuizResultDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizResult(quiz: QuizResultEntity)

    @Insert
    suspend fun insertQuestionAttempts(questions: List<QuestionAttemptEntity>)

    @Transaction
    suspend fun insertCompleteQuiz(result: QuizResultEntity, questions: List<QuestionAttemptEntity>) {
        insertQuizResult(result)
        insertQuestionAttempts(questions)
    }

    @Query("SELECT * FROM quiz_results WHERE category = :category")
    suspend fun getQuizResult(category: String): QuizResultEntity?

    @Query("SELECT * FROM question_attempts WHERE category = :category")
    suspend fun getQuestionAttempts(category: String): List<QuestionAttemptEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM quiz_results WHERE category = :categoryName)")
    suspend fun quizCategoryExists(categoryName: String): Boolean
}