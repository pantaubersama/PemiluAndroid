package com.pantaubersama.app.ui.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import android.view.Surface
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import com.pantaubersama.app.BuildConfig
import com.pantaubersama.app.R
import com.pantaubersama.app.utils.ImageUtil
import com.pantaubersama.app.utils.PantauConstants.RequestCode.RC_CAMERA
import com.pantaubersama.app.utils.PantauConstants.RequestCode.RC_STORAGE
import id.zelory.compressor.Compressor
import java.io.File

class ImageChooserTools {

    companion object {
        @Deprecated("changed to ImageUtil#compressImage")
        fun proccedImageFromCamera(context: Context, data: Intent, windowManager: WindowManager? = null): File {
            var rotation = 0
            if (windowManager != null) {
                when (windowManager.defaultDisplay?.rotation) {
                    Surface.ROTATION_0 -> rotation = 90
                    Surface.ROTATION_90 -> rotation = 180
                    Surface.ROTATION_180 -> rotation = 270
                    Surface.ROTATION_270 -> rotation = 0
                }
            }
            val bitmap = data.extras?.get("data") as Bitmap
            val rotatedBitmap = ImageUtil.BitmapTools.rotate(bitmap, rotation)
            return ImageUtil.getImageFile(context, rotatedBitmap)
        }

        fun proccedImageFromStorage(data: Intent, context: Context): File {
            val selectedImage = data.data
            val projection = arrayOf(MediaStore.MediaColumns.DATA)
            val cursor = (context as FragmentActivity).managedQuery(selectedImage, projection, null, null, null)
            val column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
            cursor.moveToFirst()
            val path = cursor.getString(column_index)
            return Compressor(context)
                .setMaxWidth(1080)
                .setMaxHeight(1080)
                .setQuality(75)
                .setCompressFormat(Bitmap.CompressFormat.JPEG)
                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES).absolutePath)
                .compressToFile(File(path))
        }

        fun showDialog(context: Context, listener: ImageChooserListener) {
            val items = arrayOf<CharSequence>(context.getString(R.string.kamera), context.getString(R.string.galeri))
            val builder = AlertDialog.Builder(context)
            builder.setTitle(context.getString(R.string.ambil_gambar_dari))
            builder.setItems(items) { dialog, item ->
                when (item) {
                    0 -> listener.onClickCamera(openCamera(context))
                    1 -> openGallery(context)
                }
            }
            builder.show()
        }

        private fun openCamera(context: Context): File {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val cacheDir = context.cacheDir
            var file = File(cacheDir.absolutePath + "/image/")
            if (!file.isDirectory) {
                file.mkdir()
            }
            file = File(cacheDir, "image/" + System.currentTimeMillis().toString() + ".jpg")
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file))
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            (context as FragmentActivity).startActivityForResult(intent, RC_CAMERA)
            return file
        }

        private fun openGallery(context: Context) {
            val intentGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            (context as FragmentActivity).startActivityForResult(Intent.createChooser(intentGallery, "Pilih"), RC_STORAGE)
        }
    }

    interface ImageChooserListener {
        fun onClickCamera(file: File)
    }
}