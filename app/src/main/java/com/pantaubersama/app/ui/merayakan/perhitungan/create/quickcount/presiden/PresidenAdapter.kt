package com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.presiden

import android.text.InputFilter
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.data.model.tps.realcount.Candidate
import com.pantaubersama.app.data.model.tps.realcount.RealCount
import com.pantaubersama.app.utils.MinMaxInputFilter
import com.pantaubersama.app.utils.RxSchedulers
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.UndoRedoTools
import com.pantaubersama.app.utils.extensions.inflate
import io.reactivex.Observable
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.presiden_item_layout.*

class PresidenAdapter(private val rxSchedulers: RxSchedulers, private val isIncrementEnable: Boolean) : BaseRecyclerAdapter() {
    val undoRedoToolses: MutableList<UndoRedoTools> = ArrayList()
    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PresidenViewHolder(parent.inflate(R.layout.presiden_item_layout))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PresidenViewHolder).bind(data[position] as Candidate)
    }

    fun setVote(realCount: RealCount) {
        (data[0] as Candidate).totalVote = realCount.candidates[0].totalVote
        (data[1] as Candidate).totalVote = realCount.candidates[1].totalVote
        notifyDataSetChanged()
    }

    fun undoPresiden(position: Int) {
        undoRedoToolses[position].undo()
    }

    inner class PresidenViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(candidate: Candidate) {
            candidate_count_field.filters = arrayOf<InputFilter>(MinMaxInputFilter("0", "500"))
            when (adapterPosition) {
                0 -> paslon_image.setImageResource(R.drawable.ava_calon_1)
                1 -> paslon_image.setImageResource(R.drawable.ava_calon_2)
            }
            candidate_count_field.setText(candidate.totalVote.toString())
            if (!isIncrementEnable) {
                candidate_count_field.isEnabled = false
            }
            candidate_inc_button.setOnClickListener {
                if (isIncrementEnable) {
                    val count = if (candidate_count_field.text.isNotEmpty()) {
                        candidate_count_field.text.toString().toLong()
                    } else {
                        0
                    }
                    candidate_count_field.setText(count.plus(1).toString())
                } else {
                    ToastUtil.show(itemView.context, "Perhitungan kamu telah dikirim dan tidak dapat diubah")
                }
            }
            RxTextView.textChanges(candidate_count_field)
                .skipInitialValue()
                .flatMap {
                    if (it.isEmpty()) {
                        Observable.just("0")
                    } else {
                        Observable.just(it)
                    }
                }
                .map {
                    it.toString()
                }
                .map {
                    it.toLong()
                }
                .subscribeOn(rxSchedulers.io())
                .observeOn(rxSchedulers.mainThread())
                .doOnNext {
                    candidate.totalVote = it
                    listener?.saveRealCount(adapterPosition)
                }
                .doOnError {
                    it.printStackTrace()
                }
                .subscribe()
            undoRedoToolses.add(adapterPosition, UndoRedoTools(candidate_count_field))
        }
    }

    interface Listener {
        fun saveRealCount(adapterPosition: Int)
    }
}