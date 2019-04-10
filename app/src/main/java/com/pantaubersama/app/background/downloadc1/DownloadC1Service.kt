package com.pantaubersama.app.background.downloadc1

import android.app.IntentService
import android.content.Intent
import com.pantaubersama.app.base.BaseApp
import com.pantaubersama.app.di.module.ServiceModule
import android.app.NotificationManager
import androidx.core.app.NotificationCompat
import android.app.NotificationChannel
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.graphics.Color
import android.media.MediaScannerConnection
import android.os.Environment
import android.os.Handler
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.pantaubersama.app.R
import timber.log.Timber
import java.io.* // ktlint-disable
import java.net.URL

class DownloadC1Service : IntentService("DownloadService") {
    private lateinit var notificationBuilder: NotificationCompat.Builder
    private lateinit var notificationManager: NotificationManager

    override fun onCreate() {
        super.onCreate()
        (application as BaseApp).appComponent.withServiceComponent(ServiceModule(this)).inject(this)
    }

    override fun onHandleIntent(intent: Intent) {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel("download", "Unduh Gambar C1", NotificationManager.IMPORTANCE_LOW)
            notificationChannel.description = "no sound"
            notificationChannel.setSound(null, null)
            notificationChannel.enableLights(false)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationBuilder = NotificationCompat.Builder(this, "id")
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setContentTitle("Unduh Gambar C1")
            .setContentText("Mengunduh Gambar C1")
            .setDefaults(0)
            .setAutoCancel(true)
        notificationManager.notify(0, notificationBuilder.build())
        downloadImage(intent.getStringExtra("image_url"))
    }

    @Throws(IOException::class)
    private fun downloadImage(stringUrl: String) {
        var count: Int
        try {
            val url = URL(stringUrl)
            val connection = url.openConnection()
            connection.connect()
            val lengthOfFile = connection.contentLength
            val input = BufferedInputStream(url.openStream(), 8192)
            val destDir = File(Environment.getExternalStorageDirectory().absolutePath + "/Pantau Bersama/")
            if (!destDir.isDirectory) {
                destDir.mkdir()
            }
            val destFile = File(
                destDir, System.currentTimeMillis().toString() + ".jpg")

            if (!destDir.isDirectory) {
                destDir.mkdirs()
            }
            val output = FileOutputStream(destFile.absolutePath)
            val data = ByteArray(1024)
            var total: Long = 0
            do {
                count = input.read(data)
                if (count == -1)
                    break
                total += count.toLong()
                updateNotification((total * 100 / lengthOfFile).toInt())
                output.write(data, 0, count)
            } while (true)
            output.flush()
            output.close()
            input.close()
            Handler().postDelayed({
                MediaScannerConnection.scanFile(this, arrayOf(destFile.absolutePath), null, null)
            }, 500)
            onDownloadComplete(true)
        } catch (e: Exception) {
            Timber.e("Error: %s", e.message)
            onDownloadComplete(false)
        }
    }

    private fun updateNotification(currentProgress: Int) {
        notificationBuilder.setProgress(100, currentProgress, false)
        notificationBuilder.setContentText("Terunduh: $currentProgress%")
        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun sendProgressUpdate(downloadComplete: Boolean) {
        val intent = Intent("progress_complete")
        intent.putExtra("downloadComplete", downloadComplete)
        LocalBroadcastManager.getInstance(this@DownloadC1Service).sendBroadcast(intent)
    }

    private fun onDownloadComplete(downloadComplete: Boolean) {
        sendProgressUpdate(downloadComplete)
        notificationManager.cancel(0)
        notificationBuilder.setProgress(0, 0, false)
        notificationBuilder.setContentText("Gambar Berhasil Diunduh")
        notificationBuilder.setSmallIcon(android.R.drawable.stat_sys_download_done)
        notificationBuilder.setLargeIcon(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            getBitmap(R.drawable.ic_notification_icon)
        } else {
            getBitmap(R.drawable.ic_logo_notification_transparent_in)
        })
        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun getBitmap(resource: Int): Bitmap {
        return BitmapFactory.decodeResource(resources, resource)
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        notificationManager.cancel(0)
    }
}