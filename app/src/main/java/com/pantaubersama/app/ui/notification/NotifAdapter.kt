package com.pantaubersama.app.ui.notification

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.base.viewholder.LoadingViewHolder
import com.pantaubersama.app.data.model.notification.NotificationWhole
import com.pantaubersama.app.utils.extensions.inflate
import com.pantaubersama.app.utils.extensions.loadUrl
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_notif_badge.*
import kotlinx.android.synthetic.main.item_notif_general.*
import kotlinx.android.synthetic.main.item_notif_new_quiz.*
import kotlinx.android.synthetic.main.item_notif_question.*

class NotifAdapter : BaseRecyclerAdapter() {
    var listener: Listener? = null

    companion object {
        const val NOTIF_TYPE_GENERAL = 0
        const val NOTIF_TYPE_QUESTION = 1
        const val NOTIF_TYPE_QUIZ = 2
        const val NOTIF_TYPE_BADGE = 3
        const val NOTIF_TYPE_LOADING = 4
    }

    override fun getItemViewType(position: Int): Int {
        if (data[position] is NotificationWhole) {
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
        } else {
            return NOTIF_TYPE_LOADING
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            NOTIF_TYPE_QUIZ -> QuizViewHolder(parent.inflate(R.layout.item_notif_new_quiz))
            NOTIF_TYPE_QUESTION -> QuestionVideHolder(parent.inflate(R.layout.item_notif_question))
            NOTIF_TYPE_BADGE -> BadgeViewHolder(parent.inflate(R.layout.item_notif_badge))
            NOTIF_TYPE_LOADING -> LoadingViewHolder(parent.inflate(R.layout.item_loading))
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
            itemView.setOnClickListener {
                if (notification.data.payload.quizNotif != null) {
                    notification.data.payload.quizNotif?.id?.let {
                        listener?.onClickQuiz(it)
                    }
                }
            }
            notif_new_quiz_follow.setOnClickListener {
                if (notification.data.payload.quizNotif != null) {
                    notification.data.payload.quizNotif?.id?.let {
                        listener?.onClickQuiz(it)
                    }
                }
            }
        }
    }

    inner class NotificationViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(notification: NotificationWhole) {
            notif_message.text = notification.notification.body
            itemView.setOnClickListener {
                if (notification.broadcast != null) {
                    notification.broadcast?.link?.let {
                        listener?.onClickBroadcast(it)
                    }
                } else if (notification.data.payload.broadcast != null) {
                    notification.data.payload.broadcast?.link?.let {
                        listener?.onClickBroadcast(it)
                    }
                }
            }
        }
    }

    inner class QuestionVideHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(notification: NotificationWhole) {
            notif_question_message.text = notification.notification.body
            notif_question_text.text = notification.data.payload.question.body
            try {
                notif_question_avatar.loadUrl(notification.data.payload.question.upvotedBy.avatar.url, R.drawable.ic_avatar_placeholder)
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }
            question_container.setOnClickListener {
                listener?.onClickTanyaKandidat(notification.data.payload.question.id)
            }
            itemView.setOnClickListener {
                listener?.onClickTanyaKandidat(notification.data.payload.question.id)
            }
        }
    }

    inner class BadgeViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(notification: NotificationWhole) {
            notif_badge_message.text = notification.notification.body
            notif_badge_avatar.loadUrl(notification.data.payload.badgeNotif.badge.image.thumbnail)
            itemView.setOnClickListener {
                listener?.onClickBadge(notification.data.payload.badgeNotif.achievedId)
            }
        }
    }

    interface Listener {
        fun onClickTanyaKandidat(id: String)
        fun onClickBadge(id: String)
        fun onClickQuiz(id: String)
        fun onClickBroadcast(link: String)
    }
}