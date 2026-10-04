package com.example.androidarchitecture.feature.counter

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.androidarchitecture.R

/**
 * CounterActivity — the "View" in MVP.
 *
 * Activity IMPLEMENTS CounterView interface — Presenter doesn't know
 * it's talking to an Activity. Model and Presenter created in onCreate()
 * — destroyed on configuration changes.
 *
 * Note: attachView/detachView are called at lifecycle events. In this simple
 * example the Presenter doesn't outlive the Activity, so they're not strictly
 * needed. They would matter if the Presenter were retained (e.g., via Loader).
 */
class CounterActivity : AppCompatActivity(), CounterView {

    private lateinit var presenter: CounterPresenter
    private lateinit var countTextView: TextView
    private lateinit var errorTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter)

        countTextView = findViewById(R.id.countTextView)
        errorTextView = findViewById(R.id.errorTextView)

        presenter = CounterPresenter(CounterModel())
        presenter.attachView(this)

        findViewById<Button>(R.id.incrementButton).setOnClickListener {
            presenter.onIncrementClicked()
        }
        findViewById<Button>(R.id.resetButton).setOnClickListener {
            presenter.onResetClicked()
        }
        findViewById<Button>(R.id.setToNeg1Button).setOnClickListener {
            presenter.onSetCountClicked(-1)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun updateCounter(count: Int) {
        countTextView.text = count.toString()
    }

    override fun showError(message: String) {
        errorTextView.text = message
        errorTextView.visibility = View.VISIBLE
    }

    override fun clearError() {
        errorTextView.text = ""
        errorTextView.visibility = View.GONE
    }
}
