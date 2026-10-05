package com.example.androidarchitecture.feature.counter

import android.content.Context
import androidx.loader.content.Loader

/**
 * ControllerLoader — retains the CounterController across configuration changes.
 *
 * LoaderManager keeps this Loader alive when the Activity is destroyed and
 * recreated (e.g., on rotation). The Controller inside it survives with its
 * Model and state intact.
 *
 * On first launch: creates a fresh Model + Controller.
 * On rotation: delivers the same Controller instance to the new Activity.
 *
 * This was the standard approach before Jetpack's ViewModel class existed.
 * ViewModel replaces this pattern entirely — zero boilerplate, same result.
 */
class ControllerLoader(
    context: Context
) : Loader<CounterController>(context) {

    private var controller: CounterController? = null

    override fun onStartLoading() {
        if (controller == null) {
            val model = CounterModel()
            controller = CounterController(model)
        }
        controller?.let { deliverResult(it) }
    }
}
