package com.example.androidarchitecture.feature.counter

/**
 * State — immutable snapshot of the counter screen.
 * Single source of truth for everything the View needs to render.
 */
data class CounterState(
    val count: Int = 0,
    val error: String? = null
)
