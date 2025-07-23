package com.example.quizapp.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.domain.Question
import com.example.quizapp.domain.QuizCategory
import com.example.quizapp.domain.model.QuestionAttempt
import com.example.quizapp.domain.model.QuizResult
import com.example.quizapp.domain.useCases.CheckIfProgressStoredForCategoryUseCase
import com.example.quizapp.domain.useCases.GetListOfCategoriesUseCase
import com.example.quizapp.domain.useCases.GetListOfQuestionsUseCase
import com.example.quizapp.domain.useCases.GetSavedQuestionsFromDBUseCase
import com.example.quizapp.domain.useCases.SaveQuizWithResultUseCase
import kotlinx.coroutines.launch

class MainViewModel(
    private val getListOfQuestionsUseCase: GetListOfQuestionsUseCase,
    private val getListOfCategoriesUseCase: GetListOfCategoriesUseCase,
    private val checkIfProgressStoredForCategoryUseCase: CheckIfProgressStoredForCategoryUseCase,
    private val getSavedQuestionsFromDBUseCase: GetSavedQuestionsFromDBUseCase,
    private val saveQuizWithResultUseCase: SaveQuizWithResultUseCase
) : ViewModel() {

    val highestStreak = MutableLiveData(0)

    private var skippedQuestions = 0
    private var currentStreak = 0
    private var wrongAnswers = 0

    private val _questions = MutableLiveData<QuizResult>()
    val questions: LiveData<QuizResult> = _questions

    private val _categories = MutableLiveData<List<QuizCategory>>()
    val categories: LiveData<List<QuizCategory>> = _categories

    private val _triggerToast = MutableLiveData<Boolean>()
    val triggerToast: LiveData<Boolean> = _triggerToast

    private var _currentIndex = MutableLiveData(0)
    val currentIndex: LiveData<Int> = _currentIndex

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isQuestionsLoading = MutableLiveData(true)
    val isQuestionsLoading: LiveData<Boolean> = _isQuestionsLoading

    private val _isQuestionsFromDBLoading = MutableLiveData(true)
    val isQuestionsFromDBLoading: LiveData<Boolean> = _isQuestionsFromDBLoading

    private val _correctAnswers = MutableLiveData(0)
    val correctAnswers: LiveData<Int> = _correctAnswers

    private val _isQuizCompleted = MutableLiveData(false)
    val isQuizCompleted: LiveData<Boolean> = _isQuizCompleted

    private val listToSave: MutableList<QuestionAttempt> = mutableListOf()

    lateinit var currentCategory: QuizCategory

    fun fetchQuestions() {
        viewModelScope.launch {
            _isQuestionsLoading.value = true
            val response = getListOfQuestionsUseCase(currentCategory.questionsUrl, currentCategory.title) // assume this works
            Log.i("cTAG", "fetchQuestions: $response")
            _questions.value = response
            _isQuestionsLoading.value = false
        }
    }

    fun fetchCategories() {
        viewModelScope.launch {
            _isLoading.value = true
            val response = getListOfCategoriesUseCase()
            if (response.isNotEmpty()) {
                _categories.value = response
                _isLoading.value = false
            }
            else _triggerToast.value = true
        }
    }

    fun submitAnswer(selectedIndex: Int) {
        val currentQuestion = _questions.value?.questions?.get(_currentIndex.value ?: 0)
        if (currentQuestion?.correctAnswerIndex == selectedIndex) {
            _correctAnswers.value = _correctAnswers.value?.plus(1)
        }
        goToNext()
    }

    private fun goToNext() {
        val nextIndex = (_currentIndex.value ?: 0) + 1
        if (nextIndex < (_questions.value?.questions?.size ?: 0)) {
            _currentIndex.value = nextIndex
        } else {
            // Quiz is complete
            _isQuizCompleted.value = true
        }
    }

    fun incrementSkippedQuestionsCount(){
        skippedQuestions+=1
    }

    fun getSkippedQuestionsCount(): Int {
        return skippedQuestions
    }

    fun incrementWrongAnswersCount(){
        wrongAnswers+=1
    }

    fun getWrongAnswersCount(): Int {
        return wrongAnswers
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
        wrongAnswers = 0
        _triggerToast.value = false
        _currentIndex.value = 0
        _isLoading.value = true
        _correctAnswers.value = 0
        _isQuizCompleted.value = false
    }

    suspend fun isCategoryDataStored(category: String): Boolean{
        return checkIfProgressStoredForCategoryUseCase(category)
    }

    fun fetchCategoryQuestions(category: String) {
        viewModelScope.launch {
            _isQuestionsFromDBLoading.value = true
            val response = getSavedQuestionsFromDBUseCase(category) // assume this works
            Log.i("cTAG", "fetchQuestions: $response")

            response?.let {
                _questions.value = response
                _isQuestionsFromDBLoading.value = false
            }
        }
    }

    fun addResponseToCategory(question: QuestionAttempt, clear: Boolean){
        viewModelScope.launch {
            if(clear){
                saveQuizWithResultUseCase(
                    currentCategory.id,
                    score = _correctAnswers.value ?: 0,
                    listToSave
                )
            }
            listToSave.add(question)
        }
    }
}