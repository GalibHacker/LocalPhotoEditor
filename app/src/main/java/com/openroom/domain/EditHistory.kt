package com.openroom.domain

class EditHistory(initial: EditParams = EditParams()) {
    private val undo = ArrayDeque<EditParams>()
    private val redo = ArrayDeque<EditParams>()
    var current: EditParams = initial
        private set

    fun update(next: EditParams) {
        if (next == current) return
        undo.addLast(current)
        current = next
        redo.clear()
    }

    fun canUndo() = undo.isNotEmpty()
    fun canRedo() = redo.isNotEmpty()

    fun undo(): EditParams {
        if (undo.isEmpty()) return current
        redo.addLast(current)
        current = undo.removeLast()
        return current
    }

    fun redo(): EditParams {
        if (redo.isEmpty()) return current
        undo.addLast(current)
        current = redo.removeLast()
        return current
    }
}
