package com.pantaubersama.app.ui.penpol.tanyakandidat.list

import android.animation.ValueAnimator
import android.view.* // ktlint-disable
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.base.viewholder.LoadingViewHolder
import com.pantaubersama.app.data.model.LoadingModel
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.data.model.tanyakandidat.Pertanyaan
import com.pantaubersama.app.data.model.user.EMPTY_PROFILE
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.ui.widget.BannerViewHolder
import com.pantaubersama.app.utils.extensions.inflate
import com.pantaubersama.app.utils.extensions.loadUrl
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_tanya_kandidat.*
import kotlinx.android.synthetic.main.layout_action_post.*
import kotlinx.android.synthetic.main.header_item_layout.*

class TanyaKandidatAdapter() : BaseRecyclerAdapter() {
    private var profile: Profile = EMPTY_PROFILE
    var listener: TanyaKandidatAdapter.AdapterListener? = null

    override fun getItemViewType(position: Int): Int {
        return if (data[position] is BannerInfo) {
            VIEW_TYPE_BANNER
        } else if (data[position] is LoadingModel) {
            VIEW_TYPE_LOADING
        } else if (data[position] is Profile) {
            VIEW_TYPE_HEADER
        } else {
            VIEW_TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_LOADING -> LoadingViewHolder(parent.inflate(R.layout.item_loading))
            VIEW_TYPE_HEADER -> HeaderViewHolder(parent.inflate(R.layout.header_item_layout))
            VIEW_TYPE_BANNER -> BannerViewHolder(parent.inflate(R.layout.item_banner_container),
                onClick = { listener?.onClickBanner(data[it] as BannerInfo) },
                onRemove = { removeBanner() })
            else -> TanyaKandidatViewHolder(parent.inflate(R.layout.item_tanya_kandidat))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? LoadingViewHolder)?.bind()
        (holder as? HeaderViewHolder)?.onBind(data[position] as Profile)
        (holder as? BannerViewHolder)?.bind(data[position] as BannerInfo)
        (holder as? TanyaKandidatViewHolder)?.onBind(data[position] as Pertanyaan)
    }

    inner class TanyaKandidatViewHolder(
        override val containerView: View?
    ) : RecyclerView.ViewHolder(
        containerView!!), LayoutContainer {
        val animator = ValueAnimator.ofFloat(0.0f, 1.0f).setDuration(1000)

        fun onBind(item: Pertanyaan) {
            iv_user_avatar.loadUrl(item.user.avatar?.url, R.drawable.ic_avatar_placeholder)
            tv_user_name.text = item.user.fullName
            tv_user_bio.text = item.user.about
            question_time.text = item.createdAtInWord?.id
            upvote_count_text.text = item.likeCount.toString()
            user_question.text = item.body
            user_question.maxLines = 5
            iv_options_button.setOnClickListener {
//                showOptionsDialog(itemView)
                listener?.onClickTanyaOption(item, adapterPosition)
            }
            iv_share_button.setOnClickListener {
                listener?.onClickShare(item)
            }
            if ((data[adapterPosition] as Pertanyaan).isliked) {
                upvote_animation.progress = 1.0f
            } else {
                upvote_animation.progress = 0.0f
            }
            upvote_container.setOnClickListener {
                if (profile != EMPTY_PROFILE) {
                    if (!animator.isRunning) {
                        setUpvoted(item)
                    }
                } else {
                    listener?.onclickActionUnauthorized()
                }
            }
            layout_item_tanya_kandidat.setOnClickListener {
                item.let { item -> listener?.onClickContent(item, adapterPosition) }
            }
        }

        private fun setUpvoted(item: Pertanyaan) {
            val upVoted = item.isliked
            item.isliked = !item.isliked
            if (!upVoted) {
                item.likeCount += 1
                animator.addUpdateListener { animation ->
                    upvote_animation.progress = animation.animatedValue as Float
                    upvote_count_text.text = item.likeCount.toString()
                }
                animator.start()
            } else {
                item.likeCount -= 1
                upvote_count_text.text = item.likeCount.toString()
                upvote_animation.progress = 0.0f
            }
            listener?.onClickUpvote(item.id, upVoted, adapterPosition)
        }
    }

    inner class HeaderViewHolder(
        override val containerView: View?
    ) : RecyclerView.ViewHolder(containerView!!), LayoutContainer {
        fun onBind(item: Profile) {
            header_user_avatar.loadUrl(item.avatar?.medium?.url, R.drawable.ic_avatar_placeholder)
            user_name.text = item.fullName
            header_hint.text = itemView.context.getString(R.string.question_short_hint)
            question_section.setOnClickListener {
                listener?.onClickHeader()
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun addHeader(profile: Profile) {
        if (data.size != 0 && data[0] is BannerInfo) {
            addItem(profile, 1)
        } else {
            addItem(profile, 0)
        }
    }

    fun reverseVote(liked: Boolean, position: Int) {
        (data[position] as Pertanyaan).isliked = liked
        notifyItemChanged(position)
    }

    fun deleteItem(position: Int?) {
        data.removeAt(position!!)
        notifyItemRemoved(position)
    }

    fun addBanner(bannerInfo: BannerInfo) {
        addItem(bannerInfo, 0)
    }

    private fun removeBanner() {
        if (data[0] is BannerInfo) {
            deleteItem(0)
        }
    }

    fun setHaveUser(profile: Profile) {
        this.profile = profile
    }

    companion object {
        var VIEW_TYPE_LOADING = 0
        var VIEW_TYPE_HEADER = 1
        var VIEW_TYPE_ITEM = 2
        var VIEW_TYPE_BANNER = 3
    }

    interface AdapterListener {
        fun onClickBanner(bannerInfo: BannerInfo)
        fun onClickHeader()
        fun onClickShare(item: Pertanyaan?)
        fun onClickUpvote(id: String, isLiked: Boolean, position: Int)
        fun onClickDeleteItem(id: String, position: Int)
        fun onClickCopyUrl(id: String)
        fun onClickLapor(id: String)
        fun onClickTanyaOption(item: Pertanyaan, position: Int)
        fun onClickContent(item: Pertanyaan, position: Int)
        fun onclickActionUnauthorized()
    }
}