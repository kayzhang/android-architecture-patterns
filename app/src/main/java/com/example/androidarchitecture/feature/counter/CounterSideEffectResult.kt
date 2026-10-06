package com.example.androidarchitecture.feature.counter

/**
 * SideEffectResult — the output of a side effect, fed into the pure reducer.
 *
 * The flow is: Intent → handleSideEffect() → SideEffectResult → reduce() → State.
 *
 * Intents represent what the user WANTS to do (user actions).
 * SideEffectResults represent what HAPPENED after executing the side effect.
 * The reducer only sees SideEffectResults, never raw Intents — keeping it pure.
 */
sealed interface CounterSideEffectResult {
    data class CountUpdated(val count: Int) : CounterSideEffectResult
    data class SetCountFailed(val message: String) : CounterSideEffectResult
}
