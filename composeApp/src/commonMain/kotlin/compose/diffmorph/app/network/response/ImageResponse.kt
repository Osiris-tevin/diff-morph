package compose.diffmorph.app.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageResponse(@SerialName("image_url") val imageUrl: String)

@Serializable
data class ImageNameListResponse(@SerialName("image_name_list") val imageNameList: List<String>)
