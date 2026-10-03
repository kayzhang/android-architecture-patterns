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
 * CounterViewModel — lightweight MVI pattern.
 *
 * Data flow is STRICTLY UNIDIRECTIONAL and enforced:
 * - View emits Intent → processIntent() → Reducer → new State → View observes
 * ALL state changes go through this single path. There is no other way to
 * update the State — no scattered setState calls, no multiple entry points.
 * Compare with MVVM where any method can update any StateFlow.
 *
 * - Single State object (CounterState) — single source of truth
 * - Single entry point (processIntent) — all user actions go through here
 * - Reducer calls Model and maps result into new State
 * - One-shot effects (toast, navigation) delivered via Channel, not State
 * - Survives configuration changes automatically (ViewModel class feature)
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
        _state.update { oldState -> reduce(oldState, intent) }
    }

    private fun reduce(
        oldState: CounterState,
        intent: CounterIntent
    ): CounterState =
        when (intent) {
            is CounterIntent.Increment -> {
                model.increment()
                oldState.copy(count = model.getCount(), error = null)
            }

            is CounterIntent.Reset -> {
                model.reset()
                oldState.copy(count = model.getCount(), error = null)
            }

            is CounterIntent.SetCount -> {
                model.setCount(intent.value).fold(
                    onSuccess = {
                        oldState.copy(count = model.getCount(), error = null)
                    },
                    onFailure = { e ->
                        postEffect(CounterEffect.ShowToast(e.message ?: "Error"))
                        oldState.copy(error = e.message)
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
