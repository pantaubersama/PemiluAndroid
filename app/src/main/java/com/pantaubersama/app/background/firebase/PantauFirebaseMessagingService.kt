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
import com.pantaubersama.app.BuildConfig
import com.pantaubersama.app.R
import com.pantaubersama.app.ui.home.HomeActivity
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
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setLargeIcon(BitmapFactory.decodeResource(applicationContext.resources, R.drawable.ic_notification_icon))
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
//            Timber.d("edityo notif NotificationManagerCompat.notify")
//        }
//        val title = remoteMessage?.notification?.title
//        val message = remoteMessage?.notification?.body
        val title = remoteMessage?.data?.getValue("title")
        val message = remoteMessage?.data?.getValue("body")
        createNotif(Intent(this, HomeActivity::class.java), title, message)
//        notif()
        Timber.d("edityo notif remoteMessage : $remoteMessage")
        Timber.d("edityo notif data : ${remoteMessage?.data}")
    }

    private fun notif() {
        val mBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_NAME_BROADCAST)

        val bigText = NotificationCompat.BigTextStyle()
        bigText.bigText("OYYYY")
        bigText.setBigContentTitle("Today's Bible Verse")
        bigText.setSummaryText("Text in detail")

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.androidauthority.com/"))
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        mBuilder.setContentIntent(pendingIntent)
        mBuilder.setSmallIcon(R.mipmap.ic_launcher_round)
        mBuilder.setContentTitle("My notification")
        mBuilder.setContentText("Hello World!")
        mBuilder.setStyle(bigText)

        val mNotificationManager =  getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID_BROADCAST,
                NOTIFICATION_CHANNEL_NAME_BROADCAST,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationChannel.description = NOTIFICATION_CHANNEL_DESC_BROADCAST
            mNotificationManager.createNotificationChannel(notificationChannel)
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID_BROADCAST)
            Timber.d("edityo notif channel added")
        }

        mNotificationManager.notify(0, mBuilder.build())

        Timber.d("edityo notif notify notif created()")

    }

    private fun createNotif(intent: Intent, title: String?, message: String?, largeIcon: Bitmap? = null) {
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_NAME_BROADCAST)
//            .setSmallIcon(R.mipmap.ic_launcher_round)
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


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            notificationBuilder.setSmallIcon(R.drawable.ic_logo_notification_transparent_in)
        } else {
            notificationBuilder.setSmallIcon(R.drawable.ic_notification_icon)
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID_BROADCAST,
                NOTIFICATION_CHANNEL_NAME_BROADCAST,
                NotificationManager.IMPORTANCE_MAX
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
            Timber.d("edityo notif channel added")
        }
        notificationManager.notify(NotificationID.id, notificationBuilder.build())
        Timber.d("edityo notif notif created()")
    }
}