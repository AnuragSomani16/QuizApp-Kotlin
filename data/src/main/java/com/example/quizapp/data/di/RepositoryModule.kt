package com.example.quizapp.data.di

import com.example.quizapp.data.repositoryImpl.GetAPIResponseRepositoryImpl
import com.example.quizapp.data.repositoryImpl.QuizResultRepositoryImpl
import com.example.quizapp.domain.repository.GetAPIResponseRepository
import com.example.quizapp.domain.repository.QuizResultRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<GetAPIResponseRepository> { GetAPIResponseRepositoryImpl(get()) }
    factory<QuizResultRepository> { QuizResultRepositoryImpl(get()) }
}