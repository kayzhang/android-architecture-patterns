package com.example.androidarchitecture.feature.counter

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * Stateful wrapper — connects ViewModel to the stateless CounterContent below.
 * This composable manages state (observes ViewModel), so CounterContent doesn't have to.
 *
 * Observes TWO things:
 * - viewState (StateFlow) → persistent screen state, auto-recomposes
 * - viewEffect (Channel) → one-shot events, consumed exactly once
 */
@Composable
fun CounterScreen(
    modifier: Modifier = Modifier,
    viewModel: CounterViewModel = viewModel()
) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()

    // Collect one-shot effects (toast, navigation)
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(viewModel.viewEffect, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.viewEffect.collect { effect ->
                when (effect) {
                    is CounterEffect.ShowToast ->
                        Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    CounterContent(
        state = state,
        onIntent = viewModel::processIntent,
        modifier = modifier
    )
}

/**
 * Stateless content — receives state + intent callback as parameters.
 * Doesn't know about ViewModel, StateFlow, or anything else.
 * Just renders whatever it's given — easy to @Preview with fake data.
 */
@Composable
private fun CounterContent(
    state: CounterState,
    onIntent: (CounterIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = state.count.toString(),
            style = MaterialTheme.typography.displayLarge
        )

        state.error?.let { error ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = error, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { onIntent(CounterIntent.Increment) }) {
            Text("+1")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { onIntent(CounterIntent.Reset) }) {
            Text("Reset")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { onIntent(CounterIntent.SetCount(-1)) }) {
            Text("Set to -1 (error + toast)")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CounterContentPreview() {
    MaterialTheme {
        CounterContent(
            state = CounterState(count = 42),
            onIntent = {}
        )
    }
}
