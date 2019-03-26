package com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.data.model.tps.realcount.RealCount
import com.pantaubersama.app.data.model.tps.candidate.Candidate
import com.pantaubersama.app.utils.RxSchedulers
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.UndoRedoTools
import com.pantaubersama.app.utils.extensions.inflate
import io.reactivex.Observable
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.kandidat_person_item.*

class DPRCandidateAdapter(private val rxSchedulers: RxSchedulers, private var isIncrementEnable: Boolean) : BaseRecyclerAdapter() {
    var listener: Listener? = null
    var undoRedoToolses: MutableList<UndoRedoTools> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CandidatesViewHolder(parent.inflate(R.layout.kandidat_person_item))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CandidatesViewHolder).bind(data[position] as Candidate)
    }

    fun undoCandidate(undoPosition: Int) {
        undoRedoToolses[undoPosition].undo()
    }

    fun updateData(realCount: RealCount) {
        realCount.candidates.forEachIndexed { i, candidateDb ->
            data.forEachIndexed { j, candidateData ->
                if ((candidateData as Candidate).id == candidateDb.id || (candidateData as Candidate).id == candidateDb.actorId) {
                    candidateData.candidateCount = candidateDb.totalVote
                }
            }
        }
        notifyDataSetChanged()
    }

    inner class CandidatesViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: Candidate) {
            candidate_name.text = "${item.serialNumber}. ${item.name}"
            candidate_count_field.setText(item.candidateCount.toString())
            if (!isIncrementEnable) {
                candidate_count_field.isEnabled = false
            }
            candidate_inc_button.setOnClickListener {
                if (isIncrementEnable) {
                    val count = if (candidate_count_field.text.isNotEmpty()) {
                        candidate_count_field.text.toString().toInt()
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
                    it.toInt()
                }
                .subscribeOn(rxSchedulers.io())
                .observeOn(rxSchedulers.mainThread())
                .doOnNext {
                    item.candidateCount = it
                    listener?.onCandidateCountChange(adapterPosition)
                }
                .doOnError {
                    it.printStackTrace()
                }
                .subscribe()
            undoRedoToolses.add(adapterPosition, UndoRedoTools(candidate_count_field))
        }
    }

    interface Listener {
        fun onCandidateCountChange(candidateUndoPosition: Int)
    }
}