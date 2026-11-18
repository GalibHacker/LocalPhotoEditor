package com.openroom.ui.editor

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.Button
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun EditorScreen(
    photoId: String,
    onBack: () -> Unit,
    viewModel: EditorViewModel = remember { EditorViewModel() }
) {
    val params by viewModel.params.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editor") },
                navigationIcon = { TextButton(onClick = onBack) { Text("Back") } },
                actions = { TextButton(onClick = viewModel::reset) { Text("Reset") } }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AsyncImage(
                model = Uri.parse("content://media/external/images/media/$photoId"),
                contentDescription = "Selected photo",
                modifier = Modifier.fillMaxWidth()
            )
            Text("Non-destructive adjustments")
            HorizontalDivider()
            Adjustment("Exposure", params.exposure, -4f..4f) {
                value -> viewModel.update { it.copy(exposure = value) }
            }
            Adjustment("Temperature", params.temperature, -1f..1f) {
                value -> viewModel.update { it.copy(temperature = value) }
            }
            Adjustment("Tint", params.tint, -1f..1f) { value -> viewModel.update { it.copy(tint = value) } }
            Adjustment("Contrast", params.contrast, -1f..1f) { value -> viewModel.update { it.copy(contrast = value) } }
            Adjustment("Highlights", params.highlights, -1f..1f) { value -> viewModel.update { it.copy(highlights = value) } }
            Adjustment("Shadows", params.shadows, -1f..1f) { value -> viewModel.update { it.copy(shadows = value) } }
            Adjustment("Whites", params.whites, -1f..1f) { value -> viewModel.update { it.copy(whites = value) } }
            Adjustment("Blacks", params.blacks, -1f..1f) { value -> viewModel.update { it.copy(blacks = value) } }
            Adjustment("Vibrance", params.vibrance, -1f..1f) { value -> viewModel.update { it.copy(vibrance = value) } }
            Adjustment("Saturation", params.saturation, -1f..1f) { value -> viewModel.update { it.copy(saturation = value) } }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = viewModel::undo) { Text("Undo") }
                Button(onClick = viewModel::redo) { Text("Redo") }
            }
        }
    }
}

@Composable
private fun Adjustment(
    label: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit
) {
    Text("$label ${"%.2f".format(value)}")
    Slider(
        value = value,
        onValueChange = onValueChange,
        valueRange = range,
        modifier = Modifier.fillMaxWidth()
    )
}
