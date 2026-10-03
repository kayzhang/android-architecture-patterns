package com.example.androidarchitecture.feature.counter

/**
 * Effect — one-shot events that don't belong in State.
 *
 * State is persistent (always visible on screen).
 * Effects happen once and are gone (toast, navigation, snackbar).
 * They're delivered via Channel, consumed exactly once.
 */
sealed interface CounterEffect {
    data class ShowToast(val message: String) : CounterEffect
    // e.g., data object NavigateToDetail : CounterEffect
}
