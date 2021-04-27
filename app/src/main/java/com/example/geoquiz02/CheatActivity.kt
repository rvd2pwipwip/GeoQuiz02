package com.example.geoquiz02

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels

private const val TAG = "CheatActivity"

//key-value pairs to be stored in onSaveInstanceState Bundle
private const val KEY_IS_CHEATER = "is_cheater"
private const val KEY_CHEATED_INDEX = "index"

//Using your package name as a qualifier for your extra prevents name collisions with extras from other apps.
private const val EXTRA_ANSWER_IS_TRUE = "com.example.geoquiz02.answer_is_true"

//const val EXTRA_ANSWER_SHOWN = "com.example.geoquiz02.answer_shown"
const val EXTRA_CHEATED_INDEX = "com.example.geoquiz02.cheated_index"

class CheatActivity : AppCompatActivity() {

    private lateinit var answerTextView: TextView
    private lateinit var showAnswerButton: Button
    private lateinit var apiLevelTextView: TextView

    private var answerIsTrue = false
    private val cheatViewModel: CheatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        /////In onCreate(Bundle?), check for values. If they exist, assign them to their respective value name.
        /////If a value with the key "value" does not exist in the bundle, or if the bundle object is null, set its default value
//        val isCheater = savedInstanceState?.getBoolean(KEY_IS_CHEATER, false) ?: false
//        cheatViewModel.isCheater = isCheater
//        val cheatedIndex = savedInstanceState?.getInt(KEY_CHEATED_INDEX, -1) ?: -1
        val cheatedIndex = intent.getIntExtra(EXTRA_CHEATED_INDEX, -1)
        cheatViewModel.cheatedIndex = cheatedIndex
        Log.d(TAG,"Created CheatActivity with cheatedIndex: $cheatedIndex")

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)
        apiLevelTextView = findViewById(R.id.api_level)

        showAnswerButton.setOnClickListener {
            val answerText = when {
                answerIsTrue -> R.string.true_button
                else -> R.string.false_button
            }
            answerTextView.setText(answerText)
            setAnswerShownResult(cheatedIndex)
        }

        val apiLevel: String = "API Level " + Build.VERSION.SDK_INT.toString()
        apiLevelTextView.text = apiLevel
    }

    /////override onSaveInstanceState(Bundle) to write the value of currentIndex to the bundle with the constant as its key
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putInt(KEY_CHEATED_INDEX, cheatViewModel.cheatedIndex)
        Log.i(TAG, "index: ${cheatViewModel.cheatedIndex}")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun setAnswerShownResult(cheatedIndex: Int) {

        val data = Intent().apply {
            putExtra(EXTRA_CHEATED_INDEX, cheatedIndex)
        }
        Log.d(TAG, "putExtra done")
        setResult(Activity.RESULT_OK, data)

        Log.d(TAG, "cheated at index: $cheatedIndex")

    }

    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean, cheatedIndex: Int): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
                putExtra(EXTRA_CHEATED_INDEX, cheatedIndex)
            }
        }
    }
}
