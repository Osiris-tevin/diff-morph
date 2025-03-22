package compose.diffmorph.app.logic.repository

import compose.diffmorph.app.network.response.BaseResponse
import compose.diffmorph.app.network.response.ImageNameListResponse
import compose.diffmorph.app.network.response.ImageResponse

interface ImageRepository {

    suspend fun getImage(filename: String): BaseResponse<ImageResponse>

    suspend fun getImageNameList(): BaseResponse<ImageNameListResponse>

    suspend fun editImage(
        imageName: String,
        sourcePrompt: String,
        editedPrompt: String,
        selfReplaceSteps: Float,
        crossReplaceSteps: Float,
        eqParamsWords: List<String>,
        eqParamsValues: List<Int>,
        seed: Int,
    ): BaseResponse<ImageResponse>

    suspend fun downloadImage(imageUrl: String): String

}
