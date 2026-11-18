package com.openroom.data

import android.content.ContentResolver
import android.provider.MediaStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PhotoRepository @Inject constructor(
    private val dao: PhotoDao,
    private val resolver: ContentResolver
) {
    fun observePhotos(): Flow<List<PhotoEntity>> = dao.observePhotos()

    suspend fun importFromMediaStore() {
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED
        )
        resolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            "${MediaStore.Images.Media.DATE_ADDED} DESC"
        )?.use { cursor ->
            val id = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val date = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)
            while (cursor.moveToNext()) {
                val uri = "${MediaStore.Images.Media.EXTERNAL_CONTENT_URI}/${cursor.getLong(id)}"
                dao.upsert(PhotoEntity(uri, cursor.getString(name), cursor.getLong(date)))
            }
        }
    }
}
