package com.example.androidarchitecture

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * "God Activity" — NO architecture pattern.
 *
 * Everything lives in ONE class: data, business logic, UI updates, validation.
 * There is no Model, no Controller, no Presenter, no ViewModel, no separate
 * file for anything. This is what early Android apps looked like.
 *
 * Problems:
 * - Can't test business logic without running the app (logic is in the Activity)
 * - Can't reuse logic (it's embedded in this specific Activity)
 * - Can't have two developers work on UI and logic separately
 * - Adding features means this class grows forever ("God Activity")
 * - State is lost on configuration changes (e.g. rotation) — count resets to 0
 * - No separation of concerns — data, validation, and UI all in the same class
 *
 * These problems are what drove the evolution to MVC → MVP → MVVM → MVI.
 */
class MainActivity : AppCompatActivity() {

    // Data — lives right here in the Activity
    private var count = 0

    // Business logic — also in the Activity (not in a separate Model)
    private fun setCount(value: Int): Boolean {
        return if (value >= 0) {
            count = value
            true
        } else {
            false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val countTextView = findViewById<TextView>(R.id.countTextView)
        val errorTextView = findViewById<TextView>(R.id.errorTextView)

        findViewById<Button>(R.id.incrementButton).setOnClickListener {
            count++                                   // data mutation in UI
            countTextView.text = count.toString()      // UI update
            errorTextView.visibility = View.GONE
        }

        findViewById<Button>(R.id.resetButton).setOnClickListener {
            count = 0                                  // data mutation in UI
            countTextView.text = count.toString()      // UI update
            errorTextView.visibility = View.GONE
        }

        findViewById<Button>(R.id.setToNeg1Button).setOnClickListener {
            if (setCount(-1)) {
                countTextView.text = count.toString()
                errorTextView.visibility = View.GONE
            } else {
                errorTextView.text = "Negative counts not allowed"
                errorTextView.visibility = View.VISIBLE
            }
        }
    }
}
