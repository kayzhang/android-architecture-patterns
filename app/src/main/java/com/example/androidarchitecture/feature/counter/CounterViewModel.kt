package com.example.androidarchitecture.feature.counter

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * CounterViewModel — the core of MVVM.
 *
 * Data flow is UNIDIRECTIONAL in principle (state down, events up):
 * - Events flow up:  View → ViewModel (calls increment(), reset(), etc.)
 * - State flows down: Model → ViewModel → View (via reactive StateFlow)
 * However, this is not strictly enforced — any method can update any
 * StateFlow at any time. There's no single path for all state changes.
 * Compare with MVI where ALL changes go through Intent → Reducer → State.
 *
 * Because the Model is reactive (exposes StateFlow), the ViewModel can pass
 * it through directly to the View — no manual sync needed.
 *
 * The View observes MULTIPLE StateFlows — one per piece of state (count, error).
 * Compare with MVI where the View observes ONE State object containing everything.
 *
 * ViewModel has ZERO reference to the View — no interface, no Activity,
 * no callbacks. Survives configuration changes automatically.
 */
class CounterViewModel : ViewModel() {

    private val model = CounterModel()

    /**
     * Count — observed DIRECTLY from the Model's reactive StateFlow.
     * No manual sync needed. When model.increment() changes the count,
     * this StateFlow automatically reflects the new value.
     */
    val count: StateFlow<Int> = model.count

    /**
     * Error — UI-only state, managed by ViewModel (not in the Model).
     * This is a separate StateFlow from count. The View must observe both.
     */
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun increment() {
        model.increment()
        _error.value = null
        // No need to sync count — model.count StateFlow already updated
    }

    fun reset() {
        model.reset()
        _error.value = null
    }

    fun setCount(value: Int) {
        model.setCount(value)
            .onSuccess { _error.value = null }
            .onFailure { e -> _error.value = e.message }
    }
}
