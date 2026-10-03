package com.example.androidarchitecture.feature.counter

/**
 * Model — holds data, operations, and business rule validation.
 * A plain class with no reactive API and no Android dependencies.
 *
 * In MVC, the Model is not reactive. The Controller reads from the Model
 * imperatively (model.getCount()) and pushes the result to the View.
 * There is no automatic notification when data changes — the Controller
 * is responsible for keeping the View up to date after every operation.
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
