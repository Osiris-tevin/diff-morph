package compose.diffmorph.app.logic.repositoryImpl

import compose.diffmorph.app.logic.repository.ImageRepository
import compose.diffmorph.app.logic.util.FileUtil
import compose.diffmorph.app.network.baseUrl
import compose.diffmorph.app.network.response.BaseResponse
import compose.diffmorph.app.network.response.ImageNameListResponse
import compose.diffmorph.app.network.response.ImageResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

class ImageRepositoryImpl(private val ktorClient: HttpClient) : ImageRepository {

    override suspend fun getImage(filename: String): BaseResponse<ImageResponse> {
        return ktorClient.get("$baseUrl/image/$filename").body()
    }

    override suspend fun getImageNameList(): BaseResponse<ImageNameListResponse> {
        return ktorClient.get("$baseUrl/image_name_list").body()
    }

    override suspend fun editImage(
        imageName: String,
        sourcePrompt: String,
        editedPrompt: String,
        selfReplaceSteps: Float,
        crossReplaceSteps: Float,
        eqParamsWords: List<String>,
        eqParamsValues: List<Int>,
        seed: Int,
    ): BaseResponse<ImageResponse> {
        return ktorClient.post("$baseUrl/edit_image") {
            parameter("image_name", imageName)
            parameter("source_prompt", sourcePrompt)
            parameter("edited_prompt", editedPrompt)
            parameter("self_replace_steps", selfReplaceSteps)
            parameter("cross_replace_steps", crossReplaceSteps)
            parameter("seed", seed)

            contentType(ContentType.Application.Json)
            setBody(
                JsonObject(
                    mapOf(
                        "eq_params_words" to JsonArray(eqParamsWords.map { JsonPrimitive(it) }),
                        "eq_params_values" to JsonArray(eqParamsValues.map { JsonPrimitive(it) })
                    )
                )
            )
        }.body()
    }

    override suspend fun downloadImage(imageUrl: String): String {
        val byteArray = ktorClient.get(imageUrl).body<ByteArray>()
        val imageName = imageUrl.substringAfterLast("/")
        return FileUtil.saveImageToFile(byteArray, imageName)
    }

}
