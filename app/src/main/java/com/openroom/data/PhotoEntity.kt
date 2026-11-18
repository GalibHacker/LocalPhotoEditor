package com.openroom.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class PhotoEntity(
    @PrimaryKey val uri: String,
    val displayName: String,
    val dateAdded: Long,
    val rating: Int = 0,
    val rejected: Boolean = false,
    val editParamsJson: String = "{}"
)
