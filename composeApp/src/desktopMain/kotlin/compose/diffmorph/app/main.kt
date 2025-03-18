package compose.diffmorph.app

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import compose.diffmorph.app.ui.screen.HomeScreen

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "DiffMorph",
    ) {
        HomeScreen()
    }
}
