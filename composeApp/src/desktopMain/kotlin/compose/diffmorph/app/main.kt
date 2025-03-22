package compose.diffmorph.app

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import compose.diffmorph.app.ui.screen.HomeScreen

fun main() = application {
    val windowState = rememberWindowState(placement = WindowPlacement.Maximized)

    Window(
        onCloseRequest = ::exitApplication,
        title = "DiffMorph",
        state = windowState
    ) {
        HomeScreen()
    }
}
