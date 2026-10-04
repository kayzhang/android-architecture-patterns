package com.example.androidarchitecture.feature.counter

/**
 * Intent — all possible user actions on the counter screen.
 * Sealed interface ensures the compiler checks every case in the reducer.
 */
sealed interface CounterIntent {
    data object Increment : CounterIntent
    data object Reset : CounterIntent
    data class SetCount(val value: Int) : CounterIntent
}
