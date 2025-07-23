package com.example.quizapp.data.entity

import com.example.quizapp.domain.model.QuestionAttempt
import com.example.quizapp.domain.model.QuizResult

fun QuizResult.toEntities(): Pair<QuizResultEntity, List<QuestionAttemptEntity>> {
    val resultEntity = QuizResultEntity(category, score)
    val questionEntities = questions.map {
        QuestionAttemptEntity(
            category = category,
            questionText = it.questionText,
            options = it.options,
            correctAnswerIndex = it.correctAnswerIndex,
            selectedAnswerIndex = it.selectedAnswerIndex
        )
    }
    return resultEntity to questionEntities
}

fun toQuizResult(
    resultEntity: QuizResultEntity,
    questionEntities: List<QuestionAttemptEntity>
): QuizResult {
    return QuizResult(
        category = resultEntity.category,
        score = resultEntity.score,
        questions = questionEntities.map {
            QuestionAttempt(
                questionText = it.questionText,
                options = it.options,
                correctAnswerIndex = it.correctAnswerIndex,
                selectedAnswerIndex = it.selectedAnswerIndex
            )
        }
    )
}