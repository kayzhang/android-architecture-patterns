package com.example.androidarchitecture.feature.counter

/**
 * Model — holds data, operations, and business rule validation.
 * A plain class with no reactive API and no Android dependencies.
 *
 * Same as MVC's Model. In MVP, the Presenter reads from the Model
 * imperatively (model.getCount()) and pushes the result to the View
 * interface. Like MVC, there is no automatic notification — the Presenter
 * manages all updates manually.
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
