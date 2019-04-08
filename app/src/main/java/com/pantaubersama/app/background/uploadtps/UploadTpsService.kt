package com.pantaubersama.app.background.uploadtps

import android.app.* // ktlint-disable
import android.content.Intent
import com.pantaubersama.app.base.BaseApp
import com.pantaubersama.app.di.module.ServiceModule
import javax.inject.Inject
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.pantaubersama.app.R
import timber.log.Timber

class UploadTpsService : IntentService("UploadTpsService"), UploadTpsView {
    @Inject
    lateinit var presenter: UploadTpsPresenter

    lateinit var notificationIntent: Intent
    lateinit var pendingIntent: PendingIntent
    lateinit var notificationBuilder: NotificationCompat.Builder
//    lateinit var notificationManager: NotificationManager
    var progress: Int = 0

    lateinit var tpsId: String

    override fun onCreate() {
        super.onCreate()
        (application as BaseApp).appComponent.withServiceComponent(ServiceModule(this)).inject(this)
    }

    override fun onHandleIntent(intent: Intent) {
        tpsId = intent.getStringExtra("tps_id")
        presenter.init(this)
        notificationIntent = Intent()
        pendingIntent = PendingIntent.getActivity(
            this@UploadTpsService, 0, notificationIntent, 0)
        notificationBuilder = NotificationCompat.Builder(this@UploadTpsService,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel("upload", "Unggah Perhitungan")
            } else {
                ""
            }
        )
            .setSmallIcon(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                R.drawable.ic_notification_icon
            } else {
                R.drawable.ic_logo_notification_transparent_in
            })
            .setContentTitle(getTitle(progress))
            .setOngoing(true)
            .setAutoCancel(false)

        notificationBuilder.setContentIntent(pendingIntent)
//        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationBuilder.setProgress(100, progress, false)
//        notificationManager.notify(1, notificationBuilder.build())
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//        }
        startForeground(1, notificationBuilder.build())
        presenter.uploadTpsData(tpsId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val chan = NotificationChannel(channelId,
            channelName, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

    override fun increaseProgress(progress: Int) {
        this.progress += progress
        notificationBuilder.setContentTitle(getTitle(this.progress))
        notificationBuilder.setProgress(100, this.progress, false)
//        notificationManager.notify(1, notificationBuilder.build())
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//        }
        startForeground(1, notificationBuilder.build())
    }

    private fun getTitle(progress: Int): String {
        return when {
            progress < 5 -> "Mengunggah Perhitungan TPS"
            progress < 10 -> "Mengunggah Perhitungan - Presiden"
            progress < 15 -> "Mengunggah Perhitungan - DPR RI"
            progress < 20 -> "Mengunggah Perhitungan - DPD"
            progress < 25 -> "Mengunggah Perhitungan - DPRD Tingkat Provinsi"
            progress < 30 -> "Mengunggah Perhitungan - DPRD Tingkat Kabupaten"
            progress < 35 -> "Mengunggah Form C1 - Presiden"
            progress < 40 -> "Mengunggah Form C1 - DPR RI"
            progress < 45 -> "Mengunggah Form C1 - DPD"
            progress < 50 -> "Mengunggah Form C1 - DPRD Tingkat Provinsi"
            progress < 55 -> "Mengunggah Form C1 - DPRD Tingkat Kabupaten"
            progress < 60 -> "Mengunggah Gambar C1 - Presiden"
            progress < 65 -> "Mengunggah Gambar C1 - DPR RI"
            progress < 75 -> "Mengunggah Gambar C1 - DPD"
            progress < 80 -> "Mengunggah Gambar C1 - DPRD Tingkat Provinsi"
            progress < 85 -> "Mengunggah Gambar C1 - DPRD Tingkat Kabupaten"
            progress < 90 -> "Mengunggah Gambar Suasana TPS"
            else -> "Menyelesaikan Unggahan"
        }
    }

    override fun onSuccessPublishTps() {
        presenter.detach()
        publishResult(true, "")
    }

    private fun publishResult(isSuccess: Boolean, message: String?) {
        val intent = Intent("upload")
        intent.putExtra("status", isSuccess)
        intent.putExtra("message", message)
        sendBroadcast(intent)
    }

    override fun showError(throwable: Throwable) {
        Timber.e(throwable)
    }

    override fun showFailed(message: String?) {
        publishResult(false, message)
        presenter.detach()
    }

    override fun onDestroy() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(false)
        }
        super.onDestroy()
    }
}