package com.pantaubersama.app.utils.download

import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import com.pantaubersama.app.CommonActivity
import com.pantaubersama.app.R
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_IMAGE_PATH
import com.pantaubersama.app.utils.ToastUtil
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.lang.Exception
import java.nio.channels.FileChannel

class DownloadActivity : CommonActivity() {
    override fun statusBarColor(): Int? = 0
    override fun setLayout(): Int = R.layout.activity_download

    private lateinit var imagePath: String

    companion object {
        fun setIntent(context: Context, imagePath: String): Intent {
            val intent = Intent(context, DownloadActivity::class.java)
            intent.putExtra(EXTRA_IMAGE_PATH, imagePath)
            return intent
        }
    }

    override fun fetchIntentExtra() {
        intent.getStringExtra(EXTRA_IMAGE_PATH)?.let { imagePath = it }
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        var sourceFileChannel: FileChannel? = null
        var destFileChannel: FileChannel? = null
        try {
            val sourceFile = File(imagePath)
            val destDir = File(Environment.getExternalStorageDirectory().absolutePath + "/Pantau Bersama/")
            if (!destDir.isDirectory) {
                destDir.mkdir()
            }
            val destFile = File(
                destDir, System.currentTimeMillis().toString() + ".jpg")
            sourceFileChannel = FileInputStream(sourceFile).channel
            destFileChannel = FileOutputStream(destFile).channel

            var count: Long = 0
            val size = sourceFileChannel.size()

            while (count < size) {
                count += destFileChannel.transferFrom(sourceFileChannel, count, size - count)
            }
            MediaScannerConnection.scanFile(this, arrayOf(destFile.absolutePath), null, null)
            sourceFile.delete()

            ToastUtil.show(this, "Gambar tersimpan di Galeri")
        } catch (e: Exception) {
            e.printStackTrace()
            ToastUtil.show(this, "Gagal menyimpan di Galeri")
        } finally {
            sourceFileChannel?.close()
            destFileChannel?.close()
        }
        finish()
    }
}
