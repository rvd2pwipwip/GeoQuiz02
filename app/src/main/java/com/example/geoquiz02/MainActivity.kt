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

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    /////declare ui element variables
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var prevButton: ImageButton
    private lateinit var nextButton: ImageButton
    private lateinit var questionTextView: TextView

    /////call Question class to create a list of Question objects
    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true))

    private var currentIndex = 0
    private var currentCounter = 0
    private var currentScore = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

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
            if (currentIndex == 0) {
                currentIndex = questionBank.size - 1
            } else {
                currentIndex = (currentIndex - 1) % questionBank.size
            }
            updateQuestion()
            toggleButtons()
        }

        nextButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
            toggleButtons()
        }

        questionTextView.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }

        updateQuestion()
        toggleNavButtons()
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
        val questionTextResId = questionBank[currentIndex].textResId
        questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean) {

        currentCounter += 1
        Log.d(TAG, "counter: ${currentCounter}")

        toggleButtons()
        val correctAnswer = questionBank[currentIndex].answer

        val messageResId = if (userAnswer == correctAnswer) {
            currentScore += 1
            Log.d(TAG, "counter: ${currentScore}")
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }

        val toast = Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL, 0, -300)
        toast.show()

        if (currentCounter == questionBank.size) {
            gradeQuiz()
        }
    }

    private fun gradeQuiz() {
        var message: String
        if (currentScore != 0) {
            val grade = (currentScore.toDouble() / questionBank.size * 100).toInt()
            message = "You got $grade%"
        } else {
            message = "You totally flunked with 0%"
        }
        val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL, 0, -300)
        toast.show()

        currentScore = 0
        currentCounter = 0
    }

    private fun toggleAnswerButtons() {
        trueButton.isEnabled = !trueButton.isEnabled
        falseButton.isEnabled = !falseButton.isEnabled
    }

    private fun toggleNavButtons() {
        prevButton.isEnabled = !prevButton.isEnabled
        nextButton.isEnabled = !nextButton.isEnabled
    }

    private fun toggleButtons() {
        toggleAnswerButtons()
        toggleNavButtons()
    }
}
