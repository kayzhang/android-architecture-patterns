package com.example.androidarchitecture.feature.counter

/**
 * State — immutable snapshot of the counter screen.
 * Contains everything the View needs to render in ONE object.
 *
 * Compare with MVVM where state is spread across multiple StateFlows
 * (count, error). In MVI, the View observes this single State — there's
 * only ONE reactive stream, not multiple.
 *
 * The reducer produces a new State after each Intent by calling the
 * CounterModel (data layer) and mapping the result.
 */
data class CounterState(
    val count: Int = 0,
    val error: String? = null
)
