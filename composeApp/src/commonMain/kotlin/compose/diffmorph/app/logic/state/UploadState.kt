package compose.diffmorph.app.logic.state

import compose.diffmorph.app.network.response.UploadResponse

sealed class UploadState {
    data object Idle : UploadState()
    data object Loading : UploadState()
    data class Success(val response: UploadResponse) : UploadState()
    data class Error(val message: String) : UploadState()
}
