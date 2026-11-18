package com.openroom.di

import android.content.ContentResolver
import android.content.Context
import androidx.room.Room
import com.openroom.data.OpenRoomDatabase
import com.openroom.data.PhotoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideContentResolver(@ApplicationContext context: Context): ContentResolver =
        context.contentResolver

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): OpenRoomDatabase =
        Room.databaseBuilder(context, OpenRoomDatabase::class.java, "openroom.db").build()

    @Provides
    fun providePhotoDao(database: OpenRoomDatabase): PhotoDao = database.photoDao()
}
