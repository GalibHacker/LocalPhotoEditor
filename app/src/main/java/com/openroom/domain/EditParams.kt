package com.openroom.domain

import kotlinx.serialization.Serializable

@Serializable
data class EditParams(
    val exposure: Float = 0f,
    val temperature: Float = 0f,
    val tint: Float = 0f,
    val contrast: Float = 0f,
    val highlights: Float = 0f,
    val shadows: Float = 0f,
    val whites: Float = 0f,
    val blacks: Float = 0f,
    val vibrance: Float = 0f,
    val saturation: Float = 0f
)
