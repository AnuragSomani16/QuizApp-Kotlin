package com.example.quizapp.data.entity

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [QuizResultEntity::class, QuestionAttemptEntity::class],
    version = 1
)
@TypeConverters(ListTypeConverter::class)
abstract class QuizDatabase : RoomDatabase() {
    abstract fun quizResultDao(): QuizResultDao
}