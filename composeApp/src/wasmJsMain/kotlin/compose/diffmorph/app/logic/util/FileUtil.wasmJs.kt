package compose.diffmorph.app.logic.util

import kotlinx.browser.document
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Int8Array
import org.khronos.webgl.get
import org.w3c.dom.HTMLInputElement
import org.w3c.files.FileReader
import org.w3c.files.get

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object FileUtil {

    actual fun openFileDialogAndUpload(onUpload: (filename: String, fileBytes: ByteArray) -> Unit) {
        val input = document.createElement("input") as HTMLInputElement
        input.type = "file"
        input.accept = "image/*"

        input.addEventListener("change") changeListener@{ _ ->
            val file = input.files?.get(0) ?: return@changeListener
            val reader = FileReader()
            reader.addEventListener("load") loadListener@{ _ ->
                val arrayBuffer = reader.result as? ArrayBuffer ?: return@loadListener
                val fileBytes = arrayBufferToByteArray(arrayBuffer)
                onUpload(file.name, fileBytes)
            }
            reader.readAsArrayBuffer(file)
        }
    }

    private fun arrayBufferToByteArray(arrayBuffer: ArrayBuffer): ByteArray {
        val int8Array = Int8Array(arrayBuffer)
        return ByteArray(int8Array.length) { int8Array[it] }
    }

    actual fun saveImageToFile(byteArray: ByteArray, imageName: String): String {
        TODO("Not yet implemented")
    }

}
