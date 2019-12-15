package com.example.geoquiz02

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    /////declare ui element variables
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /////retrieve and assign inflated ui elements
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)

        /////set listeners on buttons
        trueButton.setOnClickListener { view: View ->
            val toast = Toast.makeText(this, R.string.correct_toast, Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL, 0, -300)
            toast.show()
        }

        falseButton.setOnClickListener { view: View ->
            val toast = Toast.makeText(this, R.string.incorrect_toast, Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL, 0, -300)
            toast.show()
        }
    }
}
