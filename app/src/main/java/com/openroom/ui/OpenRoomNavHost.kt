package com.openroom.ui

import androidx.compose.runtime.Composable
import android.net.Uri
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.openroom.ui.editor.EditorScreen
import com.openroom.ui.library.LibraryScreen

@Composable
fun OpenRoomNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = "library") {
        composable("library") {
            LibraryScreen(onOpen = { uri -> navController.navigate("editor/${Uri.encode(uri)}") })
        }
        composable("editor/{photoUri}") { entry ->
            val photoUri = Uri.decode(entry.arguments?.getString("photoUri").orEmpty())
            EditorScreen(
                photoUri = photoUri,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
