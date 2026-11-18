package com.openroom.ui.editor

import androidx.lifecycle.ViewModel
import com.openroom.domain.EditHistory
import com.openroom.domain.EditParams
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class EditorViewModel : ViewModel() {
    private val history = EditHistory()
    private val _params = MutableStateFlow(history.current)
    val params = _params.asStateFlow()

    fun update(transform: (EditParams) -> EditParams) {
        history.update(transform(history.current))
        _params.value = history.current
    }

    fun undo() {
        _params.value = history.undo()
    }

    fun redo() {
        _params.value = history.redo()
    }

    fun reset() {
        update { EditParams() }
    }
}
