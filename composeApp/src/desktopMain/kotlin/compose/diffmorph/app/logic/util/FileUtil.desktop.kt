package compose.diffmorph.app.logic.util

import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object FileUtil {

    actual fun openFileDialogAndUpload(onUpload: (filename: String, fileBytes: ByteArray) -> Unit) {
        val dialog = FileDialog(Frame(), "Select Image to Upload", FileDialog.LOAD)
        dialog.isVisible = true

        val selectedFile = dialog.file ?: return
        val selectedFilePath = File(dialog.directory, selectedFile).absolutePath
        val file = File(selectedFilePath)

        if (file.exists() && file.isFile) {
            val fileBytes = file.readBytes()
            onUpload(file.name, fileBytes)
        } else {
            println("未选择有效文件")
        }
    }

    actual fun saveImageToFile(byteArray: ByteArray, imageName: String): String {
        return try {
            val projectRoot = System.getProperty("user.dir")
            val outputDir = Paths.get(projectRoot, "outputs")

            if (!Files.exists(outputDir)) {
                Files.createDirectories(outputDir)
            }

            val filePath = outputDir.resolve(imageName)
            Files.createDirectories(filePath.parent)
            Files.write(filePath, byteArray)

            "File saved to ${filePath.toAbsolutePath()}"
        } catch (e: IOException) {
            "Error saving file: ${e.message}"
        }
    }

}
