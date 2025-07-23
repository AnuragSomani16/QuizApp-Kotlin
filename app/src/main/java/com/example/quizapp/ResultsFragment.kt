package com.example.quizapp

import android.os.Bundle
import android.util.Log
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

        val correctAnswersCount = mainViewModel.correctAnswers.value ?: 0
        val quizData = mainViewModel.questions.value ?: return
        val total = quizData.questions.size
        val wrongAnswersCount = mainViewModel.getWrongAnswersCount()
        val skippedQuestionsCount = mainViewModel.getSkippedQuestionsCount()
        val currentStreak = mainViewModel.getCurrentStreak()
        val highestStreak = mainViewModel.highestStreak.value ?: currentStreak

        binding.title.text = binding.root.context.getString(R.string.result_title)
        binding.textViewResult.text = binding.root.context.getString(R.string.result_text).format(correctAnswersCount, total)
        binding.scoreValue.text = "$correctAnswersCount"
        binding.skippedValue.text = "$skippedQuestionsCount"
        binding.highestStreakValue.text = "$highestStreak"
        binding.currentStreakValue.text = "$currentStreak"
        binding.wrongAnswerCount.text = "$wrongAnswersCount"

        if (correctAnswersCount > DISTINCTION_COUNT) {
            binding.root.post {
                triggerConfetti()
            }
        }
        //for floating emoji
        animateEmoji(isGoodScore = correctAnswersCount>5)

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
        Log.i("cTAG", "triggerConfetti: ")
        if (isAdded && context != null) {
            try {
                CommonConfetti.rainingConfetti(
                    binding.resultLayout as ViewGroup,
                    intArrayOf(
                        ContextCompat.getColor(requireContext(), R.color.orange),
                        ContextCompat.getColor(requireContext(), R.color.green_700),
                        ContextCompat.getColor(requireContext(), R.color.purple_500),
                        ContextCompat.getColor(requireContext(), R.color.red_500),
                        ContextCompat.getColor(requireContext(), R.color.pink_500),
                        ContextCompat.getColor(requireContext(), R.color.teal_500),
                        ContextCompat.getColor(requireContext(), R.color.indigo_500),
                        ContextCompat.getColor(requireContext(), R.color.amber_500)
                    )
                ).stream(5000)
            } catch (e: Exception) {
                Log.e("cTAG", "Error triggering confetti: ${e.message}")
            }
        }
    }

    private fun animateEmoji(isGoodScore: Boolean = false) {
        val animation = TranslateAnimation(0f, 0f, -40f, 400f).apply {
            duration = 1500
            repeatMode = TranslateAnimation.REVERSE
            repeatCount = 1
        }
        if(isGoodScore) {
            binding.sadEmoji.visibility = View.GONE
            binding.betterLuck.visibility = View.GONE
            binding.happyEmoji.visibility = View.VISIBLE
            binding.happyEmoji.startAnimation(animation)
        }
        else {
            binding.sadEmoji.visibility = View.VISIBLE
            binding.sadEmoji.startAnimation(animation)
            binding.betterLuck.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}