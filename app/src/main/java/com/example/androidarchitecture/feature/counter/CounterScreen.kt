package com.example.androidarchitecture.feature.counter

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

/**
 * CounterScreen — launches the CounterActivity (XML Views + MVC + Loader).
 * The actual MVC implementation lives in CounterActivity, CounterController,
 * ControllerLoader, and CounterModel — using Activity + XML layout, not Compose.
 */
@Composable
fun CounterScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Counter (MVC + Loader)",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Uses Activity + XML Views + Controller + Loader retention",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
        Button(
            onClick = {
                context.startActivity(Intent(context, CounterActivity::class.java))
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Open Counter")
        }
    }
}
