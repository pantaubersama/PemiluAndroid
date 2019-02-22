package com.pantaubersama.app.ui.penpol.tanyakandidat.create

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.data.model.tanyakandidat.Pertanyaan
import com.pantaubersama.app.utils.extensions.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.available_question_item.*

class QuestionsAvailableAdapter : BaseRecyclerAdapter() {
    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return QuestionsAvailableViewHolder(parent.inflate(R.layout.available_question_item))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as QuestionsAvailableViewHolder).bind(data[position] as Pertanyaan)
    }

    inner class QuestionsAvailableViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: Pertanyaan) {
            question.text = item.body
            itemView.setOnClickListener {
                listener?.onClickQuestion(item)
            }
        }
    }

    interface Listener {
        fun onClickQuestion(item: Pertanyaan)
    }
}