package com.example.geoquiz02

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders

private const val TAG = "CheatActivity"

//Using your package name as a qualifier for your extra prevents name collisions with extras from other apps.
private const val EXTRA_ANSWER_IS_TRUE = "com.example.geoquiz02.answer_is_true"
const val EXTRA_ANSWER_SHOWN = "com.example.geoquiz02.answer_shown"

/////key-value pairs to be stored in onSaveInstanceState Bundle
private const val KEY_CHEATER = "cheater"

class CheatActivity : AppCompatActivity() {

    private lateinit var answerTextView: TextView
    private lateinit var showAnswerButton: Button

    private var answerIsTrue = false

    /////by lazy allows quizViewModel property to be a val instead of a var
    /////calculation and assignment will not happen until the first time you access quizViewModel
    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        Log.d(TAG, "onCreate(Bundle?) of CheatActivity called")

        /////In onCreate(Bundle?), check for values. If they exist, assign them to their respective value name.
        /////If a value with the key "value" does not exist in the bundle, or if the bundle object is null, set its default value
        val currentCheat = savedInstanceState?.getBoolean(KEY_CHEATER,false) ?: false
        Log.d(TAG,"KEY_CHEATER: $currentCheat")
        quizViewModel.answerMode = currentCheat

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)

        showAnswerButton.setOnClickListener {
            val answerText = when {
                answerIsTrue -> R.string.true_button
                else -> R.string.false_button
            }
            answerTextView.setText(answerText)
            setAnswerShownResult(true)
        }
    }

    /////override onSaveInstanceState(Bundle) to write the value of currentCheat to the bundle with the constant as its key
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG,"onSaveInstanceState of CheatActivity")
        savedInstanceState.putBoolean(KEY_CHEATER, quizViewModel.isCheater)
        Log.i(TAG,"Cheater: ${quizViewModel.isCheater}")
    }

    private fun setAnswerShownResult(isAnswerShown: Boolean) {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        }
        quizViewModel.isCheater = isAnswerShown
        setResult(Activity.RESULT_OK, data)

    }

    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }
}
