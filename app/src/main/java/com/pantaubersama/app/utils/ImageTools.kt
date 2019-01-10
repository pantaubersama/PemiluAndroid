package com.pantaubersama.app.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Environment
import android.view.Surface
import android.view.WindowManager
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ImageTools {
    object BitmapTools {
        fun toBitmap(data: ByteArray): Bitmap {
            return BitmapFactory.decodeByteArray(data, 0, data.size)
        }

        fun rotate(bitmap: Bitmap, angle: Int): Bitmap {
            val mat = Matrix()
            mat.postRotate(angle.toFloat())
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, mat, true)
        }
    }

    companion object {
        fun getImageFile(bitmap: Bitmap): File {
            var file = File(Environment.getExternalStorageDirectory().path + "/dir")
            if (!file.isDirectory) {
                file.mkdir()
            }
            file = File(Environment.getExternalStorageDirectory().path + "/dir", System.currentTimeMillis().toString() + ".jpg")
            try {
                val fileOutputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
                fileOutputStream.flush()
                fileOutputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (exception: Exception) {
                exception.printStackTrace()
            }

            return file
        }

        fun bitmapRotation(windowManager: WindowManager): Int {
            var rotation = 0
            when (windowManager.defaultDisplay.rotation) {
                Surface.ROTATION_0 -> rotation = 90
                Surface.ROTATION_90 -> rotation = 180
                Surface.ROTATION_180 -> rotation = 270
                Surface.ROTATION_270 -> rotation = 0
            }
            return rotation
        }
    }
}