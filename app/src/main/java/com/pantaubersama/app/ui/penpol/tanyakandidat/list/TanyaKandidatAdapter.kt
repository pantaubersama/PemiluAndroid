package com.pantaubersama.app.ui.penpol.tanyakandidat.list

import android.content.Context
import android.view.View
import com.pantaubersama.app.R
import com.pantaubersama.app.base.adapter.BaseAdapter
import com.pantaubersama.app.base.listener.OnItemClickListener
import com.pantaubersama.app.base.listener.OnItemLongClickListener
import com.pantaubersama.app.base.viewholder.BaseViewHolder
import com.pantaubersama.app.data.model.tanyakandidat.TanyaKandidat
import kotlinx.android.synthetic.main.item_tanya_kandidat.view.*

class TanyaKandidatAdapter(context: Context) : BaseAdapter<TanyaKandidat, TanyaKandidatAdapter.TanyaKandidatViewHolder>(context){

    class TanyaKandidatViewHolder(
        view: View,
        clickListener: OnItemClickListener?,
        longClickListener: OnItemLongClickListener?): BaseViewHolder<TanyaKandidat>(
        view, clickListener, longClickListener){
        override fun bind(item: TanyaKandidat) {
            itemView.user_name.text = item.user?.name
            itemView.question_time.text = item.createdAt
            itemView.upvote_count_text.text = item.upvoteCount.toString()
            itemView.user_question.text = item.question
        }
    }

    override fun initViewHolder(view: View, viewType: Int): TanyaKandidatViewHolder {
        return TanyaKandidatViewHolder(view, itemClickListener, itemLongClickListener)
    }

    override fun setItemView(viewType: Int): Int {
        return R.layout.item_tanya_kandidat
    }
}