package com.example.androidarchitecture.feature.counter

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.androidarchitecture.R

/**
 * CounterActivity — the "View" in MVC.
 *
 * Model and Controller are created in onCreate() — both destroyed on
 * configuration changes. The counter resets to 0 every time.
 */
class CounterActivity : AppCompatActivity() {

    private lateinit var controller: CounterController
    private lateinit var countTextView: TextView
    private lateinit var errorTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter)

        countTextView = findViewById(R.id.countTextView)
        errorTextView = findViewById(R.id.errorTextView)

        val model = CounterModel()
        controller = CounterController(this, model)

        findViewById<Button>(R.id.incrementButton).setOnClickListener {
            controller.onIncrementClicked()
        }
        findViewById<Button>(R.id.resetButton).setOnClickListener {
            controller.onResetClicked()
        }
        findViewById<Button>(R.id.setToNeg1Button).setOnClickListener {
            controller.onSetCountClicked(-1)
        }
    }

    fun updateCounter(count: Int) {
        countTextView.text = count.toString()
    }

    fun showError(message: String) {
        errorTextView.text = message
        errorTextView.visibility = View.VISIBLE
    }

    fun clearError() {
        errorTextView.text = ""
        errorTextView.visibility = View.GONE
    }
}
