package com.example.quizapp

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.*
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.quizapp.databinding.FragmentQuestionBinding
import com.example.quizapp.viewModels.MainViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import com.github.jinatonic.confetti.CommonConfetti

private const val DELAY_2_SECONDS = 2000L

class QuestionsFragment : Fragment() {

    private lateinit var binding: FragmentQuestionBinding
    private val mainViewModel: MainViewModel by activityViewModel()

    private var selectedIndex: Int? = null
    private var isAnswerSubmitted = false
    private var streakCount = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuestionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mainViewModel.currentIndex.observe(viewLifecycleOwner) { index ->
            selectedIndex = null
            isAnswerSubmitted = false
            binding.optionsGroup.animate()
                .rotationY(90f)
                .setDuration(150)
                .withEndAction {
                    binding.optionsGroup.rotationY = -90f
                    showQuestion(index)
                    binding.optionsGroup.animate()
                        .rotationY(0f)
                        .setDuration(150)
                        .start()
                }
                .start()
        }

        binding.buttonSkip.setOnClickListener {
            if (isAnswerSubmitted) return@setOnClickListener
            isAnswerSubmitted = true
            mainViewModel.incrementSkippedQuestionsCount()
            streakCount = 0
            updateStreakView(streakCount)
            mainViewModel.submitAnswer(selectedIndex ?: -1)
        }
    }

    private fun showQuestion(index: Int) {
        val question = mainViewModel.questions.value?.get(index) ?: return
        val totalQuestions = mainViewModel.questions.value?.size ?: 0
        binding.progressTop.progress = ((index + 1) * 100) / totalQuestions
        binding.questionProgressText.text = getString(R.string.question_progress_text, index + 1, totalQuestions)
        binding.questionText.text = question.question

        binding.optionsGroup.removeAllViews()

        question.options.forEachIndexed { i, optionText ->
            val optionView = layoutInflater.inflate(R.layout.item_option_block, binding.optionsGroup, false) as LinearLayout
            val optionTextView = optionView.findViewById<TextView>(R.id.option_text)
            val icon = optionView.findViewById<ImageView>(R.id.icon_view)

            optionTextView.text = optionText
            icon.visibility = View.GONE
            optionView.isClickable = true

            val background = ContextCompat.getDrawable(requireContext(), R.drawable.background_option_block)?.mutate()
            optionView.background = background
            optionView.background.setTint(ContextCompat.getColor(requireContext(), R.color.gray))

            optionView.setOnClickListener {
                if (!isAnswerSubmitted) {
                    selectedIndex = i
                    isAnswerSubmitted = true
                    highlightAnswers()
                }
            }

            binding.optionsGroup.addView(optionView)
        }
    }

    private fun highlightAnswers() {
        val question = mainViewModel.questions.value?.get(mainViewModel.currentIndex.value ?: 0) ?: return
        val correctIndex = question.correctOptionIndex
        var answerIsCorrect = false

        for (i in 0 until binding.optionsGroup.childCount) {
            val optionView = binding.optionsGroup.getChildAt(i) as LinearLayout
            val icon = optionView.findViewById<ImageView>(R.id.icon_view)
            val textView = optionView.findViewById<TextView>(R.id.option_text)

            when {
                i == selectedIndex && i == correctIndex -> {
                    optionView.background.setTint(ContextCompat.getColor(requireContext(), R.color.green_700))
                    textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    icon.setImageResource(R.drawable.ic_check)
                    icon.visibility = View.VISIBLE
                    answerIsCorrect = true
                }
                i == selectedIndex && i != correctIndex -> {
                    optionView.background.setTint(ContextCompat.getColor(requireContext(), R.color.red_700))
                    textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    icon.setImageResource(R.drawable.ic_close)
                    icon.visibility = View.VISIBLE
                }
                i == correctIndex -> {
                    optionView.background.setTint(ContextCompat.getColor(requireContext(), R.color.green_700))
                    textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    icon.setImageResource(R.drawable.ic_check)
                    icon.visibility = View.VISIBLE
                }
                else -> {
                    optionView.background.setTint(ContextCompat.getColor(requireContext(), R.color.gray))
                    icon.visibility = View.GONE
                }
            }

            optionView.isClickable = false
        }

        streakCount = if (answerIsCorrect) streakCount + 1 else 0
        if (streakCount > (mainViewModel.highestStreak.value ?: 0)) {
            mainViewModel.highestStreak.value = streakCount
        }
        updateStreakView(streakCount)
        animateAndMoveNext()
    }

    private fun updateStreakView(streakCount: Int) {
        val fireLayout = binding.streakLayout
        fireLayout.removeAllViews()
        if(mainViewModel.getCurrentStreak() < streakCount) mainViewModel.incrementCurrentStreak()
        if (streakCount >= 3) {
            fireLayout.alpha = 0.9f

            for (i in 1..streakCount) {
                val emoji = TextView(requireContext()).apply {
                    text = "\uD83D\uDD25"
                    textSize = 24f
                    letterSpacing = 0.2f
                    setPadding(6, 0, 6, 0)
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.orange))
                    setShadowLayer(8f, 0f, 0f, ContextCompat.getColor(requireContext(), R.color.orange))
                    alpha = 0.85f
                    scaleX = 0.7f
                    scaleY = 0.7f
                }

                emoji.animate()
                    .scaleX(1.3f)
                    .scaleY(1.3f)
                    .setDuration(200)
                    .withEndAction {
                        emoji.animate().scaleX(1f).scaleY(1f).setDuration(150).start()
                    }
                    .start()

                fireLayout.addView(emoji)
            }

            fireLayout.scaleX = 0.95f
            fireLayout.scaleY = 0.95f
            fireLayout.animate()
                .scaleX(1.1f)
                .scaleY(1.1f)
                .setDuration(200)
                .withEndAction {
                    fireLayout.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(150)
                        .start()
                }
                .start()
            val listOfStreaksForConfetti = listOf(3, 5, 7, 10)
            if (streakCount in listOfStreaksForConfetti) {
                CommonConfetti.rainingConfetti(
                    binding.root as ViewGroup,
                    intArrayOf(
                        ContextCompat.getColor(requireContext(), R.color.orange),
                        ContextCompat.getColor(requireContext(), R.color.cyan_500),
                        ContextCompat.getColor(requireContext(), R.color.green_700)
                    )
                ).stream(1000)
            }
        }
    }

    private fun animateAndMoveNext() {
        binding.progressBarNextQuestion.visibility = View.VISIBLE
        startCountdownProgress()
        binding.progressBarNextQuestion.postDelayed({
            mainViewModel.submitAnswer(selectedIndex ?: -1)
        }, DELAY_2_SECONDS)
    }

    private fun startCountdownProgress() {
        binding.progressBarNextQuestion.apply {
            visibility = View.VISIBLE
            max = 100
            progress = 100
        }
        val animator = ObjectAnimator.ofInt(binding.progressBarNextQuestion, "progress", 100, 0)
        animator.duration = DELAY_2_SECONDS
        animator.interpolator = LinearInterpolator()
        animator.start()
        animator.doOnEnd {
            binding.progressBarNextQuestion.visibility = View.GONE
        }
    }
}