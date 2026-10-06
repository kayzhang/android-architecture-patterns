package com.example.androidarchitecture.core.mvi

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
 * BaseMviViewModel — a reusable generic base for Redux-like MVI with a pure reducer.
 *
 * Every screen defines its own State (S), Intent (I), SideEffectResult (R), and
 * optional Effect (E), then extends this class and implements:
 * - handleSideEffect(): (Intent) → Pair<SideEffectResult, Effect?> — IMPURE, executes side effects
 * - reduce(): (State, SideEffectResult) → State — PURE, maps results into new state
 *
 * The flow is: Intent → handleSideEffect → SideEffectResult → reduce → State
 *
 * @param S The screen's State type (immutable data class)
 * @param I The screen's Intent type (sealed interface of user actions)
 * @param R The screen's SideEffectResult type (sealed interface of side effect outcomes)
 * @param E One-shot Effects the UI handles exactly once (toast, navigation)
 */
abstract class BaseMviViewModel<S, I, R, E>(
    initialState: S
) : ViewModel() {

    private val _viewState = MutableStateFlow(initialState)
    val viewState: StateFlow<S> = _viewState.asStateFlow()

    // Channel ensures effects are consumed exactly once and not dropped
    // if emitted while the UI is briefly in the background.
    private val _viewEffect = Channel<E>(capacity = Channel.BUFFERED)
    val viewEffect: Flow<E> = _viewEffect.receiveAsFlow()

    protected val currentState: S
        get() = _viewState.value

    /**
     * Single entry point for all user actions.
     *
     * 1. handleSideEffect() executes impure logic and produces a SideEffectResult + optional Effect
     * 2. reduce() purely maps (oldState + result) into newState
     * 3. Effect (if any) is emitted to the Channel for one-shot UI consumption
     */
    fun processIntent(intent: I) {
        val (result, effect) = handleSideEffect(intent)
        _viewState.update { oldState -> reduce(oldState, result) }
        effect?.let { postEffect(it) }
    }

    /**
     * Side effect handler — IMPURE.
     * Executes Model mutations, API calls, etc. and returns a SideEffectResult
     * plus an optional one-shot Effect.
     */
    protected abstract fun handleSideEffect(intent: I): Pair<R, E?>

    /**
     * Pure reducer: (State, SideEffectResult) → State.
     * No side effects. Same input always produces the same output.
     */
    protected abstract fun reduce(oldState: S, result: R): S

    protected fun postEffect(effect: E) {
        viewModelScope.launch {
            _viewEffect.send(effect)
        }
    }
}
