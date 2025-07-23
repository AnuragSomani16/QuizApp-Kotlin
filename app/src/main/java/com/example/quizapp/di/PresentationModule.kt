package com.example.quizapp.di

import com.example.quizapp.viewModels.MainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
     viewModel{
        MainViewModel(
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
}