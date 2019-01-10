package com.pantaubersama.app.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Environment
import id.zelory.compressor.Compressor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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
    }

    interface CompressorListener {
        fun onSuccess(file: File)
        fun onFailed(throwable: Throwable)
    }
}