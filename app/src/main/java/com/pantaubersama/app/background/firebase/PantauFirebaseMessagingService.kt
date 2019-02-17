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
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.orhanobut.logger.Logger
import com.pantaubersama.app.BuildConfig
import com.pantaubersama.app.R
import com.pantaubersama.app.data.model.notification.NotificationData
import com.pantaubersama.app.data.model.notification.PemiluBroadcast
import com.pantaubersama.app.data.model.notification.QuestionNotif
import com.pantaubersama.app.data.model.notification.AchievedBadgeNotif
import com.pantaubersama.app.data.model.notification.QuizNotif
import com.pantaubersama.app.ui.home.HomeActivity
import com.pantaubersama.app.ui.penpol.kuis.detail.DetailKuisActivity
import com.pantaubersama.app.ui.penpol.tanyakandidat.detail.DetailTanyaKandidatActivity
import com.pantaubersama.app.ui.profile.ProfileActivity
import com.pantaubersama.app.ui.profile.setting.badge.detail.DetailBadgeActivity
import com.pantaubersama.app.ui.splashscreen.SplashScreenActivity
import com.pantaubersama.app.ui.webview.ChromeTabActivity
import com.pantaubersama.app.utils.FacebookEventLogger
import com.pantaubersama.app.utils.GlideApp
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_TYPE_FEED
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_TYPE_JANPOL
import com.pantaubersama.app.utils.PantauConstants.Notification.NOTIFICATION_CHANNEL_DESC_BROADCAST
import com.pantaubersama.app.utils.PantauConstants.Notification.NOTIFICATION_CHANNEL_ID_BROADCAST
import com.pantaubersama.app.utils.PantauConstants.Notification.NOTIFICATION_CHANNEL_NAME_BROADCAST
import com.pantaubersama.app.utils.PantauConstants.Notification.NOTIFICATION_TYPE
import com.pantaubersama.app.utils.PantauConstants.Notification.NOTIFICATION_TYPE_BADGE
import com.pantaubersama.app.utils.PantauConstants.Notification.NOTIFICATION_TYPE_BROADCAST
import com.pantaubersama.app.utils.PantauConstants.Notification.NOTIFICATION_TYPE_FEED
import com.pantaubersama.app.utils.PantauConstants.Notification.NOTIFICATION_TYPE_JANPOL
import com.pantaubersama.app.utils.PantauConstants.Notification.NOTIFICATION_TYPE_PROFILE
import com.pantaubersama.app.utils.PantauConstants.Notification.NOTIFICATION_TYPE_QUESTION
import com.pantaubersama.app.utils.PantauConstants.Notification.NOTIFICATION_TYPE_QUIZ
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
            val notificationData: NotificationData? = gson.fromJson(payload.getJSONObject(NotificationData.TAG).toString(), NotificationData::class.java)
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
                    title = notificationData?.title
                    description = notificationData?.body
                    intent = HomeActivity.setIntentByOpenedTab(this, EXTRA_TYPE_JANPOL)
                    intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
                    createNotif(pendingIntent, title, description)
                }
                NOTIFICATION_TYPE_FEED -> {
                    title = notificationData?.title
                    description = notificationData?.body
                    intent = HomeActivity.setIntentByOpenedTab(this, EXTRA_TYPE_FEED)
                    intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
                    createNotif(pendingIntent, title, description)
                }
                NOTIFICATION_TYPE_QUESTION -> {
                    val question = gson.fromJson(payload.getJSONObject(QuestionNotif.TAG).toString(), QuestionNotif::class.java)
                    title = notificationData?.title
                    description = notificationData?.body
                    intent = DetailTanyaKandidatActivity.setIntent(this, question.id)
                    intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
                    createNotif(pendingIntent, title, description)
                }
                NOTIFICATION_TYPE_BADGE -> {
                    val achievedBadgeData = gson.fromJson(payload.getJSONObject(AchievedBadgeNotif.TAG).toString(), AchievedBadgeNotif::class.java)
                    val achievedId = achievedBadgeData.achievedId
                    val badge = achievedBadgeData.badge
                    title = notificationData?.title
                    description = notificationData?.body

                    intent = DetailBadgeActivity.setIntent(this, achievedId)
                    intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

                    GlideApp.with(this)
                        .asBitmap()
                        .load(badge.image.thumbnail)
                        .listener(object : RequestListener<Bitmap> {
                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                                createNotif(pendingIntent, title, description)
                                return false
                            }

                            override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                createNotif(pendingIntent, title, description, resource)
                                return true
                            }
                        })
                        .submit()
                        FacebookEventLogger.logUnlockedAchievementEvent(this, badge.name)
                }
                NOTIFICATION_TYPE_QUIZ -> {
                    val quizNotif = gson.fromJson(payload.getJSONObject(QuizNotif.TAG).toString(), QuizNotif::class.java)
                    title = notificationData?.title
                    description = notificationData?.body

                    intent = DetailKuisActivity.setIntent(this, quizNotif.id)
                    intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

                    GlideApp.with(this)
                        .asBitmap()
                        .load(quizNotif.image.large)
                        .listener(object : RequestListener<Bitmap> {
                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                                createNotif(pendingIntent, title, description)
                                return false
                            }

                            override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                createNotif(pendingIntent, title, description, bigPicture = resource)
                                return true
                            }
                        })
                        .submit()
                }
                NOTIFICATION_TYPE_PROFILE -> {
                    title = notificationData?.title
                    description = notificationData?.body
                    intent = Intent(this, ProfileActivity::class.java)
                    intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
                    Logger.d("NOTIFICATION_TYPE_PROFILE")
                    createNotif(pendingIntent, title, description)
                }
                else -> {
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

    private fun createNotif(pendingIntent: PendingIntent, title: String?, description: String?, largeIcon: Bitmap? = null, bigPicture: Bitmap? = null) {
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

        largeIcon?.let { notificationBuilder.setLargeIcon(it) }

        bigPicture?.let {
            notificationBuilder.setLargeIcon(bigPicture)
            notificationBuilder.setStyle(NotificationCompat.BigPictureStyle()
                .bigPicture(bigPicture)
                .bigLargeIcon(null))
        } ?: notificationBuilder.setStyle(NotificationCompat.BigTextStyle().bigText(description))

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