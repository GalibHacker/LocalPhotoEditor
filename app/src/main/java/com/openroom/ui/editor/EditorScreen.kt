package com.openroom.ui.editor

import android.opengl.GLSurfaceView
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Row
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openroom.render.EditorRenderer

@Composable
fun EditorScreen(
    photoUri: String,
    onBack: () -> Unit,
    viewModel: EditorViewModel = remember { EditorViewModel() }
) {
    val params by viewModel.params.collectAsStateWithLifecycle()
    val renderer = remember(photoUri) { EditorRenderer() }

    Column(
        Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Editor")
        Text("Non-destructive adjustments")
        AndroidView(
            modifier = Modifier.weight(1f).fillMaxSize(),
            factory = { context ->
                GLSurfaceView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    setEGLContextClientVersion(3)
                    setRenderer(renderer)
                    renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
                    renderer.loadTexture(context, photoUri)
                }
            },
            update = {
                renderer.params = params
                it.requestRender()
            }
        )
        Column(
            modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            AdjustmentSlider("Exposure", params.exposure, -4f..4f) { value ->
                viewModel.update { it.copy(exposure = value) }
            }
            AdjustmentSlider("Temperature", params.temperature, -1f..1f) { value ->
                viewModel.update { it.copy(temperature = value) }
            }
            AdjustmentSlider("Tint", params.tint, -1f..1f) { value ->
                viewModel.update { it.copy(tint = value) }
            }
            AdjustmentSlider("Contrast", params.contrast, -1f..1f) { value ->
                viewModel.update { it.copy(contrast = value) }
            }
            AdjustmentSlider("Highlights", params.highlights, -1f..1f) { value ->
                viewModel.update { it.copy(highlights = value) }
            }
            AdjustmentSlider("Shadows", params.shadows, -1f..1f) { value ->
                viewModel.update { it.copy(shadows = value) }
            }
            AdjustmentSlider("Whites", params.whites, -1f..1f) { value ->
                viewModel.update { it.copy(whites = value) }
            }
            AdjustmentSlider("Blacks", params.blacks, -1f..1f) { value ->
                viewModel.update { it.copy(blacks = value) }
            }
            AdjustmentSlider("Vibrance", params.vibrance, -1f..1f) { value ->
                viewModel.update { it.copy(vibrance = value) }
            }
            AdjustmentSlider("Saturation", params.saturation, -1f..1f) { value ->
                viewModel.update { it.copy(saturation = value) }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = viewModel::undo) { Text("Undo") }
            Button(onClick = viewModel::redo) { Text("Redo") }
            Button(onClick = onBack) { Text("Library") }
        }
    }
}

@Composable
private fun AdjustmentSlider(
    label: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit
) {
    Slider(
        value = value,
        onValueChange = onValueChange,
        valueRange = range
    )
    Text("$label ${"%.2f".format(value)}")
}
