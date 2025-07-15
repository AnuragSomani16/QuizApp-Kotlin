package com.example.quizapp.data.di

import com.example.quizapp.data.networking.RetrofitClient
import org.koin.dsl.module

val networkingModule = module {
    factory { RetrofitClient() }
}