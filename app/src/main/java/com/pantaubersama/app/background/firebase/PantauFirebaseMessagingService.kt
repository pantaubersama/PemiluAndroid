package com.pantaubersama.app.background.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.pantaubersama.app.base.BaseApp
import com.pantaubersama.app.di.module.ServiceModule
import javax.inject.Inject
import androidx.core.content.ContextCompat
import com.pantaubersama.app.BuildConfig
import com.pantaubersama.app.R
import com.pantaubersama.app.utils.PantauConstants.Notification.NOTIFICATION_CHANNEL_DESC_BROADCAST
import com.pantaubersama.app.utils.PantauConstants.Notification.NOTIFICATION_CHANNEL_ID_BROADCAST
import com.pantaubersama.app.utils.PantauConstants.Notification.NOTIFICATION_CHANNEL_NAME_BROADCAST
import timber.log.Timber
import java.util.Calendar

class PantauFirebaseMessagingService : FirebaseMessagingService() {
    @Inject
    lateinit var presenter: PantauFirebaseMessagingPresenter

    override fun onCreate() {
        super.onCreate()
        (application as BaseApp).appComponent.withServiceComponent(ServiceModule(this)).inject(this)
    }

    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        token?.let { presenter.saveFirebaseNewToken(it) }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        val mBuilder = NotificationCompat.Builder(this, remoteMessage?.messageId.toString())
//            .setSmallIcon(R.drawable.ic_notification_icon)
//            .setSmallIcon(R.drawable.ic_notification_icon)
//            .setLargeIcon(BitmapFactory.decodeResource(applicationContext.resources, R.drawable.ic_notification_icon))
            .setContentTitle(remoteMessage?.notification?.title)
            .setContentText(remoteMessage?.notification?.body)
            .setWhen(Calendar.getInstance().timeInMillis)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setStyle(NotificationCompat.BigTextStyle().bigText(remoteMessage?.notification?.body))
            .setAutoCancel(true)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            mBuilder.color = resources.getColor(R.color.colorPrimary)
//        }
//        with(NotificationManagerCompat.from(this)) {
//            notify(NotificationID.id, mBuilder.build())
//        }
        val title = remoteMessage?.notification?.title
        val message = remoteMessage?.notification?.body
        createNotif(Intent(), title, message)
        Timber.d("edityo notif remoteMessage : $remoteMessage")
        Timber.d("edityo notif data : ${remoteMessage?.data}")
    }

    private fun createNotif(intent: Intent, title: String?, message: String?, largeIcon: Bitmap? = null) {
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_NAME_BROADCAST)
            .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
            .setContentTitle(title)
            .setContentInfo(BuildConfig.APPLICATION_ID)
            .setContentText(message)
            .setAutoCancel(true)
//            .setSound()
//            .setContentIntent()
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.drawable.ic_logo_notification_transparent_in)
        } else {
            notificationBuilder.setSmallIcon(R.drawable.ic_notification_icon)
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID_BROADCAST,
                NOTIFICATION_CHANNEL_NAME_BROADCAST,
                NotificationManager.IMPORTANCE_DEFAULT
                )
            notificationChannel.description = NOTIFICATION_CHANNEL_DESC_BROADCAST
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.WHITE
            notificationManager.createNotificationChannel(notificationChannel)
            Timber.d("edityo notif channel added")
        }
        notificationManager.notify(NotificationID.id, notificationBuilder.build())
        Timber.d("edityo notif notif created()")
    }
}

