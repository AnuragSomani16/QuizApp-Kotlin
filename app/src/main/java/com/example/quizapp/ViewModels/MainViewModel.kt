package com.example.quizapp.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _questions = MutableLiveData<List<QuizQuestion>>()
    val questions: LiveData<List<QuizQuestion>> = _questions

    private val _currentIndex = MutableLiveData(0)
    val currentIndex: LiveData<Int> = _currentIndex

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _correctAnswers = MutableLiveData(0)
    val correctAnswers: LiveData<Int> = _correctAnswers

    fun fetchQuestions() {
        viewModelScope.launch {
            _isLoading.value = true
            val response = RetrofitClient.api.getQuestions() // assume this works
            _questions.value = response
            _isLoading.value = false
        }
    }

    fun submitAnswer(selectedIndex: Int) {
        val currentQuestion = _questions.value?.get(_currentIndex.value ?: 0)
        if (currentQuestion?.correctOptionIndex == selectedIndex) {
            _correctAnswers.value = _correctAnswers.value?.plus(1)
        }

        // delay 2 seconds before moving to next
        viewModelScope.launch {
            delay(2000)
            goToNext()
        }
    }

    fun goToNext() {
        val nextIndex = (_currentIndex.value ?: 0) + 1
        if (nextIndex < (_questions.value?.size ?: 0)) {
            _currentIndex.value = nextIndex
        } else {
            // Quiz over, trigger navigation to result
        }
    }

    fun goToPrevious() {
        val prev = (_currentIndex.value ?: 0) - 1
        if (prev >= 0) _currentIndex.value = prev
    }

    fun skipQuestion() {
        goToNext()
    }
}