package com.openroom.ui.editor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.Button
import androidx.compose.material3.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun EditorScreen(onBack: () -> Unit, viewModel: EditorViewModel = remember { EditorViewModel() }) {
    val params by viewModel.params.collectAsStateWithLifecycle()
    Column(
        Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Editor")
        Text("Non-destructive adjustments")
        Slider(
            value = params.exposure,
            onValueChange = { value -> viewModel.update { it.copy(exposure = value) } },
            valueRange = -4f..4f
        )
        Text("Exposure ${"%.2f".format(params.exposure)}")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = viewModel::undo) { Text("Undo") }
            Button(onClick = viewModel::redo) { Text("Redo") }
            Button(onClick = onBack) { Text("Library") }
        }
    }
}
