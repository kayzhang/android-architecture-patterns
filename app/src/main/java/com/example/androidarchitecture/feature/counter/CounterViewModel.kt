package com.example.androidarchitecture.feature.counter

import com.example.androidarchitecture.core.mvi.BaseMviViewModel

/**
 * CounterViewModel — Redux-like MVI, extends BaseMviViewModel.
 *
 * Implements two abstract methods:
 * - handleSideEffect(): IMPURE — mutates Model, returns SideEffectResult + optional Effect
 * - reduce():           PURE  — maps (State, SideEffectResult) → new State
 *
 * The base class orchestrates the flow:
 *   Intent → handleSideEffect → (SideEffectResult, Effect?) → reduce → State
 */
class CounterViewModel : BaseMviViewModel<CounterState, CounterIntent, CounterSideEffectResult, CounterEffect>(
    initialState = CounterState()
) {
    private val model = CounterModel()

    /**
     * Side effect handler — IMPURE.
     * Mutates the Model and returns a SideEffectResult for the reducer,
     * plus an optional one-shot Effect for the UI.
     */
    override fun handleSideEffect(
        intent: CounterIntent
    ): Pair<CounterSideEffectResult, CounterEffect?> =
        when (intent) {
            is CounterIntent.Increment -> {
                model.increment()
                CounterSideEffectResult.CountUpdated(model.getCount()) to null
            }

            is CounterIntent.Reset -> {
                model.reset()
                CounterSideEffectResult.CountUpdated(model.getCount()) to null
            }

            is CounterIntent.SetCount -> {
                model.setCount(intent.value).fold(
                    onSuccess = {
                        CounterSideEffectResult.CountUpdated(model.getCount()) to null
                    },
                    onFailure = { e ->
                        CounterSideEffectResult.SetCountFailed(e.message ?: "Error") to
                            CounterEffect.ShowToast(e.message ?: "Error")
                    }
                )
            }
        }

    /**
     * Pure reducer: (State, SideEffectResult) → State.
     * No side effects, no model calls. Same input always produces same output.
     */
    override fun reduce(
        oldState: CounterState,
        result: CounterSideEffectResult
    ): CounterState =
        when (result) {
            is CounterSideEffectResult.CountUpdated ->
                oldState.copy(count = result.count, error = null)

            is CounterSideEffectResult.SetCountFailed ->
                oldState.copy(error = result.message)
        }
}
