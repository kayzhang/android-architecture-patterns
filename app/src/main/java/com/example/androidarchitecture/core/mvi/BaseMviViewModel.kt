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
 * BaseMviViewModel — a reusable generic base for Redux-like MVI.
 *
 * Every screen defines its own State (S), Intent (I), and optional Effect (E),
 * then extends this class and implements reduce(). This ensures every feature
 * follows the same MVI structure consistently.
 *
 * @param S The screen's State type (immutable data class)
 * @param I The screen's Intent type (sealed interface of user actions)
 * @param E One-shot Effects the UI handles exactly once (toast, navigation)
 */
abstract class BaseMviViewModel<S, I, E>(
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
     * State is updated via StateFlow.update(), which is atomic.
     * The effect is captured from the reduce result and emitted AFTER
     * the new state has been committed — keeping the reduce function
     * side-effect free.
     */
    fun processIntent(intent: I) {
        var pendingEffect: E? = null
        _viewState.update { oldState ->
            val (newState, effect) = reduce(oldState, intent)
            pendingEffect = effect
            newState
        }
        pendingEffect?.let { postEffect(it) }
    }

    /**
     * The child ViewModel implements this — the pure reducer.
     * Takes (oldState + intent) and returns (newState + optional effect).
     */
    protected abstract fun reduce(oldState: S, intent: I): Pair<S, E?>

    protected fun postEffect(effect: E) {
        viewModelScope.launch {
            _viewEffect.send(effect)
        }
    }
}
