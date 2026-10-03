package com.example.androidarchitecture.feature.counter

/**
 * Intent — all possible user actions on the counter screen.
 * Sealed interface ensures the compiler checks that every intent is handled
 * in the reducer. Adding a new intent forces you to handle it — no forgotten cases.
 *
 * Note: "Intent" here is an MVI concept (the user's intention), NOT
 * Android's android.content.Intent (the system messaging class for starting
 * Activities, Services, sending Broadcasts, etc.).
 */
sealed interface CounterIntent {
    data object Increment : CounterIntent
    data object Reset : CounterIntent
    data class SetCount(val value: Int) : CounterIntent
}
