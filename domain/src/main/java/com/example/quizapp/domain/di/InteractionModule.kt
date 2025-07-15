package com.example.quizapp.domain.di

import com.example.quizapp.domain.useCases.GetListOfQuestionsUseCase
import org.koin.dsl.module

val interactionModule = module {
    factory { GetListOfQuestionsUseCase(get()) }
}