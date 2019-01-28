package com.pantaubersama.app.background.firebase

import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.pantaubersama.app.base.BaseApp
import com.pantaubersama.app.di.module.ServiceModule
import javax.inject.Inject
import androidx.core.app.NotificationManagerCompat
import com.pantaubersama.app.R
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

    override fun onMessageReceived(message: RemoteMessage?) {
        val mBuilder = NotificationCompat.Builder(this, message?.messageId.toString())
            .setSmallIcon(R.drawable.ic_notification_icon)
            .setLargeIcon(BitmapFactory.decodeResource(applicationContext.resources, R.drawable.ic_notification_icon))
            .setContentTitle(message?.notification?.title)
            .setContentText(message?.notification?.body)
            .setWhen(Calendar.getInstance().timeInMillis)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message?.notification?.body))
            .setAutoCancel(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBuilder.color = resources.getColor(R.color.colorPrimary)
        }
        with(NotificationManagerCompat.from(this)) {
            notify(NotificationID.id, mBuilder.build())
        }
    }
}