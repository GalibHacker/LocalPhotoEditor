package com.openroom.domain

import kotlinx.serialization.json.Json

object EditParamsJson {
    private val format = Json { encodeDefaults = true; ignoreUnknownKeys = true }
    fun encode(value: EditParams): String = format.encodeToString(value)
    fun decode(value: String): EditParams = format.decodeFromString(value)
}
