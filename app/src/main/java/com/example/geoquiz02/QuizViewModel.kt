package com.example.geoquiz02

import androidx.lifecycle.ViewModel

//private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel() {

//    init {
//        Log.d(TAG, "ViewModel instance created")
//    }
//
//    override fun onCleared() {
//        super.onCleared()
//        Log.d(TAG, "ViewModel instance about to be destroyed")
//    }

    /////call Question class to create a list of Question objects
    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    var currentIndex = 0
    var currentCounter = 0
    var currentScore = 0
    var answerMode = true
    var isCheater = false
    var cheatedIndexes = mutableListOf<Int>()

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    val currentSize: Int
        get() = questionBank.size

    fun moveToPrevious() {
        currentIndex = if (currentIndex == 0) {
            questionBank.size - 1
        } else {
            (currentIndex - 1) % questionBank.size
        }
    }

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }
}