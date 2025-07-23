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
import com.github.jinatonic.confetti.CommonConfetti
import org.koin.androidx.viewmodel.ext.android.activityViewModel

private const val DELAY_2_SECONDS = 2000L
private const val DELAY_FOR_CONFETTI = 300L
private val STREAK_CONFETTI_THRESHOLDS = listOf(3, 5, 10)

class QuestionsFragment : Fragment() {

    private lateinit var binding: FragmentQuestionBinding
    private val viewModel: MainViewModel by activityViewModel()

    private var selectedIndex: Int? = null
    private var isAnswerSubmitted = false
    private var streakCount = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentQuestionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.currentIndex.observe(viewLifecycleOwner) { index ->
            selectedIndex = null
            isAnswerSubmitted = false
            animateFlipToNewQuestion(index)
        }

        binding.buttonSkip.bringToFront()
        binding.buttonSkip.setOnClickListener {
            if (isAnswerSubmitted) return@setOnClickListener
            isAnswerSubmitted = true
            viewModel.incrementSkippedQuestionsCount()
            streakCount = 0
            updateStreakView()
            viewModel.submitAnswer(selectedIndex ?: -1)
        }
    }

    private fun animateFlipToNewQuestion(index: Int) {
        binding.questionCard.animate().rotationY(90f).setDuration(150).withEndAction {
            if (!isAdded) return@withEndAction // Prevent crash if Fragment is not attached
            binding.questionCard.rotationY = -90f
            showQuestion(index)
            binding.questionCard.animate().rotationY(0f).setDuration(150).start()
        }.start()
    }

    private fun showQuestion(index: Int) {
        if (!isAdded) return  // prevent invalid context use
        val quizQuestion = viewModel.questions.value ?: return
        val question = quizQuestion.questions.getOrNull(index) ?: return
        val total = quizQuestion.questions.size

        binding.progressTop.progress = ((index + 1) * 100) / total
        binding.questionProgressText.text = getString(R.string.question_progress_text, index + 1, total)
        binding.questionText.text = question.questionText
        binding.quizTitle.text = quizQuestion.category

        binding.optionsGroup.removeAllViews()

        question.options.forEachIndexed { i, text ->
            val optionView = layoutInflater.inflate(R.layout.item_option_block, binding.optionsGroup, false) as LinearLayout
            val optionText = optionView.findViewById<TextView>(R.id.option_text)
            val icon = optionView.findViewById<ImageView>(R.id.icon_view)

            optionText.text = text
            icon.visibility = View.GONE
            optionView.isClickable = true
            optionView.background = ContextCompat.getDrawable(requireContext(), R.drawable.background_option_block)?.mutate()?.apply {
                setTint(ContextCompat.getColor(requireContext(), R.color.gray))
            }

            optionView.setOnClickListener {
                if (!isAnswerSubmitted) {
                    selectedIndex = i
                    isAnswerSubmitted = true
                    highlightAnswer(question.correctAnswerIndex)
                }
            }

            binding.optionsGroup.addView(optionView)
        }
    }

    private fun highlightAnswer(correctIndex: Int) {
        var isCorrect = false

        for (i in 0 until binding.optionsGroup.childCount) {
            val optionView = binding.optionsGroup.getChildAt(i) as LinearLayout
            val text = optionView.findViewById<TextView>(R.id.option_text)
            val icon = optionView.findViewById<ImageView>(R.id.icon_view)

            when {
                i == selectedIndex && i == correctIndex -> {
                    styleOption(optionView, text, icon, R.color.green_700, R.drawable.ic_check)
                    isCorrect = true
                }
                i == selectedIndex -> {
                    viewModel.incrementWrongAnswersCount()
                    styleOption(optionView, text, icon, R.color.red_700, R.drawable.ic_close)
                }
                i == correctIndex -> {
                    styleOption(optionView, text, icon, R.color.green_700, R.drawable.ic_check)
                }
                else -> {
                    optionView.background.setTint(ContextCompat.getColor(requireContext(), R.color.gray))
                    icon.visibility = View.GONE
                }
            }

            optionView.isClickable = false
        }

        updateStreak(isCorrect)
        animateAndMoveNext()
    }

    private fun styleOption(view: LinearLayout, text: TextView, icon: ImageView, color: Int, iconRes: Int) {
        if (!isAdded) return
        view.background.setTint(ContextCompat.getColor(requireContext(), color))
        text.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        icon.setImageResource(iconRes)
        icon.visibility = View.VISIBLE
    }

    private fun updateStreak(correct: Boolean) {
        streakCount = if (correct) streakCount + 1 else 0
        if (streakCount > (viewModel.highestStreak.value ?: 0)) {
            viewModel.highestStreak.value = streakCount
        }
        if (viewModel.getCurrentStreak() < streakCount) viewModel.incrementCurrentStreak()
        updateStreakView()
    }

    private fun updateStreakView() {
        if (!isAdded) return
        val fireLayout = binding.streakLayout
        fireLayout.removeAllViews()

        if (streakCount >= 3) {
            fireLayout.alpha = 0.9f
            repeat(streakCount) {
                val fireEmoji = TextView(requireContext()).apply {
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
                fireEmoji.animate().scaleX(1.3f).scaleY(1.3f).setDuration(200).withEndAction {
                    fireEmoji.animate().scaleX(1f).scaleY(1f).setDuration(150).start()
                }.start()
                fireLayout.addView(fireEmoji)
            }

            fireLayout.animate().scaleX(1.1f).scaleY(1.1f).setDuration(200).withEndAction {
                fireLayout.animate().scaleX(1f).scaleY(1f).setDuration(150).start()
            }.start()

            if (streakCount in STREAK_CONFETTI_THRESHOLDS && isAdded) {
                CommonConfetti.rainingConfetti(
                    binding.root as ViewGroup,
                    intArrayOf(
                        ContextCompat.getColor(requireContext(), R.color.orange),
                        ContextCompat.getColor(requireContext(), R.color.cyan_500),
                        ContextCompat.getColor(requireContext(), R.color.green_700)
                    )
                ).stream(DELAY_FOR_CONFETTI)
            }
        }
    }

    private fun animateAndMoveNext() {
        if (!isAdded) return
        binding.progressBarNextQuestion.visibility = View.VISIBLE
        startCountdownProgress()
        binding.progressBarNextQuestion.postDelayed({
            if (isAdded) {
                viewModel.submitAnswer(selectedIndex ?: -1)
            }
        }, DELAY_2_SECONDS)
    }

    private fun startCountdownProgress() {
        if (!isAdded) return
        binding.progressBarNextQuestion.apply {
            visibility = View.VISIBLE
            max = 100
            progress = 100
        }
        ObjectAnimator.ofInt(binding.progressBarNextQuestion, "progress", 100, 0).apply {
            duration = DELAY_2_SECONDS
            interpolator = LinearInterpolator()
            doOnEnd {
                if (isAdded) binding.progressBarNextQuestion.visibility = View.GONE
            }
            start()
        }
    }
}