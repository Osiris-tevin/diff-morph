package compose.diffmorph.app.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UploadResponse(
    val filename: String,
    @SerialName("file_path") val filePath: String,
)
