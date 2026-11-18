package com.openroom.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {
    @Query("SELECT * FROM photos WHERE rejected = 0 ORDER BY dateAdded DESC")
    fun observePhotos(): Flow<List<PhotoEntity>>

    @Query("SELECT * FROM photos WHERE uri = :uri LIMIT 1")
    suspend fun find(uri: String): PhotoEntity?

    @Upsert
    suspend fun upsert(photo: PhotoEntity)
}
