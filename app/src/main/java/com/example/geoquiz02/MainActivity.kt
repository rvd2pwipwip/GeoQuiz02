package com.example.geoquiz02

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    /////declare ui element variables
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var prevButton: ImageButton
    private lateinit var nextButton: ImageButton
    private lateinit var questionTextView: TextView

    /////by lazy allows quizViewModel property to be a val instead of a var
    /////calculation and assignment will not happen until the first time you access quizViewModel
    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }

//    private var counter = 0
//    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

//        /////associate the activity with an instance of QuizViewModel
//        val provider: ViewModelProvider = ViewModelProviders.of(this)
//        val quizViewModel = provider.get(QuizViewModel::class.java)
        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")

        /////retrieve and assign inflated ui elements
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        prevButton = findViewById(R.id.prev_button)
        nextButton = findViewById(R.id.next_button)
        questionTextView =findViewById(R.id.question_text_view)

        /////set listeners on buttons
        trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
        }

        falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
        }

        prevButton.setOnClickListener {
            quizViewModel.moveToPrevious()
            updateQuestion()
            quizViewModel.answerMode = true
        }

        nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            quizViewModel.answerMode = true
            updateQuestion()
        }

        questionTextView.setOnClickListener {
            quizViewModel.moveToNext()
            quizViewModel.answerMode = true
            updateQuestion()
        }

        updateQuestion()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateQuestion() {
        /////use currentIndex to set the question text
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
        setUIMode()
    }

    private fun checkAnswer(userAnswer: Boolean) {

        quizViewModel.currentCounter += 1
        Log.d(TAG, "counter: ${quizViewModel.currentCounter}")

        quizViewModel.answerMode = false
        setUIMode()
        val correctAnswer = quizViewModel.currentQuestionAnswer

        val messageResId = if (userAnswer == correctAnswer) {
            quizViewModel.currentScore += 1
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }

        val toast = Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL, 0, -200)
        toast.show()

        Log.d(TAG, "score: ${quizViewModel.currentScore}")

        if (quizViewModel.currentCounter == quizViewModel.currentSize) {
            gradeQuiz()
        }
    }

    private fun gradeQuiz() {
        var message: String
        if (quizViewModel.currentScore != 0) {
            val grade = (quizViewModel.currentScore.toDouble() / quizViewModel.currentSize * 100).toInt()
            message = "You got $grade%"
        } else {
            message = "You totally flunked with 0%"
        }
        val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL, 0, -200)
        toast.show()

        quizViewModel.currentScore = 0
        quizViewModel.currentCounter = 0
    }

    private fun setUIMode() {
        if (quizViewModel.answerMode == true) {
            trueButton.isEnabled = true
            falseButton.isEnabled = true
            nextButton.isEnabled = false
            prevButton.isEnabled = false
            questionTextView.isClickable = false
        } else {
            trueButton.isEnabled = false
            falseButton.isEnabled = false
            nextButton.isEnabled = true
            prevButton.isEnabled = true
            questionTextView.isClickable = true
        }
    }
}
