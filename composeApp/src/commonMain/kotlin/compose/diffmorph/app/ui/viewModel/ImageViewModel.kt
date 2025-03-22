package compose.diffmorph.app.ui.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import compose.diffmorph.app.logic.repository.ImageRepository
import compose.diffmorph.app.logic.repository.PromptRepository
import compose.diffmorph.app.logic.repository.UploadRepository
import compose.diffmorph.app.logic.repositoryImpl.ImageRepositoryImpl
import compose.diffmorph.app.logic.repositoryImpl.PromptRepositoryImpl
import compose.diffmorph.app.logic.repositoryImpl.UploadRepositoryImpl
import compose.diffmorph.app.logic.state.ImageEditState
import compose.diffmorph.app.logic.state.PromptGenerateState
import compose.diffmorph.app.logic.state.UploadState
import compose.diffmorph.app.network.ktorClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ImageViewModel(
    private val uploadRepository: UploadRepository = UploadRepositoryImpl(ktorClient),
    private val imageRepository: ImageRepository = ImageRepositoryImpl(ktorClient),
    private val promptRepository: PromptRepository = PromptRepositoryImpl(ktorClient),
) : ViewModel() {

    private val _uploadState = mutableStateOf<UploadState>(UploadState.Idle)
    val uploadState: State<UploadState> = _uploadState
    private val _selectedSourceImageName = mutableStateOf("None Selected")
    val selectedSourceImageName = _selectedSourceImageName
    private val _selectedSourceImageUrl = mutableStateOf("")
    val selectedSourceImageUrl = _selectedSourceImageUrl
    private val _sourceImageNameList = mutableStateOf<List<String>>(emptyList())
    val sourceImageNameList = _sourceImageNameList
    private val _promptGenerateState = mutableStateOf<PromptGenerateState>(PromptGenerateState.Idle)
    val promptGenerateState: State<PromptGenerateState> = _promptGenerateState
    private val _modelVersion = mutableStateOf("base")
    val modelVersion = _modelVersion
    private val _imageEditState = mutableStateOf<ImageEditState>(ImageEditState.Idle)
    val imageEditState = _imageEditState
    private val _editedImageUrl = mutableStateOf("")
    val editedImageUrl = _editedImageUrl
    private val _sourcePrompt = mutableStateOf("")
    val sourcePrompt = _sourcePrompt
    private val _editedPrompt = mutableStateOf("")
    val editedPrompt = _editedPrompt
    private val _selfReplaceSteps = mutableStateOf(0.6f)
    val selfReplaceSteps = _selfReplaceSteps
    private val _crossReplaceSteps = mutableStateOf(0.8f)
    val crossReplaceSteps = _crossReplaceSteps
    private val _eqParamsWords = mutableStateOf(listOf<String>())
    val eqParamsWords = _eqParamsWords
    private val _eqParamsValues = mutableStateOf(listOf<Int>())
    val eqParamsValues = _eqParamsValues
    private val _seed = mutableStateOf(2025)
    val seed = _seed

    init {
        refreshSourceImageNameList()
    }

    fun uploadImage(filename: String, fileBytes: ByteArray) {
        _uploadState.value = UploadState.Loading

        viewModelScope.launch(Dispatchers.Default) {
            try {
                val response = uploadRepository.uploadImage(filename, fileBytes)
                _uploadState.value = UploadState.Success(response.data)
                _sourceImageNameList.value += listOf(response.data.filename)
                _selectedSourceImageName.value = response.data.filename
            } catch (e: Exception) {
                _uploadState.value = UploadState.Error(e.message ?: "Unknown Error")
            }
        }
    }

    fun resetUploadState() {
        _uploadState.value = UploadState.Idle
    }

    fun previewSourceImage() {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                val response = imageRepository.getImage(_selectedSourceImageName.value)
                _selectedSourceImageUrl.value = response.data.imageUrl
                _editedImageUrl.value = ""
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

    fun selectSourceImage(imageName: String) {
        _selectedSourceImageName.value = imageName
    }

    private fun refreshSourceImageNameList() {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                val response = imageRepository.getImageNameList()
                _sourceImageNameList.value = response.data.imageNameList
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

    fun changeModelVersion(modelVersion: String) {
        _modelVersion.value = modelVersion
    }

    fun generatePrompt() {
        _promptGenerateState.value = PromptGenerateState.Loading

        viewModelScope.launch(Dispatchers.Default) {
            try {
                val response =
                    promptRepository.generatePrompt(
                        imageName = _selectedSourceImageName.value,
                        modelVersion = _modelVersion.value
                    )
                _promptGenerateState.value = PromptGenerateState.Success(response.data)
                _sourcePrompt.value = response.data.prompt
            } catch (e: Exception) {
                println(e.message)
                _promptGenerateState.value = PromptGenerateState.Error(e.message ?: "Unknown Error")
            }
        }
    }

    fun resetPromptGenerateState() {
        _promptGenerateState.value = PromptGenerateState.Idle
    }

    fun updateEditedPrompt(editedPrompt: String) {
        _editedPrompt.value = editedPrompt
    }

    fun updateSelfReplaceSteps(selfReplaceSteps: Float) {
        _selfReplaceSteps.value = selfReplaceSteps
    }

    fun updateCrossReplaceSteps(crossReplaceSteps: Float) {
        _crossReplaceSteps.value = crossReplaceSteps
    }

    fun addEqParamWord(eqParamWord: String) {
        _eqParamsWords.value += listOf(eqParamWord)
    }

    fun addEqParamValue(eqParamValue: Int) {
        _eqParamsValues.value += listOf(eqParamValue)
    }

    fun updateSeed(seed: Int) {
        _seed.value = seed
    }

    fun resetImageEditState() {
        _imageEditState.value = ImageEditState.Idle
        _editedPrompt.value = ""
        _eqParamsWords.value = emptyList()
        _eqParamsValues.value = emptyList()
    }

    fun editImage() {
        _imageEditState.value = ImageEditState.Loading

        viewModelScope.launch(Dispatchers.Default) {
            try {
                val response = imageRepository.editImage(
                    imageName = _selectedSourceImageName.value,
                    sourcePrompt = _sourcePrompt.value,
                    editedPrompt = _editedPrompt.value,
                    selfReplaceSteps = _selfReplaceSteps.value,
                    crossReplaceSteps = _crossReplaceSteps.value,
                    eqParamsWords = _eqParamsWords.value,
                    eqParamsValues = _eqParamsValues.value,
                    seed = _seed.value
                )
                _imageEditState.value = ImageEditState.Success(response.data)
                _editedImageUrl.value = response.data.imageUrl
            } catch (e: Exception) {
                _imageEditState.value = ImageEditState.Error(e.message ?: "Unknown Error")
            }
        }
    }

    fun saveImage() {
        viewModelScope.launch(Dispatchers.Default) {
            imageRepository.downloadImage(_editedImageUrl.value)
        }
    }

}
