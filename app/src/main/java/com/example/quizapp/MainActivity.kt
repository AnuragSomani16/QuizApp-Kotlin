package com.example.quizapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.FragmentActivity
import com.example.quizapp.ui.theme.QuizAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            // Show loader on start
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LoaderFragment())
                .commit()

            // Simulate API call (replace with your actual API request)
            fetchDataFromApi()
        }

    }
    private fun fetchDataFromApi() {
        // For example, using a coroutine (or any async mechanism)
        GlobalScope.launch(Dispatchers.Main) {
            // Now API response is available. Swap to QuizQuestionFragment:
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, QuizQuestionFragment())
                .commit()
        }
    }
}
//
//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    QuizAppTheme {
//        Greeting("Android")
//    }
//}