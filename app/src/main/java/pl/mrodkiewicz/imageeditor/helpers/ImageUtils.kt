package pl.mrodkiewicz.imageeditor.helpers

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import ar.com.hjg.pngj.ImageInfo
import ar.com.hjg.pngj.ImageLineInt
import ar.com.hjg.pngj.PngReader
import ar.com.hjg.pngj.PngWriter
import ar.com.hjg.pngj.chunks.ChunkCopyBehaviour
import ar.com.hjg.pngj.chunks.ChunkLoadBehaviour
import pl.mrodkiewicz.imageeditor.BuildConfig
import pl.mrodkiewicz.imageeditor.data.LutFilter
import timber.log.Timber
import java.io.File
import java.io.File.separator
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.sqrt


fun Uri.loadBitmap(context: Context): Bitmap {
    var inputStream: InputStream? = context.contentResolver.openInputStream(this)
    val exif = inputStream?.let { it1 -> ExifInterface(it1) }
    var orientation = exif?.getAttributeInt(
        ExifInterface.TAG_ORIENTATION,
        ExifInterface.ORIENTATION_NORMAL
    ) ?: 0
    val matrix = decodeExifOrientation(orientation)
    inputStream?.close()
    inputStream = context.contentResolver.openInputStream(this)
    val bitmap = android.graphics.BitmapFactory.decodeStream(inputStream)
    return Bitmap.createBitmap(
        bitmap, 0, 0, bitmap.width,
        bitmap.height, matrix, true
    )
}

fun Bitmap.saveImageAndAddToGallery(context: Context): Uri? {
    return if (Build.VERSION.SDK_INT >= 29) {
        val values = contentValues()
        values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + "jetphoto")
        values.put(MediaStore.Images.Media.IS_PENDING, true)
        val uri: Uri? = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        if (uri != null) {
            saveImageToStream(this, context.contentResolver.openOutputStream(uri))
            values.put(MediaStore.Images.Media.IS_PENDING, false)
            context.contentResolver.update(uri, values, null, null)
        }
        uri
    } else {
        val directory = File(Environment.getExternalStorageDirectory().toString() + separator + "jetphoto")

        if (!directory.exists()) {
            directory.mkdirs()
        }
        val fileName = System.currentTimeMillis().toString() + ".png"
        val file = File(directory, fileName)
        saveImageToStream(this, FileOutputStream(file))
        if (file.absolutePath != null) {
            val values = contentValues()
            values.put(MediaStore.Images.Media.DATA, file.absolutePath)
            context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        }
        FileProvider.getUriForFile(
                context,
                context.applicationContext.packageName.toString() + ".provider",
                file
        )
    }
}

private fun contentValues() : ContentValues {
    val values = ContentValues()
    values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
    values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
    values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
    return values
}

private fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?) {
    outputStream?.let{
        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}


fun getUriForCameraPhoto(context: Context): Uri? {
    val imagePath: File = File(context.filesDir, "images")
    if (!imagePath.exists()) {
        imagePath.mkdirs()
    }
    val filename = "image.jpg"
    val file = File(imagePath, filename)
    return FileProvider.getUriForFile(
        context,
        BuildConfig.APPLICATION_ID + ".provider", file
    )
}

fun Bitmap.convertToLutFilter(name: String): LutFilter {
    var lut = LutFilter(name)
    var x = width / height
    var lutList = mutableListOf<Int>()
    lut.x = x
    lut.y = lut.x
    lut.z = lut.x
    var pixels = IntArray(width * height)
    getPixels(pixels, 0, width, 0, 0, width, height)
    for (r in 0 until x) {
        for (g in 0 until x) {
            val p: Int = r + g * width
            for (b in 0 until x) {
                lutList.add(pixels[p + b * height])
            }
        }
    }
    lut.lutFilter = lutList.toIntArray()
    return lut
}
