package com.example.androidarchitecture.feature.counter

/**
 * Model — holds data, operations, and business rule validation.
 * Same plain class as all other patterns.
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
