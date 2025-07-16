package com.example.quizapp.di

import com.example.quizapp.data.di.networkingModule
import com.example.quizapp.data.di.repositoryModule
import com.example.quizapp.domain.di.interactionModule

object KoinModules {
    val networkingModules = networkingModule
    val repositoryModules = repositoryModule
    val dataModules = listOf(networkingModules, repositoryModules)
    val domainModules = interactionModule
}