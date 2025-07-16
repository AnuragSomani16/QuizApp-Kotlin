package com.example.quizapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.example.quizapp.viewModels.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : FragmentActivity() {

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        // Show loader initially
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, LoaderFragment())
            .commit()

        mainViewModel.fetchQuestions()

        mainViewModel.isLoading.observe(this) {
            if (!it) showQuizScreen()
        }

        mainViewModel.triggerToast.observe(this) { boolean ->
            if(boolean) {
                Toast.makeText(
                    this,
                    "Oops! No questions found, closing the app",
                    Toast.LENGTH_SHORT
                ).show()
                lifecycleScope.launch {
                    delay(5000)
                    finishAffinity()
                }
            }

        }

        mainViewModel.isQuizCompleted.observe(this) { completed ->
            if (completed) {
                showResultScreen()
            }
        }
    }

    private fun showQuizScreen() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right, // enter
                R.anim.slide_out_left  // exit
            )
            .replace(R.id.fragment_container, QuestionsFragment())
            .commit()
    }

    private fun showResultScreen() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            .replace(R.id.fragment_container, ResultFragment())
            .commit()
    }
}