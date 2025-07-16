package com.example.quizapp.data.di

import com.example.quizapp.data.networking.RetrofitClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module

val networkingModule = module {
    factory { RetrofitClient(get()) }
    single { HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY } }
}