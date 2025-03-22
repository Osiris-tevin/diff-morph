package compose.diffmorph.app.logic.repository

import compose.diffmorph.app.network.response.BaseResponse
import compose.diffmorph.app.network.response.PromptResponse

interface PromptRepository {

    suspend fun generatePrompt(
        imageName: String,
        modelVersion: String,
    ): BaseResponse<PromptResponse>

}
