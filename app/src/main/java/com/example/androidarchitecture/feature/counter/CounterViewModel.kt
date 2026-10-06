package com.example.androidarchitecture.feature.counter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * CounterViewModel — lightweight MVI pattern with a pure reducer.
 *
 * Data flow is STRICTLY UNIDIRECTIONAL:
 *   View → Intent → handleSideEffect() → SideEffectResult → reduce() → State → View
 *
 * The key separation:
 * - handleSideEffect(): IMPURE — mutates the Model, returns a SideEffectResult + optional Effect
 * - reduce():           PURE  — maps (State + SideEffectResult) → new State, no side effects
 *
 * This means the reducer is deterministic and trivially testable:
 * given the same (oldState, sideEffectResult), it always returns the same newState.
 * Side effect logic is tested separately through the ViewModel's public API.
 *
 * Compare with MVVM where any method can update any StateFlow — here ALL
 * state changes flow through a single path via reduce().
 */
class CounterViewModel : ViewModel() {

    private val model = CounterModel()

    private val _state = MutableStateFlow(CounterState())
    val state: StateFlow<CounterState> = _state.asStateFlow()

    // Channel ensures effects are consumed exactly once and not dropped
    // if emitted while the UI is briefly in the background.
    private val _effect = Channel<CounterEffect>(capacity = Channel.BUFFERED)
    val effect: Flow<CounterEffect> = _effect.receiveAsFlow()

    fun processIntent(intent: CounterIntent) {
        val (result, effect) = handleSideEffect(intent)
        _state.update { oldState -> reduce(oldState, result) }
        effect?.let { postEffect(it) }
    }

    /**
     * Pure reducer: (State, SideEffectResult) → State.
     * No side effects, no model calls. Same input always produces same output.
     */
    private fun reduce(
        oldState: CounterState,
        result: CounterSideEffectResult
    ): CounterState =
        when (result) {
            is CounterSideEffectResult.CountUpdated ->
                oldState.copy(count = result.count, error = null)

            is CounterSideEffectResult.SetCountFailed ->
                oldState.copy(error = result.message)
        }

    /**
     * Side effect handler: (Intent) → (SideEffectResult, Effect?).
     * IMPURE — mutates the Model and produces a SideEffectResult for the reducer,
     * plus an optional one-shot Effect for the UI.
     */
    private fun handleSideEffect(
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

    private fun postEffect(effect: CounterEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}
