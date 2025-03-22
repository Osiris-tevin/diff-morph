package compose.diffmorph.app.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

object AppColors {
    val LightPrimary = pink100
    val LightSecondary = pink900
    val LightBackground = white
    val LightSurface = white850
    val LightOnPrimary = gray
    val LightOnSecondary = white
    val LightOnBackground = gray
    val LightOnSurface = gray

    val DarkPrimary = green900
    val DarkSecondary = green300
    val DarkBackground = gray
    val DarkSurface = white150
    val DarkOnPrimary = white
    val DarkOnSecondary = gray
    val DarkOnBackground = white
    val DarkOnSurface = white850
}

@Composable
fun AppTheme(isDarkTheme: Boolean, content: @Composable () -> Unit) {
    val colors = if (isDarkTheme) {
        darkColors(
            primary = AppColors.DarkPrimary,
            secondary = AppColors.DarkSecondary,
            background = AppColors.DarkBackground,
            surface = AppColors.DarkSurface,
            onPrimary = AppColors.DarkOnPrimary,
            onSecondary = AppColors.DarkOnSecondary,
            onBackground = AppColors.DarkOnBackground,
            onSurface = AppColors.DarkOnSurface
        )
    } else {
        lightColors(
            primary = AppColors.LightPrimary,
            secondary = AppColors.LightSecondary,
            background = AppColors.LightBackground,
            surface = AppColors.LightSurface,
            onPrimary = AppColors.LightOnPrimary,
            onSecondary = AppColors.LightOnSecondary,
            onBackground = AppColors.LightOnBackground,
            onSurface = AppColors.LightOnSurface
        )
    }

    MaterialTheme(
        colors = colors,
        content = content
    )
}
