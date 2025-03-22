package compose.diffmorph.app.logic.repositoryImpl

import compose.diffmorph.app.logic.repository.PromptRepository
import compose.diffmorph.app.network.baseUrl
import compose.diffmorph.app.network.response.BaseResponse
import compose.diffmorph.app.network.response.PromptResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class PromptRepositoryImpl(private val ktorClient: HttpClient) : PromptRepository {

    override suspend fun generatePrompt(
        imageName: String,
        modelVersion: String,
    ): BaseResponse<PromptResponse> {
        return ktorClient.get("$baseUrl/prompt?") {
            url {
                parameters.append("image_name", imageName)
                parameters.append("model_version", modelVersion)
            }
        }.body()
    }

}
