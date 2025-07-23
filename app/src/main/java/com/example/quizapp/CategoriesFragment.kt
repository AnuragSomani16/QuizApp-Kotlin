package com.example.quizapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.quizapp.databinding.FragmentCategoryBinding
import com.example.quizapp.databinding.FragmentQuestionBinding
import com.example.quizapp.viewModels.MainViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import kotlin.getValue

class CategoriesFragment: Fragment() {
    private lateinit var binding: FragmentCategoryBinding
    private val viewModel: MainViewModel by activityViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.categoriesGroup
    }

    private fun showCategories() {
        val categories = viewModel.categories.value
        binding.categoriesGroup.removeAllViews()
        categories?.forEach { category ->
            val categoryView = layoutInflater.inflate(R.layout.quiz_category_block, binding.categoriesGroup, false) as LinearLayout
            val title = categoryView.findViewById<TextView>(R.id.textCategoryTitle)
            val icon = categoryView.findViewById<ImageView>(R.id.imageTick)
            val startBtn = categoryView.findViewById<Button>(R.id.btnStart)
            val restartBtn = categoryView.findViewById<Button>(R.id.btnRestart)
            val reviewBtn = categoryView.findViewById<Button>(R.id.btnReview)
            title.text = category.title
            lifecycleScope.launch {
                val ifProgressStored = viewModel.isCategoryDataStored(category.id)
                if(ifProgressStored) {
                    icon.visibility = View.VISIBLE
                    restartBtn.visibility = View.VISIBLE
                    reviewBtn.visibility = View.VISIBLE
                    startBtn.visibility = View.GONE
                }
                else {
                    startBtn.visibility = View.GONE
                    icon.visibility = View.GONE
                    restartBtn.visibility = View.GONE
                    reviewBtn.visibility = View.VISIBLE
                }
            }
            startBtn.setOnClickListener {
                viewModel.currentCategory = category
                viewModel.fetchQuestions()
            }
            reviewBtn.setOnClickListener {
                viewModel.currentCategory = category
                viewModel.fetchCategoryQuestions(category.id)
            }
        }
    }

}