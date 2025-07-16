package com.example.quizapp.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.domain.Question
import com.example.quizapp.domain.useCases.GetListOfQuestionsUseCase
import kotlinx.coroutines.launch

class MainViewModel(
    private val getListOfQuestionsUseCase: GetListOfQuestionsUseCase
) : ViewModel() {

    val highestStreak = MutableLiveData(0)

    private var skippedQuestions = 0
    private var currentStreak = 0

    private val _questions = MutableLiveData<List<Question>>()
    val questions: LiveData<List<Question>> = _questions

    private val _triggerToast = MutableLiveData<Boolean>()
    val triggerToast: LiveData<Boolean> = _triggerToast

    private var _currentIndex = MutableLiveData(0)
    val currentIndex: LiveData<Int> = _currentIndex

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _correctAnswers = MutableLiveData(0)
    val correctAnswers: LiveData<Int> = _correctAnswers

    private val _isQuizCompleted = MutableLiveData(false)
    val isQuizCompleted: LiveData<Boolean> = _isQuizCompleted

    fun fetchQuestions() {
        viewModelScope.launch {
            _isLoading.value = true
            val response = getListOfQuestionsUseCase() // assume this works
            Log.i("cTAG", "fetchQuestions: $response")

            if (response.isNotEmpty()) {
                _questions.value = response
                _isLoading.value = false
            }
            else _triggerToast.value = true

        }
    }

    fun submitAnswer(selectedIndex: Int) {
        val currentQuestion = _questions.value?.get(_currentIndex.value ?: 0)
        if (currentQuestion?.correctOptionIndex == selectedIndex) {
            _correctAnswers.value = _correctAnswers.value?.plus(1)
        }
        goToNext()
    }

    private fun goToNext() {
        val nextIndex = (_currentIndex.value ?: 0) + 1
        if (nextIndex < (_questions.value?.size ?: 0)) {
            _currentIndex.value = nextIndex
        } else {
            // Quiz is complete
            _isQuizCompleted.value = true
        }
    }

    fun goToPrevious() {
        val prev = (_currentIndex.value ?: 0) - 1
        if (prev >= 0) _currentIndex.value = prev
    }

    fun skipQuestion() {
        goToNext()
    }

    fun incrementSkippedQuestionsCount(){
        skippedQuestions+=1
    }

    fun getSkippedQuestionsCount(): Int {
        return skippedQuestions
    }

    fun incrementCurrentStreak(){
        currentStreak+=1
    }

    fun getCurrentStreak(): Int {
        return currentStreak
    }

    fun reset() {
        skippedQuestions = 0
        currentStreak = 0
        _questions.value = emptyList()
        _triggerToast.value = false
        _currentIndex.value = 0
        _isLoading.value = true
        _correctAnswers.value = 0
        _isQuizCompleted.value = false
    }
}