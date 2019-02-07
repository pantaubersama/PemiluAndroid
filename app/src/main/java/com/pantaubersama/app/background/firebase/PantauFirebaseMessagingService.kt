package com.pantaubersama.app.background.firebase

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.pantaubersama.app.base.BaseApp
import com.pantaubersama.app.di.module.ServiceModule
import javax.inject.Inject
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.orhanobut.logger.Logger
import com.pantaubersama.app.BuildConfig
import com.pantaubersama.app.R
import com.pantaubersama.app.data.model.notification.NotificationData
import com.pantaubersama.app.data.model.notification.PemiluBroadcast
import com.pantaubersama.app.ui.home.HomeActivity
import com.pantaubersama.app.ui.splashscreen.SplashScreenActivity
import com.pantaubersama.app.ui.webview.ChromeTabActivity
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_TYPE_FEED
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_TYPE_JANPOL
import com.pantaubersama.app.utils.PantauConstants.Notification.NOTIFICATION_CHANNEL_DESC_BROADCAST
import com.pantaubersama.app.utils.PantauConstants.Notification.NOTIFICATION_CHANNEL_ID_BROADCAST
import com.pantaubersama.app.utils.PantauConstants.Notification.NOTIFICATION_CHANNEL_NAME_BROADCAST
import com.pantaubersama.app.utils.PantauConstants.Notification.NOTIFICATION_TYPE
import com.pantaubersama.app.utils.PantauConstants.Notification.NOTIFICATION_TYPE_BROADCAST
import com.pantaubersama.app.utils.PantauConstants.Notification.NOTIFICATION_TYPE_FEED
import com.pantaubersama.app.utils.PantauConstants.Notification.NOTIFICATION_TYPE_JANPOL
import org.json.JSONObject

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
        val gson: Gson = GsonBuilder().setFieldNamingPolicy(com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()
        var intent: Intent? = null
        var title: String? = null
        var description: String? = null
        var largeIcon: String? = null

        Logger.d(remoteMessage?.notification.toString())
        Logger.json(remoteMessage?.data.toString())

        var notifType = ""
        remoteMessage?.data?.get("payload")?.let { payloadData ->
            val payload = JSONObject(payloadData)
            payload.optString(NOTIFICATION_TYPE)?.let { notifType = it }
            when (notifType) {
                NOTIFICATION_TYPE_BROADCAST -> {
                    val pemiluBroadcast = gson.fromJson(payload.getJSONObject(PemiluBroadcast.TAG).toString(), PemiluBroadcast::class.java)
                    title = pemiluBroadcast.title
                    description = pemiluBroadcast.description
                    val broadcastUrl = pemiluBroadcast.link
                    intent = ChromeTabActivity.setIntent(this, broadcastUrl)
                    intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
                    createNotif(pendingIntent, title, description)
                }
                NOTIFICATION_TYPE_JANPOL -> {
                    val notificationData = gson.fromJson(payload.getJSONObject(NotificationData().TAG).toString(), NotificationData::class.java)
                    title = notificationData.title
                    description = notificationData.body
                    intent = HomeActivity.setIntentByOpenedTab(this, EXTRA_TYPE_JANPOL)
                    intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
                    createNotif(pendingIntent, title, description)
                }
                NOTIFICATION_TYPE_FEED -> {
                    val notificationData = gson.fromJson(payload.getJSONObject(NotificationData().TAG).toString(), NotificationData::class.java)
                    title = notificationData.title
                    description = notificationData.body
                    intent = HomeActivity.setIntentByOpenedTab(this, EXTRA_TYPE_FEED)
                    intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
                    createNotif(pendingIntent, title, description)
                }
                else -> {
                    val notificationData: NotificationData? = gson.fromJson(payload.getJSONObject(NotificationData().TAG).toString(), NotificationData::class.java)
                    title = notificationData?.title ?: remoteMessage.notification?.title
                    description = notificationData?.body ?: remoteMessage.notification?.body

                    intent = Intent(this, SplashScreenActivity::class.java)
                    intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
                    title?.let {
                        createNotif(pendingIntent, title, description)
                    }
                }
            }
        }
    }

    private fun createNotif(pendingIntent: PendingIntent, title: String?, description: String?, largeIcon: Bitmap? = null) {
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_NAME_BROADCAST)
            .setSmallIcon(R.drawable.ic_notification_icon)
            .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
            .setContentTitle(title)
            .setContentInfo(BuildConfig.APPLICATION_ID)
            .setContentText(description)
            .setAutoCancel(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentIntent(pendingIntent)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(Notification.DEFAULT_ALL)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(description))

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