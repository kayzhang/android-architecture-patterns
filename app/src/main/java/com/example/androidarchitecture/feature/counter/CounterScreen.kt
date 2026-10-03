package com.example.androidarchitecture.feature.counter

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * Counter screen — MVVM pattern.
 *
 * Compare with previous patterns:
 * - MVC/MVP: CounterScreen is a Compose launcher that starts an Activity
 * - MVVM: CounterScreen IS the UI — no Activity, no XML, pure Compose
 *
 * The View observes MULTIPLE StateFlows from the ViewModel (count, error).
 * Each piece of state is a separate observation. Compare with MVI where
 * the View observes ONE State object containing everything.
 *
 * When the Model's data changes, it flows automatically:
 * Model (reactive) → ViewModel (passes through) → View (observes and re-renders)
 */
@Composable
fun CounterScreen(
    modifier: Modifier = Modifier,
    viewModel: CounterViewModel = viewModel()
) {
    val count by viewModel.count.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()

    CounterContent(
        count = count,
        error = error,
        onIncrement = viewModel::increment,
        onReset = viewModel::reset,
        onSetCount = viewModel::setCount,
        modifier = modifier
    )
}

/**
 * Stateless content — receives individual values + callbacks.
 *
 * Compare with MVI's CounterContent which receives a single State object.
 * MVVM passes individual values because there's no unified State class.
 */
@Composable
private fun CounterContent(
    count: Int,
    error: String?,
    onIncrement: () -> Unit,
    onReset: () -> Unit,
    onSetCount: (Int) -> Unit,
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
            text = count.toString(),
            style = MaterialTheme.typography.displayLarge
        )

        error?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = it, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = onIncrement) {
            Text("+1")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onReset) {
            Text("Reset")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { onSetCount(-1) }) {
            Text("Set to -1 (error)")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CounterContentPreview() {
    MaterialTheme {
        CounterContent(
            count = 42,
            error = null,
            onIncrement = {},
            onReset = {},
            onSetCount = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CounterContentErrorPreview() {
    MaterialTheme {
        CounterContent(
            count = 0,
            error = "Negative counts not allowed",
            onIncrement = {},
            onReset = {},
            onSetCount = {}
        )
    }
}
