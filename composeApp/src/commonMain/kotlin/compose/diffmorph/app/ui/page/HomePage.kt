package compose.diffmorph.app.ui.page

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import compose.diffmorph.app.logic.state.ImageEditState
import compose.diffmorph.app.logic.state.PromptGenerateState
import compose.diffmorph.app.logic.state.UploadState
import compose.diffmorph.app.logic.util.FileUtil
import compose.diffmorph.app.ui.viewModel.ImageViewModel
import diffmorph.composeapp.generated.resources.Res
import diffmorph.composeapp.generated.resources.edit
import diffmorph.composeapp.generated.resources.exit
import diffmorph.composeapp.generated.resources.get_start
import diffmorph.composeapp.generated.resources.img_logo
import diffmorph.composeapp.generated.resources.preview
import diffmorph.composeapp.generated.resources.upload
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomePage(imageViewModel: ImageViewModel = viewModel()) {
    var showContent by remember { mutableStateOf(false) }

    AnimatedContent(
        targetState = showContent,
        transitionSpec = {
            fadeIn() + slideInVertically { if (targetState) it else -it } togetherWith fadeOut() + slideOutVertically { if (targetState) -it else it }
        }
    ) { target ->
        if (target) {
            HomePageMainContent(imageViewModel) { showContent = false }
        } else {
            HomeGreeting { showContent = true }
        }
    }
}

@Composable
private fun HomeGreeting(onStartClick: () -> Unit) {
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
        Button(onClick = { onStartClick() }) {
            Text(text = stringResource(Res.string.get_start))
        }
    }
}

@Composable
private fun HomePageMainContent(imageViewModel: ImageViewModel, onExitClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.weight(1f)) {
            // UploadSection
            Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
                SectionTitleBar(title = stringResource(Res.string.upload))
                UploadContentSection(imageViewModel)
            }
            VerticalDivider()
            // PreviewSection
            Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
                SectionTitleBar(title = stringResource(Res.string.preview))
                PreviewContentSection(imageViewModel)
            }
        }
        Divider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = Color.Gray)
        Row(modifier = Modifier.weight(1f)) {
            // EditSection
            Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
                SectionTitleBar(title = stringResource(Res.string.preview))
                EditContentSection(imageViewModel)
            }
            VerticalDivider()
            // ExitSection
            Box(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Button(onClick = { onExitClick() }) {
                    Text(text = stringResource(Res.string.exit))
                }
            }
        }
    }
}

@Composable
private fun VerticalDivider() {
    Divider(modifier = Modifier.width(1.dp).fillMaxHeight(), color = Color.Gray)
}

@Composable
private fun SectionTitleBar(title: String) {
    Row(
        modifier = Modifier.fillMaxWidth().height(48.dp).background(MaterialTheme.colors.primary),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = MaterialTheme.colors.onPrimary,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

/**
 * 图片上传 Section
 */
@Composable
private fun UploadContentSection(imageViewModel: ImageViewModel) {
    val uploadState by imageViewModel.uploadState
    val selectedSourceImageName by imageViewModel.selectedSourceImageName
    val sourceImageNameList by imageViewModel.sourceImageNameList

    Row(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.weight(2f).fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (uploadState) {
                UploadState.Idle -> {
                    Text(text = "Ready to upload", color = MaterialTheme.colors.onBackground)
                    Button(
                        onClick = {
                            FileUtil.openFileDialogAndUpload { filename, fileBytes ->
                                imageViewModel.uploadImage(filename, fileBytes)
                            }
                        }
                    ) {
                        Text("Select Image")
                    }
                }

                UploadState.Loading -> {
                    CircularProgressIndicator()
                    Text(text = "Uploading...", color = MaterialTheme.colors.onBackground)
                }

                is UploadState.Success -> {
                    val response = (uploadState as UploadState.Success).response
                    Text("Upload successful file: ${response.filename}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        Button(onClick = { imageViewModel.resetUploadState() }) {
                            Text("Reset")
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(onClick = { imageViewModel.previewSourceImage() }) {
                            Text("Preview")
                        }
                    }
                }

                is UploadState.Error -> {
                    val errorMessage = (uploadState as UploadState.Error).message
                    Text("Error: $errorMessage")
                    println(errorMessage)
                    Button(onClick = { imageViewModel.resetUploadState() }) {
                        Text("Reset")
                    }
                }
            }
        }
        Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
            Box(
                modifier = Modifier.fillMaxWidth().height(48.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Select Image: $selectedSourceImageName",
                    color = MaterialTheme.colors.onBackground,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Divider(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 48.dp),
                thickness = 1.dp,
                color = Color.Gray
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                items(sourceImageNameList.size) { index ->
                    val imageName = sourceImageNameList[index]

                    Text(
                        text = imageName,
                        color = MaterialTheme.colors.onBackground,
                        modifier = Modifier.clickable {
                            imageViewModel.selectSourceImage(imageName)
                            imageViewModel.previewSourceImage()
                        }
                    )
                }
            }
        }
    }
}

/**
 * 图片预览 Section
 */
@Composable
private fun PreviewContentSection(imageViewModel: ImageViewModel) {
    val selectedSourceImageUrl by imageViewModel.selectedSourceImageUrl
    val editedImageUrl by imageViewModel.editedImageUrl

    Row(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
            Box(
                modifier = Modifier.fillMaxWidth().height(48.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Source Image",
                    color = MaterialTheme.colors.onBackground,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Divider(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 48.dp),
                thickness = 1.dp,
                color = Color.Gray
            )
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = selectedSourceImageUrl,
                    contentDescription = "Source Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(256.dp).clip(RoundedCornerShape(8.dp))
                )
            }
        }
        Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
            Box(
                modifier = Modifier.fillMaxWidth().height(48.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Edited Image",
                    color = MaterialTheme.colors.onBackground,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Divider(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 48.dp),
                thickness = 1.dp,
                color = Color.Gray
            )
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = editedImageUrl,
                    contentDescription = "Edited Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(256.dp).clip(RoundedCornerShape(8.dp))
                )
            }
        }
    }
}

/**
 * 图像编辑 Section
 */
@Composable
private fun EditContentSection(imageViewModel: ImageViewModel) {
    val promptGenerateState by imageViewModel.promptGenerateState
    val modelVersion by imageViewModel.modelVersion
    val imageEditState by imageViewModel.imageEditState
    val sourcePrompt by imageViewModel.sourcePrompt
    val editedPrompt by imageViewModel.editedPrompt
    val selfReplaceSteps by imageViewModel.selfReplaceSteps
    val crossReplaceSteps by imageViewModel.crossReplaceSteps
    var eqParamsWord by remember { mutableStateOf("") }
    val eqParamsWords by imageViewModel.eqParamsWords
    var eqParamsValue by remember { mutableStateOf("") }
    val eqParamsValues by imageViewModel.eqParamsValues
    val seed by imageViewModel.seed

    Column(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (promptGenerateState) {
                PromptGenerateState.Idle -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Model Version",
                                color = MaterialTheme.colors.onBackground,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                            ModelVersionRadioGroup { imageViewModel.changeModelVersion(it) }
                        }
                        Button(onClick = { imageViewModel.generatePrompt() }) {
                            Text("Generate Prompt")
                        }
                    }
                }

                PromptGenerateState.Loading -> {
                    CircularProgressIndicator()
                    Text(text = "Generating...", color = MaterialTheme.colors.onBackground)
                }

                is PromptGenerateState.Success -> {
                    Text(
                        text = "modelVersion: $modelVersion",
                        color = MaterialTheme.colors.onBackground
                    )
                    Text(
                        text = "sourcePrompt: $sourcePrompt",
                        color = MaterialTheme.colors.onBackground
                    )
                    Text(
                        text = "editedPrompt: $editedPrompt",
                        color = MaterialTheme.colors.onBackground
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "eqParamWords: $eqParamsWords",
                            color = MaterialTheme.colors.onBackground
                        )
                        Text(
                            text = "eqParamValues: $eqParamsValues",
                            color = MaterialTheme.colors.onBackground
                        )
                    }
                    Button(onClick = { imageViewModel.resetPromptGenerateState() }) {
                        Text("Reset")
                    }
                }

                is PromptGenerateState.Error -> {
                    val errorMessage = (promptGenerateState as PromptGenerateState.Error).message
                    Text("Error: $errorMessage")
                    println(errorMessage)
                    Button(onClick = { imageViewModel.resetPromptGenerateState() }) {
                        Text("Reset")
                    }
                }
            }
        }
        Divider(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            thickness = 1.dp,
            color = Color.Gray
        )
        Column(
            modifier = Modifier.fillMaxWidth().weight(2f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (imageEditState) {
                ImageEditState.Idle -> {
                    // 输入 EditedPrompt
                    Row(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = editedPrompt,
                            onValueChange = { imageViewModel.updateEditedPrompt(it) },
                            label = { Text("Enter edited prompt") },
                            textStyle = TextStyle(color = MaterialTheme.colors.onBackground)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    // 输入替换步长及随机种子
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        OutlinedTextField(
                            value = selfReplaceSteps.toString(),
                            onValueChange = { imageViewModel.updateSelfReplaceSteps(it.toFloat()) },
                            label = { Text("Enter self replace steps") },
                            textStyle = TextStyle(color = MaterialTheme.colors.onBackground),
                            modifier = Modifier.width(200.dp)
                        )
                        OutlinedTextField(
                            value = crossReplaceSteps.toString(),
                            onValueChange = { imageViewModel.updateCrossReplaceSteps(it.toFloat()) },
                            label = { Text("Enter cross replace steps") },
                            textStyle = TextStyle(color = MaterialTheme.colors.onBackground),
                            modifier = Modifier.width(200.dp)
                        )
                        OutlinedTextField(
                            value = seed.toString(),
                            onValueChange = { imageViewModel.updateSeed(it.toInt()) },
                            label = { Text("Enter seed") },
                            textStyle = TextStyle(color = MaterialTheme.colors.onBackground),
                            modifier = Modifier.width(120.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    // 输入 eqParams
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = eqParamsWord,
                            onValueChange = { eqParamsWord = it },
                            label = { Text("Enter words") },
                            textStyle = TextStyle(color = MaterialTheme.colors.onBackground),
                            modifier = Modifier.width(120.dp)
                        )
                        OutlinedTextField(
                            value = eqParamsValue,
                            onValueChange = { eqParamsValue = it },
                            label = { Text("Enter values") },
                            textStyle = TextStyle(color = MaterialTheme.colors.onBackground),
                            modifier = Modifier.width(120.dp)
                        )
                        Button(
                            onClick = {
                                imageViewModel.addEqParamWord(eqParamsWord)
                                imageViewModel.addEqParamValue(eqParamsValue.toInt())
                                eqParamsWord = ""
                                eqParamsValue = ""
                            }
                        ) {
                            Text(text = "Add")
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.weight(1f)) {
                        Button(onClick = { imageViewModel.editImage() }) {
                            Text(text = stringResource(Res.string.edit))
                        }
                    }
                }

                ImageEditState.Loading -> {
                    CircularProgressIndicator()
                    Text(text = "Editing...", color = MaterialTheme.colors.onBackground)
                }

                is ImageEditState.Success -> {
                    Text(text = "Editing successful", color = MaterialTheme.colors.onBackground)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = {
                                imageViewModel.resetImageEditState()
                                eqParamsWord = ""
                                eqParamsValue = ""
                            }
                        ) {
                            Text("Reset")
                        }
                        Button(onClick = { imageViewModel.saveImage() }) {
                            Text("Save")
                        }
                    }
                }

                is ImageEditState.Error -> {
                    val errorMessage = (imageEditState as ImageEditState.Error).message
                    Text("Error: $errorMessage")
                    println(errorMessage)
                    Button(onClick = { imageViewModel.resetImageEditState() }) {
                        Text("Reset")
                    }
                }
            }
        }
    }
}

@Composable
private fun ModelVersionRadioGroup(onModelVersionChange: (String) -> Unit) {
    val options = listOf("base", "large")
    var selectedOption by remember { mutableStateOf(options[0]) }

    Row {
        options.forEach { option ->
            Column(
                modifier = Modifier.padding(horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                RadioButton(
                    selected = (option == selectedOption),
                    onClick = {
                        selectedOption = option
                        onModelVersionChange(option)
                    }
                )
                Text(
                    text = option,
                    color = MaterialTheme.colors.onBackground
                )
            }
        }
    }
}
