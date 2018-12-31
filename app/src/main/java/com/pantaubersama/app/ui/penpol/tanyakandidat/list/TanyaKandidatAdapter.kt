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
import com.pantaubersama.app.data.model.tanyakandidat.Pertanyaan
import com.pantaubersama.app.ui.penpol.tanyakandidat.create.CreateTanyaKandidatActivity
import com.pantaubersama.app.utils.GlideApp
import com.pantaubersama.app.utils.extensions.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_tanya_kandidat.*
import kotlinx.android.synthetic.main.layout_action_post.*
import kotlinx.android.synthetic.main.layout_option_dialog_tanya_kandidat.*
import kotlinx.android.synthetic.main.layout_tanya_kandidat_header.*

class TanyaKandidatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var data: MutableList<Pertanyaan> = ArrayList()
    var listener: TanyaKandidatAdapter.AdapterListener? = null
    var VIEW_TYPE_LOADING = 0
    var VIEW_TYPE_HEADER = 1
    var VIEW_TYPE_ITEM = 2

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
            tv_user_name.text = item?.user?.firstName + item?.user?.lastName
            question_time.text = item?.createdAt?.id
            upvote_count_text.text = item?.likeCount.toString()
            user_question.text = item?.body
            iv_options_button.setOnClickListener {
                showOptionsDialog(itemView)
            }
            upvote_button.setOnClickListener {
                setUpvoted(item)
            }
            iv_share_button.setOnClickListener {
                listener?.onClickShare(item)
            }
        }

        private fun setUpvoted(item: Pertanyaan?) {
            val upVoted = item?.isliked
            item?.isliked = !item?.isliked!!
            val animator = ValueAnimator.ofFloat(0.0f, 1.0f).setDuration(1000)
            if (upVoted!!) {
                val upVoteCount = item.likeCount!! - 1
                item.likeCount = upVoteCount
                upvote_count_text.text = item.likeCount.toString()
                upvote_button.progress = 0.0f
            } else {
                val loveCount = item.likeCount!! + 1
                item.likeCount = loveCount
                animator.addUpdateListener {
                    animation -> upvote_button.progress = animation.animatedValue as Float
                    upvote_count_text.text = item.likeCount.toString()
                }
                animator.start()
            }
        }

        private fun showOptionsDialog(itemView: View?) {
            val dialog = Dialog(itemView?.context!!)
            dialog.setContentView(R.layout.layout_option_dialog_tanya_kandidat)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setOnKeyListener { _, i, _ ->
                if (i == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss()
                    true
                } else {
                    false
                }
            }
            dialog.setCanceledOnTouchOutside(true)
            val lp = WindowManager.LayoutParams()
            val window = dialog.window
            lp.copyFrom(window?.attributes)
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            window?.attributes = lp
            lp.gravity = Gravity.BOTTOM
            window?.attributes = lp
            dialog.delete_tanya_kandidat_item_action?.setOnClickListener {
                // delete
            }
            dialog.copy_url_tanya_kandidat_action?.setOnClickListener {
                // copy url
            }
            dialog.share_tanya_kandidat_action?.setOnClickListener {
                // share
            }
            dialog.report_tanya_kandidat_action?.setOnClickListener {
                // lapor
            }
            dialog.show()
        }
    }

    inner class LoadingViewHolder(
        override val containerView: View?
    ) : RecyclerView.ViewHolder(containerView!!), LayoutContainer {
        fun onBind() {
            // no need to do
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

    override fun getItemViewType(position: Int): Int {
        return when (data[position].viewType) {
            VIEW_TYPE_LOADING -> VIEW_TYPE_LOADING
            VIEW_TYPE_HEADER -> VIEW_TYPE_HEADER
            else -> VIEW_TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_LOADING -> LoadingViewHolder(parent.inflate(R.layout.layout_loading))
            VIEW_TYPE_HEADER -> HeaderViewHolder(parent.inflate(R.layout.layout_tanya_kandidat_header))
            else -> TanyaKandidatViewHolder(parent.inflate(R.layout.item_tanya_kandidat))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LoadingViewHolder -> holder.onBind()
            is HeaderViewHolder -> holder.onBind()
            is TanyaKandidatViewHolder -> holder.onBind(data[position])
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun addHeader() {
        data.add(0, Pertanyaan(viewType = VIEW_TYPE_HEADER))
        notifyItemInserted(0)
    }

    fun addItem(question: Pertanyaan, position: Int) {
        data.add(question)
        notifyItemInserted(itemCount)
    }

    fun setData(question: MutableList<Pertanyaan>) {
        data.clear()
        data.addAll(question)
        notifyDataSetChanged()
    }

    fun setLoading() {
        data.add(Pertanyaan(viewType = VIEW_TYPE_LOADING))
        notifyItemInserted(data.size - 1)
    }

    fun setLoaded() {
        data.removeAt(data.size - 1)
        notifyItemRemoved(data.size)
    }

    fun addData(questions: MutableList<Pertanyaan>) {
        data.addAll(questions)
        notifyItemRangeInserted(itemCount, questions.size)
    }

    interface AdapterListener {
        fun onClickShare(item: Pertanyaan?)
    }
}