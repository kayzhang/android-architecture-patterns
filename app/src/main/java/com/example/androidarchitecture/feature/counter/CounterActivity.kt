package com.example.androidarchitecture.feature.counter

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import com.example.androidarchitecture.R

/**
 * CounterActivity — the "View" in the Loader-based MVC variant.
 *
 * Implements CounterView interface so the retained Controller can call
 * UI methods without knowing the concrete Activity class.
 *
 * Uses LoaderManager to retain the Controller across configuration changes:
 * - onCreateLoader: creates a fresh ControllerLoader (first launch only)
 * - onLoadFinished: re-attaches the new Activity to the retained Controller
 * - onDestroy: detaches the View to prevent leaking the destroyed Activity
 *
 * This is the legacy approach. Today's ViewModel class replaces all of this
 * boilerplate — see the MVVM section.
 */
class CounterActivity : AppCompatActivity(), CounterView {

    companion object {
        private const val LOADER_ID = 1001
    }

    private var controller: CounterController? = null
    private lateinit var countTextView: TextView
    private lateinit var errorTextView: TextView

    private val loaderCallbacks = object : LoaderManager.LoaderCallbacks<CounterController> {
        override fun onCreateLoader(id: Int, args: Bundle?): Loader<CounterController> {
            return ControllerLoader(this@CounterActivity)
        }

        override fun onLoadFinished(loader: Loader<CounterController>, data: CounterController?) {
            // CRITICAL: always re-attach the new Activity instance to the retained Controller.
            // Without this, the Controller still holds a reference to the destroyed Activity
            // and any UI calls would either silently do nothing or crash.
            controller = data
            controller?.attachView(this@CounterActivity)
        }

        override fun onLoaderReset(loader: Loader<CounterController>) {
            controller?.detachView()
            controller = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter)

        countTextView = findViewById(R.id.countTextView)
        errorTextView = findViewById(R.id.errorTextView)

        // Use the AndroidX LoaderManager API (not the deprecated Activity.loaderManager property)
        LoaderManager.getInstance(this).initLoader(LOADER_ID, null, loaderCallbacks)

        findViewById<Button>(R.id.incrementButton).setOnClickListener {
            controller?.onIncrementClicked()
        }
        findViewById<Button>(R.id.resetButton).setOnClickListener {
            controller?.onResetClicked()
        }
        findViewById<Button>(R.id.setToNeg1Button).setOnClickListener {
            controller?.onSetCountClicked(-1)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Detach the view to avoid leaking this Activity instance while the
        // Loader (and Controller) continue to live in the background.
        controller?.detachView()
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
