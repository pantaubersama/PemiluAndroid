package com.pantaubersama.app.ui.penpol.tanyakandidat.list

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.* // ktlint-disable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.base.viewholder.LoadingViewHolder
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.LoadingModel
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.data.model.tanyakandidat.Pertanyaan
import com.pantaubersama.app.ui.penpol.tanyakandidat.create.CreateTanyaKandidatActivity
import com.pantaubersama.app.utils.GlideApp
import com.pantaubersama.app.utils.extensions.inflate
import com.pantaubersama.app.utils.extensions.loadUrl
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_banner_container.*
import kotlinx.android.synthetic.main.item_tanya_kandidat.*
import kotlinx.android.synthetic.main.layout_action_post.*
import kotlinx.android.synthetic.main.layout_delete_confirmation_dialog.*
import kotlinx.android.synthetic.main.layout_option_dialog_tanya_kandidat.*
import kotlinx.android.synthetic.main.layout_tanya_kandidat_header.*

class TanyaKandidatAdapter() : BaseRecyclerAdapter<ItemModel, RecyclerView.ViewHolder>() {
//    private var data: MutableList<Pertanyaan> = ArrayList()
    var listener: TanyaKandidatAdapter.AdapterListener? = null

    override fun getItemViewType(position: Int): Int {
        return if (data[position] is BannerInfo) {
            VIEW_TYPE_BANNER
        } else if (data[position] is LoadingModel) {
            VIEW_TYPE_LOADING
        }
        else if ((data[position] as Pertanyaan).viewType != null) {
            (data[position] as Pertanyaan).viewType!!
        }
        else {
            VIEW_TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_LOADING -> LoadingViewHolder(parent.inflate(R.layout.item_loading))
            VIEW_TYPE_HEADER -> HeaderViewHolder(parent.inflate(R.layout.layout_tanya_kandidat_header))
            VIEW_TYPE_BANNER -> BannerViewHolder(parent.inflate(R.layout.item_banner_container))
            else -> TanyaKandidatViewHolder(parent.inflate(R.layout.item_tanya_kandidat))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? LoadingViewHolder)?.bind()
        (holder as? HeaderViewHolder)?.onBind()
        (holder as? BannerViewHolder)?.bind(data[position] as BannerInfo)
        (holder as? TanyaKandidatViewHolder)?.onBind(data[position] as Pertanyaan)
    }

    inner class TanyaKandidatViewHolder(
        override val containerView: View?
    ) : RecyclerView.ViewHolder(
        containerView!!), LayoutContainer {

        @SuppressLint("SetTextI18n")
        fun onBind(item: Pertanyaan?) {
            GlideApp
                .with(itemView.context)
                .load(item?.user?.avatar?.url)
                .placeholder(ContextCompat.getDrawable(itemView.context, R.drawable.ic_person))
                .into(user_avatar)
            tv_user_name.text = item?.user?.fullName
            question_time.text = item?.createdAt?.id
            upvote_count_text.text = item?.likeCount.toString()
            user_question.text = item?.body
            iv_options_button.setOnClickListener {
//                showOptionsDialog(itemView)
                listener?.onClickTanyaOption(item!!, adapterPosition)
            }
            iv_share_button.setOnClickListener {
                listener?.onClickShare(item)
            }
            if ((data[adapterPosition] as Pertanyaan).isliked != null) {
                if ((data[adapterPosition] as Pertanyaan).isliked!!) {
                    upvote_animation.progress = 1.0f
                } else {
                    upvote_animation.progress = 0.0f
                }
            }
            upvote_container.setOnClickListener {
                if (!item?.isliked!!) {
                    setUpvoted(item)
                    listener?.onClickUpvote(item.id, item.isliked, position)
                }
            }
        }

        private fun setUpvoted(item: Pertanyaan?) {
            val upVoted = item?.isliked
            item?.isliked = !item?.isliked!!
            val animator = ValueAnimator.ofFloat(0.0f, 1.0f).setDuration(1000)
            if (!upVoted!!) {
                val loveCount = item.likeCount!! + 1
                item.likeCount = loveCount
                animator.addUpdateListener { animation -> upvote_animation.progress = animation.animatedValue as Float
                    upvote_count_text.text = item.likeCount.toString()
                }
                animator.start()
            }
//            else {
//                val upVoteCount = item.likeCount!! - 1
//                item.likeCount = upVoteCount
//                upvote_count_text.text = item.likeCount.toString()
//                upvote_animation.progress = 0.0f
//            }
        }

    }

    inner class HeaderViewHolder(
        override val containerView: View?
    ) : RecyclerView.ViewHolder(containerView!!), LayoutContainer {
        fun onBind() {
            question_section.setOnClickListener {
                val intent = Intent(itemView.context, CreateTanyaKandidatActivity::class.java)
                itemView.context.startActivity(intent)
            }
        }
    }

    inner class BannerViewHolder(override val containerView: View)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: BannerInfo) {
            tv_banner_text.text = item.body
            iv_banner_image.loadUrl(item.headerImage?.url)
            rl_banner_container.setOnClickListener { listener?.onClickBanner(item) }
            iv_banner_close.setOnClickListener { removeBanner() }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun addHeader() {
        if (data.size != 0 && data[0] is BannerInfo) {
            addItem(Pertanyaan(viewType = VIEW_TYPE_HEADER), 1)
        } else {
            addItem(Pertanyaan(viewType = VIEW_TYPE_HEADER), 0)
        }
    }

//    fun addItem(question: Pertanyaan, position: Int) {
//        data.add(question)
//        notifyItemInserted(itemCount)
//    }

//    fun setData(question: MutableList<Pertanyaan>) {
//        data.clear()
//        data.addAll(question)
//        notifyDataSetChanged()
//    }
//
//    fun setLoading() {
//        data.add(Pertanyaan(viewType = VIEW_TYPE_LOADING))
//        notifyItemInserted(data.size - 1)
//    }
//
//    fun setLoaded() {
//        data.removeAt(data.size - 1)
//        notifyItemRemoved(data.size)
//    }
//
//    fun addData(questions: MutableList<Pertanyaan>) {
//        data.addAll(questions)
//        notifyItemRangeInserted(itemCount, questions.size)
//    }

    fun reverseVote(liked: Boolean?, position: Int?) {
        (data[position!!] as Pertanyaan).isliked = liked
        notifyItemChanged(position)
    }

    fun deleteItem(position: Int?) {
        data.removeAt(position!!)
        notifyItemRemoved(position)
    }

    fun addBanner(bannerInfo: BannerInfo) {
        addItem(bannerInfo, 0)
    }

    fun removeBanner() {
        if (data[0] is BannerInfo) {
            deleteItem(0)
        }
    }

    companion object {
        var VIEW_TYPE_LOADING = 0
        var VIEW_TYPE_HEADER = 1
        var VIEW_TYPE_ITEM = 2
        var VIEW_TYPE_BANNER = 3
    }

    interface AdapterListener {
        fun onClickBanner(bannerInfo: BannerInfo)
        fun onClickShare(item: Pertanyaan?)
        fun onClickUpvote(id: String?, isLiked: Boolean?, position: Int?)
        fun onClickDeleteItem(id: String?, position: Int?)
        fun onClickCopyUrl(id: String?)
        fun onClickLapor(id: String?)
        fun onClickTanyaOption(item: Pertanyaan, position: Int)
    }
}