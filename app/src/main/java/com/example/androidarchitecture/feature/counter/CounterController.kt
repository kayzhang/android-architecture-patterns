package com.example.androidarchitecture.feature.counter

/**
 * Controller — Loader-retained variant using a View interface.
 *
 * Unlike the basic MVC Controller (which holds a direct reference to
 * CounterActivity), this version uses a CounterView interface so it can
 * be safely retained by a Loader across configuration changes.
 *
 * After rotation, LoaderManager reattaches the same Controller instance,
 * but it still holds a reference to the OLD (destroyed) Activity.
 * The View interface + attachView/detachView solves this:
 * - detachView() in onDestroy nulls the reference (no leak)
 * - attachView() in onLoadFinished reconnects the new Activity
 *
 * Data flow is the same as basic MVC (Controller mediates everything),
 * but the View reference is now an interface, not a concrete class.
 */
class CounterController(private val model: CounterModel) {

    private var view: CounterView? = null

    // Track the last error so it can be restored after rotation.
    // The Model holds the count, but error state is UI-level —
    // the Controller must track it separately.
    private var lastError: String? = null

    fun attachView(view: CounterView) {
        this.view = view
        // Sync the new View with the full current state
        view.updateCounter(model.getCount())
        if (lastError != null) {
            view.showError(lastError!!)
        } else {
            view.clearError()
        }
    }

    fun detachView() {
        this.view = null
    }

    fun onIncrementClicked() {
        model.increment()
        lastError = null
        view?.updateCounter(model.getCount())
        view?.clearError()
    }

    fun onResetClicked() {
        model.reset()
        lastError = null
        view?.updateCounter(model.getCount())
        view?.clearError()
    }

    fun onSetCountClicked(value: Int) {
        model.setCount(value)
            .onSuccess {
                lastError = null
                view?.updateCounter(model.getCount())
                view?.clearError()
            }
            .onFailure { e ->
                lastError = e.message
                view?.showError(e.message ?: "Error")
            }
    }
}
