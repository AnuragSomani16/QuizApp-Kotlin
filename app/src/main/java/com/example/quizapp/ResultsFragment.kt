package com.example.quizapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.quizapp.databinding.FragmentResultBinding
import com.example.quizapp.viewModels.MainViewModel
import com.github.jinatonic.confetti.CommonConfetti
import org.koin.androidx.viewmodel.ext.android.activityViewModel

private const val DISTINCTION_COUNT = 8

class ResultFragment : Fragment() {

    private lateinit var binding: FragmentResultBinding
    private val mainViewModel: MainViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val correct = mainViewModel.correctAnswers.value ?: 0
        val total = mainViewModel.questions.value?.size ?: 10
        val skippedQuestionsCount = mainViewModel.getSkippedQuestionsCount()
        val currentStreak = mainViewModel.getCurrentStreak()
        val highestStreak = mainViewModel.highestStreak.value ?: currentStreak

        binding.title.text = binding.root.context.getString(R.string.result_title)
        binding.textViewResult.text = binding.root.context.getString(R.string.result_text).format(correct, total)
        binding.scoreValue.text = "$correct"
        binding.skippedValue.text = "$skippedQuestionsCount"
        binding.highestStreakValue.text = "$highestStreak"
        binding.currentStreakValue.text = "$currentStreak"

        if(correct>=DISTINCTION_COUNT) triggerConfetti()

        if (correct < 3) {
            binding.sadEmoji.visibility = View.VISIBLE
            binding.betterLuck.visibility = View.VISIBLE
            animateSadEmoji()
        } else {
            binding.sadEmoji.visibility = View.GONE
            binding.betterLuck.visibility = View.GONE
        }

        binding.restartButton.setOnClickListener {
            mainViewModel.reset()
            mainViewModel.fetchQuestions()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LoaderFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun triggerConfetti() {
        CommonConfetti.rainingConfetti(
            binding.root as ViewGroup,
            intArrayOf(
                ContextCompat.getColor(requireContext(), R.color.orange),
                ContextCompat.getColor(requireContext(), R.color.cyan_500),
                ContextCompat.getColor(requireContext(), R.color.green_700)
            )
        ).stream(1000)
    }

    private fun animateSadEmoji() {
        val animation = TranslateAnimation(0f, 0f, -500f, 500f).apply {
            duration = 1500
            repeatMode = TranslateAnimation.REVERSE
            repeatCount = 1
        }
        binding.sadEmoji.startAnimation(animation)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}