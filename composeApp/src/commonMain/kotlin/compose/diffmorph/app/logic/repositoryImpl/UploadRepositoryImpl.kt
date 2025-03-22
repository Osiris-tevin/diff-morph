package compose.diffmorph.app.logic.repositoryImpl

import compose.diffmorph.app.logic.repository.UploadRepository
import compose.diffmorph.app.network.baseUrl
import compose.diffmorph.app.network.response.BaseResponse
import compose.diffmorph.app.network.response.UploadResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode

class UploadRepositoryImpl(private val ktorClient: HttpClient) : UploadRepository {

    override suspend fun uploadImage(
        filename: String,
        fileBytes: ByteArray,
    ): BaseResponse<UploadResponse> {
        val response: HttpResponse = ktorClient.submitFormWithBinaryData(
            url = "$baseUrl/upload",
            formData = formData {
                append(
                    key = "file", fileBytes, Headers.build {
                        val contentType = when {
                            filename.endsWith(".png", ignoreCase = true) -> ContentType.Image.PNG
                            filename.endsWith(
                                ".jpg",
                                ignoreCase = true
                            ) || filename.endsWith(
                                ".jpeg",
                                ignoreCase = true
                            ) -> ContentType.Image.JPEG

                            else -> throw IllegalArgumentException("Unsupported file format")
                        }

                        append(HttpHeaders.ContentType, contentType.toString())
                        append(HttpHeaders.ContentDisposition, "filename=$filename")
                    }
                )
            }
        )

        if (response.status == HttpStatusCode.OK) {
            return response.body()
        } else {
            throw Exception("Upload failed: ${response.status}")
        }
    }

}
