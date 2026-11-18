package com.openroom.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.openroom.ui.editor.EditorScreen
import com.openroom.ui.library.LibraryScreen

@Composable
fun OpenRoomNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = "library") {
        composable("library") {
            LibraryScreen(onOpen = { navController.navigate("editor/${it.substringAfterLast('/')}") })
        }
        composable("editor/{photoId}") { EditorScreen(onBack = { navController.popBackStack() }) }
    }
}
