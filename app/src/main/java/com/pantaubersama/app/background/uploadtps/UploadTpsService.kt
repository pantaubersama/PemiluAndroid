package com.pantaubersama.app.background.uploadtps

import android.app.* // ktlint-disable
import android.content.Intent
import com.pantaubersama.app.base.BaseApp
import com.pantaubersama.app.di.module.ServiceModule
import javax.inject.Inject
import android.content.Context
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.pantaubersama.app.BuildConfig
import com.pantaubersama.app.R
import com.pantaubersama.app.background.firebase.NotificationID
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.extensions.color
import timber.log.Timber

class UploadTpsService : IntentService("UploadTpsService"), UploadTpsView {
    @Inject
    lateinit var presenter: UploadTpsPresenter

    private lateinit var notificationIntent: Intent
    private lateinit var pendingIntent: PendingIntent
    private lateinit var notificationBuilder: NotificationCompat.Builder
    lateinit var notificationManager: NotificationManager
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
        notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationBuilder = NotificationCompat.Builder(this@UploadTpsService,
            PantauConstants.Notification.NOTIFICATION_CHANNEL_NAME_UPLOAD
        )
            .setSmallIcon(android.R.drawable.stat_sys_upload)
            .setColor(color(R.color.colorPrimary))
            .setContentTitle(getTitle(progress))
            .setContentInfo(BuildConfig.APPLICATION_ID)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                PantauConstants.Notification.NOTIFICATION_CHANNEL_ID_UPLOAD,
                PantauConstants.Notification.NOTIFICATION_CHANNEL_NAME_UPLOAD,
                NotificationManager.IMPORTANCE_LOW
            )
            notificationChannel.description = PantauConstants.Notification.NOTIFICATION_CHANNEL_DESC_UPLOAD
            notificationChannel.enableVibration(false)

            notificationManager.createNotificationChannel(notificationChannel)
            notificationBuilder.setChannelId(PantauConstants.Notification.NOTIFICATION_CHANNEL_ID_UPLOAD)
        }

        notificationBuilder.setProgress(100, progress, false)
        notificationManager.notify(1, notificationBuilder.build())
        presenter.uploadTpsData(tpsId)
    }

    override fun increaseProgress(progress: Int) {
        this.progress += progress
        notificationBuilder.setContentTitle(getTitle(this.progress))
        notificationBuilder.setProgress(100, this.progress, false)
        notificationManager.notify(1, notificationBuilder.build())
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
        sendBroadcast(intent)
        intent.putExtra("status", isSuccess)
        intent.putExtra("message", message)
        createNotification(intent)

    }

    override fun showError(throwable: Throwable) {
        Timber.e(throwable)
    }

    override fun showFailed(message: String?) {
        publishResult(false, message)
        presenter.detach()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun createNotification(intent: Intent) {
        notificationManager.cancel(1)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, Intent(), PendingIntent.FLAG_UPDATE_CURRENT)
        val notificationBuilder = NotificationCompat.Builder(this, PantauConstants.Notification.NOTIFICATION_CHANNEL_NAME_UPLOAD)
            .setColor(color(R.color.colorPrimary))
            .setContentTitle(if (intent.getBooleanExtra("status", false)) {
                "Berhasil mengunggah perhitungan"
            } else {
                "Gagal mengunggah perhitungan"
            })
            .setContentInfo(BuildConfig.APPLICATION_ID)
            .setContentText(if (intent.getBooleanExtra("status", false)) {
                "Perhitungan kamu berhasil terunggah"
            } else {
                intent.getStringExtra("message")
            })
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(android.R.drawable.stat_sys_upload_done)

        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                PantauConstants.Notification.NOTIFICATION_CHANNEL_ID_UPLOAD,
                PantauConstants.Notification.NOTIFICATION_CHANNEL_NAME_UPLOAD,
                NotificationManager.IMPORTANCE_LOW
            )
            notificationChannel.description = PantauConstants.Notification.NOTIFICATION_CHANNEL_DESC_UPLOAD
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)
            notificationBuilder.setChannelId(PantauConstants.Notification.NOTIFICATION_CHANNEL_ID_UPLOAD)
        }
        notificationManager.notify(NotificationID.id, notificationBuilder.build())
    }
}