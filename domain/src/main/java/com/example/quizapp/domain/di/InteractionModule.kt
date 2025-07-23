package com.example.quizapp.domain.di

import com.example.quizapp.domain.useCases.CheckIfProgressStoredForCategoryUseCase
import com.example.quizapp.domain.useCases.GetListOfCategoriesUseCase
import com.example.quizapp.domain.useCases.GetListOfQuestionsUseCase
import com.example.quizapp.domain.useCases.GetSavedQuestionsFromDBUseCase
import com.example.quizapp.domain.useCases.SaveQuizWithResultUseCase
import org.koin.dsl.module

val interactionModule = module {
    factory { GetListOfQuestionsUseCase(get()) }
    factory { GetListOfCategoriesUseCase(get()) }
    factory { GetSavedQuestionsFromDBUseCase(get()) }
    factory { SaveQuizWithResultUseCase(get()) }
    factory { CheckIfProgressStoredForCategoryUseCase(get()) }
}