package com.pantaubersama.app.background.firebase

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.pantaubersama.app.base.BaseApp
import com.pantaubersama.app.di.module.ServiceModule
import javax.inject.Inject
import androidx.core.content.ContextCompat
import com.orhanobut.logger.Logger
import com.pantaubersama.app.BuildConfig
import com.pantaubersama.app.R
import com.pantaubersama.app.data.model.notification.PemiluBroadcast
import com.pantaubersama.app.ui.home.HomeActivity
import com.pantaubersama.app.utils.PantauConstants.Notification.NOTIFICATION_CHANNEL_DESC_BROADCAST
import com.pantaubersama.app.utils.PantauConstants.Notification.NOTIFICATION_CHANNEL_ID_BROADCAST
import com.pantaubersama.app.utils.PantauConstants.Notification.NOTIFICATION_CHANNEL_NAME_BROADCAST
import com.pantaubersama.app.utils.PantauConstants.Notification.NOTIFICATION_TYPE
import com.pantaubersama.app.utils.PantauConstants.Notification.NOTIFICATION_TYPE_BROADCAST
import org.json.JSONObject
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
        var intent: Intent? = null
        var title: String? = null
        var message: String? = null
        var largeIcon: String? = null

        var notifType = ""
        val payload = JSONObject(remoteMessage?.data?.get("payload"))
        payload.optString(NOTIFICATION_TYPE)?.let { notifType = it }

        when (notifType) {
            NOTIFICATION_TYPE_BROADCAST -> {
//                var pemiluBroadcast = ge
            }
        }
//        val title = remoteMessage?.data?.getValue("title")
//        val message = remoteMessage?.data?.getValue("body")
//        createNotif(Intent(this, HomeActivity::class.java), title, message)
    }

    private fun createNotif(intent: Intent, title: String?, message: String?, largeIcon: Bitmap? = null) {
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_NAME_BROADCAST)
            .setSmallIcon(R.drawable.ic_notification_icon)
            .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
            .setContentTitle(title)
            .setContentInfo(BuildConfig.APPLICATION_ID)
            .setContentText(message)
            .setAutoCancel(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentIntent(pendingIntent)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(Notification.DEFAULT_ALL)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID_BROADCAST,
                NOTIFICATION_CHANNEL_NAME_BROADCAST,
                NotificationManager.IMPORTANCE_DEFAULT
                )
            notificationChannel.description = NOTIFICATION_CHANNEL_DESC_BROADCAST
            notificationChannel.enableVibration(true)

            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
            notificationChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), audioAttributes)
            notificationManager.createNotificationChannel(notificationChannel)
            notificationBuilder.setChannelId(NOTIFICATION_CHANNEL_ID_BROADCAST)
        }
        notificationManager.notify(NotificationID.id, notificationBuilder.build())
    }
}