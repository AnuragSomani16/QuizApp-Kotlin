package com.example.quizapp.data.networking

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient(
    private val httpLoggingInterceptor: HttpLoggingInterceptor
) {

    private fun getOkHttpClient() =
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS).apply {
                addInterceptor(httpLoggingInterceptor)
            }

    fun getQuizQuestionsApiService(baseUrl: String): QuizQuestionsApiService = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkHttpClient().build())
            .build()
            .create(QuizQuestionsApiService::class.java)

    fun getQuizCategoryApiService(baseUrl: String): QuizCategoryApiService = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkHttpClient().build())
            .build()
            .create(QuizCategoryApiService::class.java)
}