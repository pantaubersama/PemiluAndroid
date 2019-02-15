package com.pantaubersama.app.utils

import android.content.Context
import android.os.Bundle
import com.facebook.appevents.AppEventsConstants
import com.facebook.appevents.AppEventsLogger
import android.util.StatsLog.logEvent

class FacebookEventLogger {
    companion object {
        fun logRatedEvent(context: Context, contentType: String, contentData: String, contentId: String, maxRatingValue: Int, ratingGiven: Double) {
            val params = Bundle()
            params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, contentType)
            params.putString(AppEventsConstants.EVENT_PARAM_CONTENT, contentData)
            params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, contentId)
            params.putInt(AppEventsConstants.EVENT_PARAM_MAX_RATING_VALUE, maxRatingValue)
            AppEventsLogger.newLogger(context).logEvent(AppEventsConstants.EVENT_NAME_RATED, ratingGiven, params)
        }

        fun logUnlockedAchievementEvent(context: Context, description: String) {
            val params = Bundle()
            params.putString(AppEventsConstants.EVENT_PARAM_DESCRIPTION, description)
            AppEventsLogger.newLogger(context).logEvent(AppEventsConstants.EVENT_NAME_UNLOCKED_ACHIEVEMENT, params)
        }

        fun logCompletedRegistrationEvent(context: Context, registrationMethod: String) {
            val params = Bundle()
            params.putString(AppEventsConstants.EVENT_PARAM_REGISTRATION_METHOD, registrationMethod)
            AppEventsLogger.newLogger(context).logEvent(AppEventsConstants.EVENT_NAME_COMPLETED_REGISTRATION, params)
        }

        fun logViewedContentEvent(context: Context, contentType: String, contentData: String, contentId: String) {
            val params = Bundle()
            params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, contentType)
            params.putString(AppEventsConstants.EVENT_PARAM_CONTENT, contentData)
            params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, contentId)
            AppEventsLogger.newLogger(context).logEvent(AppEventsConstants.EVENT_NAME_VIEWED_CONTENT, params)
        }
    }
}