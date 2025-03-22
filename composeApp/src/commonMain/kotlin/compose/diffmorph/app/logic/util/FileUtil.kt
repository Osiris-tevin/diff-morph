package compose.diffmorph.app.logic.util

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object FileUtil {

    fun openFileDialogAndUpload(onUpload: (filename: String, fileBytes: ByteArray) -> Unit)

    fun saveImageToFile(byteArray: ByteArray, imageName: String): String

}
