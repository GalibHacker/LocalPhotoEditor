package com.openroom.ui.library

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun LibraryScreen(onOpen: (String) -> Unit, viewModel: LibraryViewModel = hiltViewModel()) {
    val photos by viewModel.photos.collectAsStateWithLifecycle()
    Scaffold(topBar = { TopAppBar(title = { Text("OpenRoom") }) }) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(120.dp),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            items(photos, key = { it.uri }) { photo ->
                Card(Modifier.clickable { onOpen(photo.uri) }) {
                    AsyncImage(model = photo.uri, contentDescription = photo.displayName)
                }
            }
        }
    }
}
