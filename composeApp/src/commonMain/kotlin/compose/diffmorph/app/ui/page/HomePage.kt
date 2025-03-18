package compose.diffmorph.app.ui.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import diffmorph.composeapp.generated.resources.Res
import diffmorph.composeapp.generated.resources.get_start
import diffmorph.composeapp.generated.resources.img_logo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomePage() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(Res.drawable.img_logo),
            contentDescription = "DiffMorph Logo",
            modifier = Modifier.size(384.dp)
        )
        Button(onClick = {}) {
            Text(text = stringResource(Res.string.get_start))
        }
    }
}
