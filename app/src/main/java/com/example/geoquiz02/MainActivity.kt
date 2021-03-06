package com.example.geoquiz02

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels

private const val TAG = "MainActivity"

/////key-value pairs to be stored in onSaveInstanceState Bundle
private const val KEY_INDEX = "index"
private const val KEY_COUNTER = "counter"
private const val KEY_SCORE = "score"
private const val KEY_MODE = "mode"

/////key-value pair for activity intent
private const val REQUEST_CODE_CHEAT = 0

class MainActivity : AppCompatActivity() {

    /////declare ui element variables
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var prevButton: ImageButton
    private lateinit var nextButton: ImageButton
    private lateinit var cheatButton: Button
    private lateinit var questionTextView: TextView

    /////associate the activity with an instance of QuizViewModel
    private val quizViewModel: QuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")

        /////In onCreate(Bundle?), check for values. If they exist, assign them to their respective value name.
        /////If a value with the key "value" does not exist in the bundle, or if the bundle object is null, set its default value
        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex

        val currentScore = savedInstanceState?.getInt(KEY_SCORE, 0) ?: 0
        quizViewModel.currentScore = currentScore

        val currentCounter = savedInstanceState?.getInt(KEY_COUNTER,0) ?: 0
        quizViewModel.currentCounter = currentCounter

        val currentMode = savedInstanceState?.getBoolean(KEY_MODE,true) ?: true
        quizViewModel.answerMode = currentMode

        /////retrieve and assign inflated ui elements
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        prevButton = findViewById(R.id.prev_button)
        nextButton = findViewById(R.id.next_button)
        cheatButton = findViewById(R.id.cheat_button)
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

        cheatButton.setOnClickListener { view -> //lambda uses argument (view) so it is named (instead of it)
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val cheatIndex = quizViewModel.currentIndex
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue, cheatIndex)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val options = ActivityOptions.makeClipRevealAnimation(view, 0, 0, view.width, view.height)
                startActivityForResult(intent, REQUEST_CODE_CHEAT, options.toBundle())
            } else {
                startActivityForResult(intent, REQUEST_CODE_CHEAT)
            }
        }

        questionTextView.setOnClickListener {
            quizViewModel.moveToNext()
            quizViewModel.answerMode = true
            updateQuestion()
        }

        updateQuestion()
    }

    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int,
                                  data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
//            quizViewModel.isCheater =
//                data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
            quizViewModel.cheatedIndexes.add(data?.getIntExtra(EXTRA_CHEATED_INDEX, -1) ?: -1)
        }
        Log.d(TAG, "onActivityResult received cheatedIndex ${data?.getIntExtra(EXTRA_CHEATED_INDEX, -1) ?: -1}")
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

    /////override onSaveInstanceState(Bundle) to write the value of currentIndex to the bundle with the constant as its key
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG,"onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
        savedInstanceState.putInt(KEY_COUNTER, quizViewModel.currentCounter)
        savedInstanceState.putInt(KEY_SCORE, quizViewModel.currentScore)
        savedInstanceState.putBoolean(KEY_MODE, quizViewModel.answerMode)
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

        quizViewModel.currentCounter++
        Log.d(TAG, "counter: ${quizViewModel.currentCounter}")

        quizViewModel.answerMode = false
        setUIMode()
        val correctAnswer = quizViewModel.currentQuestionAnswer

        val messageResId = when {
            quizViewModel.cheatedIndexes.contains(quizViewModel.currentIndex) -> R.string.judgment_toast
            userAnswer == correctAnswer -> {
                quizViewModel.currentScore++
                R.string.correct_toast
            } else -> R.string.incorrect_toast
        }

        val toast = Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL, 0, -300)
        toast.show()

        Log.d(TAG, "score: ${quizViewModel.currentScore}")

        if (quizViewModel.currentCounter == quizViewModel.currentSize) {
            gradeQuiz()
        }
    }

    private fun gradeQuiz() {
        val message: String
        message = if (quizViewModel.currentScore != 0) {
            val grade = (quizViewModel.currentScore.toDouble() / quizViewModel.currentSize * 100).toInt()
            "You got $grade%"
        } else {
            "You totally flunked with 0%"
        }
        val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL, 0, -300)
        toast.show()

        quizViewModel.currentScore = 0
        quizViewModel.currentCounter = 0
    }

    private fun setUIMode() {
        if (quizViewModel.answerMode) {
            trueButton.isEnabled = true
            falseButton.isEnabled = true
            nextButton.isEnabled = false
            prevButton.isEnabled = false
            questionTextView.isClickable = false
            cheatButton.isEnabled = true
        } else {
            trueButton.isEnabled = false
            falseButton.isEnabled = false
            nextButton.isEnabled = true
            prevButton.isEnabled = true
            questionTextView.isClickable = true
            cheatButton.isEnabled = false
        }
    }
}
