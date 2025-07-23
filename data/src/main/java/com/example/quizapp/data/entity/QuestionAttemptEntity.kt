package com.example.quizapp.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "question_attempts",
    foreignKeys = [ForeignKey(
        entity = QuizResultEntity::class,
        parentColumns = ["category"],
        childColumns = ["category"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("category")]
)
data class QuestionAttemptEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String, // Foreign key reference
    val questionText: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val selectedAnswerIndex: Int
)