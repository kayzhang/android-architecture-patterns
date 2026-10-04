package com.example.androidarchitecture.feature.counter

/**
 * Presenter — holds a reference to the View INTERFACE (not a concrete Activity).
 *
 * Data flow is MULTI-DIRECTIONAL (same as MVC, but through an interface):
 * - User action flows: View → Presenter → Model
 * - Data flows back:   Model → Presenter → View (Presenter pushes)
 * The Presenter both reads from the Model AND pushes to the View interface.
 *
 * What changed from MVC:
 * - View is an interface, not a concrete class → testable with a mock
 * - View is nullable and attached/detached → lifecycle-ready structure
 * - Presenter can be tested in a plain JUnit test (no Android runtime needed)
 *
 * About attachView/detachView:
 * Note: In this example the Presenter is NOT retained — it's recreated on
 * every rotation, just like MVC. The attachView/detachView structure exists
 * so it COULD be retained (e.g., via a Loader) if needed. Detaching prevents
 * memory leaks of the old Activity, attaching reconnects the new one.
 * MVVM makes this automatic with ViewModel.
 *
 * What's still not ideal (leads to MVVM):
 * - Presenter must manually push every update: view?.updateCounter(...)
 * - Must manually manage attach/detach lifecycle
 * - Miss one push call = stale UI
 */
class CounterPresenter(private val model: CounterModel) {

    private var view: CounterView? = null

    fun attachView(view: CounterView) {
        this.view = view
        view.updateCounter(model.getCount())
    }

    fun detachView() {
        this.view = null
    }

    fun onIncrementClicked() {
        model.increment()
        view?.updateCounter(model.getCount())
        view?.clearError()
    }

    fun onResetClicked() {
        model.reset()
        view?.updateCounter(model.getCount())
        view?.clearError()
    }

    fun onSetCountClicked(value: Int) {
        model.setCount(value)
            .onSuccess {
                view?.updateCounter(model.getCount())
                view?.clearError()
            }
            .onFailure { e ->
                view?.showError(e.message ?: "Error")
            }
    }
}
