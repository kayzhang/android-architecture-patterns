package com.example.androidarchitecture.feature.counter

/**
 * Model — holds data, operations, and business rule validation.
 * A plain class with no reactive API and no Android dependencies.
 *
 * Unlike MVVM's Model (which is reactive and exposes StateFlow), MVI's Model
 * does NOT need to be reactive. The reducer reads from the Model imperatively
 * (model.getCount()) and builds a new immutable State object.
 *
 * In MVI, reactivity lives at the STATE level (the ViewModel exposes one
 * StateFlow<CounterState>), not at the Model level. The Model is just a
 * plain data source that the reducer queries.
 */
class CounterModel {
    private var count = 0

    fun increment() { count++ }
    fun reset() { count = 0 }

    fun setCount(value: Int): Result<Unit> {
        return if (value >= 0) {
            count = value
            Result.success(Unit)
        } else {
            Result.failure(IllegalArgumentException("Negative counts not allowed"))
        }
    }

    fun getCount(): Int = count
}
