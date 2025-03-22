package compose.diffmorph.app.ui.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import compose.diffmorph.app.ui.page.HomePage
import compose.diffmorph.app.ui.theme.AppTheme
import diffmorph.composeapp.generated.resources.Res
import diffmorph.composeapp.generated.resources.history
import diffmorph.composeapp.generated.resources.ic_dark_mode_filled
import diffmorph.composeapp.generated.resources.ic_history_filled
import diffmorph.composeapp.generated.resources.theme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeScreen() {
    var isDarkTheme by rememberSaveable { mutableStateOf(false) }
    var isExpanded by remember { mutableStateOf(false) }

    AppTheme(isDarkTheme = isDarkTheme) {
        Row {
            Sidebar(
                isExpanded = isExpanded,
                onToggle = { isExpanded = !isExpanded },
                onThemeChange = { isDarkTheme = !isDarkTheme }
            )
            Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {
                HomePage()
            }
        }
    }
}

@Composable
private fun Sidebar(isExpanded: Boolean, onToggle: () -> Unit, onThemeChange: () -> Unit) {
    val sidebarWidth by animateDpAsState(if (isExpanded) 180.dp else 64.dp)

    Column(
        modifier = Modifier
            .width(sidebarWidth)
            .fillMaxHeight()
            .background(MaterialTheme.colors.primary)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        IconButton(onClick = onToggle) {
            Icon(
                imageVector = if (isExpanded) Icons.AutoMirrored.Filled.ArrowBack else Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Toggle Sidebar",
                tint = MaterialTheme.colors.onPrimary
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        SidebarItem(
            icon = painterResource(Res.drawable.ic_history_filled),
            title = stringResource(Res.string.history),
            isExpanded = isExpanded
        ) {
            println("This is History (Click Success!)")
        }
        SidebarItem(
            icon = painterResource(Res.drawable.ic_dark_mode_filled),
            title = stringResource(Res.string.theme),
            isExpanded = isExpanded
        ) {
            onThemeChange()
        }
    }
}

@Composable
private fun SidebarItem(
    icon: Any,
    title: String,
    isExpanded: Boolean,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    Row(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() }
            .hoverable(interactionSource = interactionSource)
            .animateContentSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (icon) {
            is ImageVector -> Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colors.onPrimary,
                modifier = Modifier.size(24.dp)
            )

            is Painter -> Icon(
                painter = icon,
                contentDescription = title,
                tint = MaterialTheme.colors.onPrimary,
                modifier = Modifier.size(24.dp)
            )

            else -> throw IllegalArgumentException("Unsupported icon type")
        }

        if (isExpanded) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                color = MaterialTheme.colors.onPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        if (!isExpanded && isHovered) {
            Popup(
                alignment = Alignment.CenterStart,
                offset = IntOffset(48, 0)
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colors.secondary,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        color = MaterialTheme.colors.onSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
