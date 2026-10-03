package com.example.androidarchitecture.feature.counter

/**
 * Controller — holds a DIRECT reference to the concrete Activity (the View).
 *
 * Data flow is MULTI-DIRECTIONAL:
 * - User action flows: View → Controller → Model
 * - Data flows back:   Model → Controller → View (Controller pushes)
 * The Controller both reads from the Model AND pushes to the View,
 * so data moves in multiple directions through the Controller.
 *
 * The Controller knows the exact class (CounterActivity), not an interface.
 * This means:
 * - Tight coupling: Controller cannot work with any other View implementation
 * - Hard to test: Controller requires a CounterActivity instance, so you need
 *   the Android runtime to test it (instrumented test on device/emulator).
 *   Since it's a concrete class (not an interface), you can't substitute a
 *   lightweight mock/fake for fast unit tests either.
 * - State loss on configuration changes: Controller is created in onCreate(),
 *   so it's destroyed and recreated along with the Activity
 *
 * These limitations are what led to the evolution toward MVP (View interface),
 * MVVM (reactive state), and MVI (unidirectional flow).
 *
 * NOTE: There is intentionally no CounterControllerTest in this project.
 * The Controller cannot be unit tested without the Android runtime because
 * it holds a direct reference to CounterActivity (a concrete Android class).
 * You would need an instrumented test running on a device or emulator.
 * This is one of the key testability limitations that MVP solves by
 * introducing a View interface the Presenter can be tested against.
 */
class CounterController(
    private val view: CounterActivity,
    private val model: CounterModel
) {
    fun onIncrementClicked() {
        model.increment()
        view.updateCounter(model.getCount())
        view.clearError()
    }

    fun onResetClicked() {
        model.reset()
        view.updateCounter(model.getCount())
        view.clearError()
    }

    fun onSetCountClicked(value: Int) {
        model.setCount(value)
            .onSuccess {
                view.updateCounter(model.getCount())
                view.clearError()
            }
            .onFailure { e ->
                view.showError(e.message ?: "Error")
            }
    }
}
