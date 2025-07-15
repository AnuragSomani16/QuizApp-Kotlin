package com.example.quizapp.di

import com.example.quizapp.data.di.networkingModule
import com.example.quizapp.data.di.repositoryModule
import com.example.quizapp.domain.di.interactionModule

object KoinModules {
    val dataModules = listOf(networkingModule, repositoryModule)
    val domainModules = listOf(interactionModule)
}