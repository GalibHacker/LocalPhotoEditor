package com.openroom.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class EditParamsTest {
    @Test
    fun serializationRoundTrip() {
        val original = EditParams(exposure = 1.25f, temperature = -0.3f, saturation = 0.8f)
        assertEquals(original, EditParamsJson.decode(EditParamsJson.encode(original)))
    }

    @Test
    fun historySupportsUndoAndRedo() {
        val history = EditHistory()
        val edited = EditParams(exposure = 1f)
        history.update(edited)
        assertEquals(EditParams(), history.undo())
        assertEquals(edited, history.redo())
    }
}
