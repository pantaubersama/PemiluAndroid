package com.pantaubersama.app.ui.notification

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.data.model.notification.NotificationWhole
import com.pantaubersama.app.utils.extensions.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_notif_badge.*
import kotlinx.android.synthetic.main.item_notif_general.*
import kotlinx.android.synthetic.main.item_notif_new_quiz.*
import kotlinx.android.synthetic.main.item_notif_question.*

class NotifAdapter : BaseRecyclerAdapter() {

    companion object {
        const val NOTIF_TYPE_GENERAL = 0
        const val NOTIF_TYPE_QUESTION = 1
        const val NOTIF_TYPE_QUIZ = 2
        const val NOTIF_TYPE_BADGE = 3
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            (data[position] as NotificationWhole).data.notif_type != null -> when ((data[position] as NotificationWhole).data.notif_type) {
                "quiz" -> NOTIF_TYPE_QUIZ
                "question" -> NOTIF_TYPE_QUESTION
                "badge" -> NOTIF_TYPE_BADGE
                else -> NOTIF_TYPE_GENERAL
            }
            (data[position] as NotificationWhole).data.payload.notif_type != null -> when ((data[position] as NotificationWhole).data.payload.notif_type) {
                "quiz" -> NOTIF_TYPE_QUIZ
                "question" -> NOTIF_TYPE_QUESTION
                "badge" -> NOTIF_TYPE_BADGE
                else -> NOTIF_TYPE_GENERAL
            }
            else -> NOTIF_TYPE_GENERAL
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            NOTIF_TYPE_QUIZ -> QuizViewHolder(parent.inflate(R.layout.item_notif_new_quiz))
            NOTIF_TYPE_QUESTION -> QuestionVideHolder(parent.inflate(R.layout.item_notif_question))
            NOTIF_TYPE_BADGE -> BadgeViewHolder(parent.inflate(R.layout.item_notif_badge))
            else -> NotificationViewHolder(parent.inflate(R.layout.item_notif_general))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? QuizViewHolder)?.bind(data[position] as NotificationWhole)
        (holder as? QuestionVideHolder)?.bind(data[position] as NotificationWhole)
        (holder as? BadgeViewHolder)?.bind(data[position] as NotificationWhole)
        (holder as? NotificationViewHolder)?.bind(data[position] as NotificationWhole)
    }

    inner class QuizViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(notification: NotificationWhole) {
            notif_new_quiz_message.text = notification.notification.body
        }
    }

    inner class NotificationViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(notification: NotificationWhole) {
            notif_message.text = notification.notification.body
        }
    }

    inner class QuestionVideHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(notification: NotificationWhole) {
            notif_question_message.text = notification.notification.body
            notif_question_text.text = notification.data.payload.question.body
        }
    }

    inner class BadgeViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(notification: NotificationWhole) {
            notif_badge_message.text = notification.notification.body
        }
    }
}