package com.openroom.ui.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openroom.data.PhotoEntity
import com.openroom.data.PhotoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class LibraryViewModel @Inject constructor(repository: PhotoRepository) : ViewModel() {
    val photos = repository.observePhotos().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList<PhotoEntity>())
    init { viewModelScope.launch { repository.importFromMediaStore() } }
}
