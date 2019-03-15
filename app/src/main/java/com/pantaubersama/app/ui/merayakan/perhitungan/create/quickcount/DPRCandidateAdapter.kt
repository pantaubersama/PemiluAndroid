package com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.data.model.tps.candidate.Candidate
import com.pantaubersama.app.utils.RxSchedulers
import com.pantaubersama.app.utils.UndoRedoTools
import com.pantaubersama.app.utils.extensions.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.kandidat_person_item.*
import java.text.FieldPosition

class DPRCandidateAdapter(private val rxSchedulers: RxSchedulers) : BaseRecyclerAdapter() {
    var listener: Listener? = null
    private var undoRedoToolses: MutableList<UndoRedoTools> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CandidatesViewHolder(parent.inflate(R.layout.kandidat_person_item))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CandidatesViewHolder).bind(data[position] as Candidate)
    }

    fun undoCandidate(undoPosition: Int) {
        undoRedoToolses[undoPosition].undo()
    }

    inner class CandidatesViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: Candidate) {
            candidate_name.text = "${item.serialNumber}. ${item.name}"
            candidate_count_field.setText(item.candidateCount.toString())
            candidate_inc_button.setOnClickListener {
                val count = candidate_count_field.text.toString().toInt()
                candidate_count_field.setText(count.plus(1).toString())
            }
            RxTextView.textChanges(candidate_count_field)
                .filter {
                    it.isNotEmpty()
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
                    listener?.onCandidateCountChange(item.id, item.candidateCount, adapterPosition)
                }
                .doOnError {
                    it.printStackTrace()
                }
                .subscribe()
            undoRedoToolses.add(adapterPosition, UndoRedoTools(candidate_count_field))
        }
    }

    interface Listener {
        fun onCandidateCountChange(candidateId: Int, totalCount: Int, candidateUndoPosition: Int)
    }
}