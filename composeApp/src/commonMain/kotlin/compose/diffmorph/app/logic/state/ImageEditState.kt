package compose.diffmorph.app.logic.state

import compose.diffmorph.app.network.response.ImageResponse

sealed class ImageEditState {
    data object Idle : ImageEditState()
    data object Loading : ImageEditState()
    data class Success(val response: ImageResponse) : ImageEditState()
    data class Error(val message: String) : ImageEditState()
}
