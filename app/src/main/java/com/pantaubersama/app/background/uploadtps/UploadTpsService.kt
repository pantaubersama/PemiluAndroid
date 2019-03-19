package com.pantaubersama.app.background.uploadtps

import android.app.IntentService
import android.content.Intent
import com.pantaubersama.app.base.BaseApp
import com.pantaubersama.app.di.module.ServiceModule
import javax.inject.Inject
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.pantaubersama.app.R
import timber.log.Timber

class UploadTpsService : IntentService("UploadTpsService"), UploadTpsView {
    @Inject
    lateinit var presenter: UploadTpsPresenter

    lateinit var notificationIntent: Intent
    lateinit var pendingIntent: PendingIntent
    lateinit var notificationBuilder: NotificationCompat.Builder
    lateinit var notificationManager: NotificationManager
    var progress: Int = 0

    lateinit var tpsId: String

    override fun onCreate() {
        super.onCreate()
        (application as BaseApp).appComponent.withServiceComponent(ServiceModule(this)).inject(this)
    }

    override fun onHandleIntent(intent: Intent) {
        presenter.init(this)
        tpsId = intent.getStringExtra("tps_id")
        notificationIntent = Intent()
        pendingIntent = PendingIntent.getActivity(
            this@UploadTpsService, 0, notificationIntent, 0)
        notificationBuilder = NotificationCompat.Builder(this@UploadTpsService)
            .setSmallIcon(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                R.drawable.ic_notification_icon
            } else {
                R.drawable.ic_logo_notification_transparent_in
            })
            .setContentTitle(getTitle(progress))
            .setOngoing(true)

        notificationBuilder.setContentIntent(pendingIntent)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
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
            progress < 5 -> "Mengunggah Data TPS"
            progress < 10 -> "Mengunggah Perhitungan - Presiden"
            progress < 15 -> "Mengunggah Perhitungan - DPR RI"
            progress < 20 -> "Mengunggah Perhitungan - DPD"
            progress < 25 -> "Mengunggah Perhitungan - DPRD Tingkat Provinsi"
            progress < 30 -> "Mengunggah Perhitungan - DPRD Tingkat Kabupaten"
            else -> "Berhasil Mengunggah Data TPS"
        }
    }

    override fun showError(throwable: Throwable) {
        Timber.e(throwable)
    }

    override fun showFailed() {
        val pendingIntent = PendingIntent.getActivity(this@UploadTpsService, 1, notificationIntent, 0)
        val mBuilder = NotificationCompat.Builder(this@UploadTpsService)
            .setSmallIcon(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                R.drawable.ic_notification_icon
            } else {
                R.drawable.ic_logo_notification_transparent_in
            })
            .setContentTitle("Gagal mengunggah data")
            .setContentText("Terjadi masalah saat mengunggah data")

        mBuilder.setContentIntent(pendingIntent)
        notificationManager.notify(1, mBuilder.build())
        stopSelf()
    }
}