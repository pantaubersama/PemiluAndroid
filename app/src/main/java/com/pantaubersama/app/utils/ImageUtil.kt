package com.pantaubersama.app.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.os.Environment
import id.zelory.compressor.Compressor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import android.view.Surface
import android.view.View
import android.view.WindowManager
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ImageUtil {
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

        fun compressImage(context: Context, imageFile: File, maxSizeInMb: Int = 2, listener: CompressorListener) {
//        var ITERATOR = 0
//        val MAX_ITERATOR = 10
//
//        for (i in ITERATOR..MAX_ITERATOR) loop@ {
//
//        }
            Compressor(context)
                .compressToFileAsFlowable(imageFile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        if (it.length() <= (maxSizeInMb * 1024 * 1024)) {
                            listener.onSuccess(it)
                        } else {
                            listener.onFailed(Throwable("Gambar terlalu besar"))
                        }
                    },
                    {
                        listener.onFailed(Throwable("Gambar terlalu besar"))
                    }
                )
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

        fun getScreenshotAsFile(rootView: View) : File {
            val bitmap = Bitmap.createBitmap(rootView.width, rootView.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            rootView.draw(canvas)
            return getImageFile(bitmap)
        }
    }

    interface CompressorListener {
        fun onSuccess(file: File)
        fun onFailed(throwable: Throwable)
    }
}