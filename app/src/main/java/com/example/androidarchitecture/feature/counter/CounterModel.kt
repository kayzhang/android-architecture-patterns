package com.example.androidarchitecture.feature.counter

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Model — holds data, operations, and business rule validation.
 * REACTIVE: exposes data as StateFlow that the ViewModel can observe directly.
 *
 * This is the key difference from MVC/MVP Models (which are plain classes):
 * in MVVM, the standard practice is for the Model (or Repository) to expose
 * reactive streams. The ViewModel observes these streams and passes them
 * to the View — no manual sync needed.
 *
 * In a real app, this would be a Repository backed by Room, DataStore, or
 * a network API. Room DAOs return Flow<T> natively, so the reactivity
 * comes built-in — you don't need to wrap anything manually.
 *
 * We use StateFlow here to simulate a reactive data source.
 */
class CounterModel {

    private val _count = MutableStateFlow(0)

    /** Observable count — in a real app, this would be a Room DAO Flow. */
    val count: StateFlow<Int> = _count.asStateFlow()

    /**
     * Use MutableStateFlow.update {} for read-modify-write operations.
     * _count.value++ would be a non-atomic read-modify-write — it reads
     * the current value, increments it, then writes it back. If two
     * coroutines call increment() concurrently, one update could be lost.
     * update {} is atomic: it retries on CAS (Compare-And-Swap) failure.
     */
    fun increment() { _count.update { it + 1 } }
    fun reset() { _count.value = 0 }

    fun setCount(value: Int): Result<Unit> {
        return if (value >= 0) {
            _count.value = value
            Result.success(Unit)
        } else {
            Result.failure(IllegalArgumentException("Negative counts not allowed"))
        }
    }
}
