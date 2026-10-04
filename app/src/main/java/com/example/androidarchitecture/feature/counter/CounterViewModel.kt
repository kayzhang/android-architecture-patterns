package com.example.androidarchitecture.feature.counter

import com.example.androidarchitecture.core.mvi.BaseMviViewModel

/**
 * CounterViewModel — Redux-like MVI, extends BaseMviViewModel.
 *
 * Compare with lightweight MVI:
 * - Lightweight: writes its own StateFlow + processIntent + reduce inline
 * - Redux-like: extends BaseMviViewModel which provides the infrastructure,
 *   only implements reduce() — the pure function
 *
 * The reduce function returns Pair(newState, effect?):
 * - newState → goes to StateFlow (persistent, View observes)
 * - effect → goes to Channel (one-shot, View consumes once)
 */
class CounterViewModel : BaseMviViewModel<CounterState, CounterIntent, CounterEffect>(
    initialState = CounterState()
) {
    private val model = CounterModel()

    override fun reduce(
        oldState: CounterState,
        intent: CounterIntent
    ): Pair<CounterState, CounterEffect?> {
        return when (intent) {
            is CounterIntent.Increment -> {
                model.increment()
                CounterState(count = model.getCount()) to null
            }

            is CounterIntent.Reset -> {
                model.reset()
                CounterState(count = model.getCount()) to null
            }

            is CounterIntent.SetCount -> {
                model.setCount(intent.value).fold(
                    onSuccess = {
                        CounterState(count = model.getCount()) to null
                    },
                    onFailure = { e ->
                        oldState.copy(error = e.message) to
                            CounterEffect.ShowToast(e.message ?: "Error")
                    }
                )
            }
        }
    }
}
