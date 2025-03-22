package compose.diffmorph.app.logic.repository

import compose.diffmorph.app.network.response.BaseResponse
import compose.diffmorph.app.network.response.UploadResponse

interface UploadRepository {

    /**
     * 上传图片
     */
    suspend fun uploadImage(filename: String, fileBytes: ByteArray): BaseResponse<UploadResponse>

}
