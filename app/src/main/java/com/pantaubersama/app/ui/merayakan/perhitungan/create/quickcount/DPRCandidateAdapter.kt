package com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.data.model.tps.candidate.Candidate
import com.pantaubersama.app.utils.RxSchedulers
import com.pantaubersama.app.utils.extensions.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.kandidat_person_item.*

class DPRCandidateAdapter(private val rxSchedulers: RxSchedulers) : BaseRecyclerAdapter() {
    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CandidatesViewHolder(parent.inflate(R.layout.kandidat_person_item))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CandidatesViewHolder).bind(data[position] as Candidate)
    }

//    fun updateCandidateData(totalVote: Int, candidatePosition: Int) {
//        (data[candidatePosition] as Candidate).totalVote = totalVote
//        notifyItemChanged(candidatePosition)
//    }

    inner class CandidatesViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: Candidate) {
            candidate_name.text = "${item.serialNumber}. ${item.name}"
            candidate_count_field.setText(item.candidateCount.toString())
            candidate_inc_button.setOnClickListener {
                val count = candidate_count_field.text.toString().toInt()
                candidate_count_field.setText(count.plus(1).toString())
            }
            RxTextView.textChanges(candidate_count_field)
                .skipInitialValue()
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
                    listener?.onCandidateCountChange(item.id, item.candidateCount)
                }
                .doOnError {
                    it.printStackTrace()
                }
                .subscribe()
        }
    }

    interface Listener {
        fun onCandidateCountChange(candidateId: Int, totalCount: Int)
    }
}