package com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.jakewharton.rxbinding2.widget.RxTextView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.tps.realcount.RealCount
import com.pantaubersama.app.data.model.tps.candidate.CandidateData
import com.pantaubersama.app.utils.RxSchedulers
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.UndoRedoTools
import com.pantaubersama.app.utils.extensions.inflate
import com.pantaubersama.app.utils.extensions.loadUrl
import io.reactivex.Observable
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.kandidat_partai_item.*
import java.util.concurrent.TimeUnit

class DPRPartaiAdapter(private val rxSchedulers: RxSchedulers, private var isIncrementEnable: Boolean) : BaseRecyclerAdapter() {
    var listener: Listener? = null
    var adapters: MutableList<DPRCandidateAdapter> = ArrayList()
    var undoRedoToolses: MutableList<UndoRedoTools> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DPRViewHolder(parent.inflate(R.layout.kandidat_partai_item))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as DPRViewHolder).bind(data[position] as CandidateData)
    }

    fun updateData(realCount: RealCount) {
        realCount.parties.forEachIndexed { i, partyFromDB ->
            data.forEachIndexed { j, partyFromAdapter ->
                if ((partyFromAdapter as CandidateData).id == partyFromDB.id || (partyFromAdapter as CandidateData).id == partyFromDB.actorId) {
                    val candidateCounts: MutableList<Long> = ArrayList()
                    realCount.candidates.forEachIndexed { k, candidateDb ->
                        partyFromAdapter.candidates.forEachIndexed { l, candidateData ->
                            if (candidateData.id == candidateDb.id || candidateData.id == candidateDb.actorId) {
                                candidateData.candidateCount = candidateDb.totalVote
                                candidateCounts.add(candidateData.candidateCount)
                            }
                        }
                    }
                    partyFromAdapter.partyCount = partyFromDB.totalVote - candidateCounts.sum()
                    partyFromAdapter.totalCount = partyFromDB.totalVote
                }
            }
        }
        notifyDataSetChanged()
    }

    fun undoParty(undoPosition: Int) {
        undoRedoToolses[undoPosition].undo()
    }

    fun undoCandidate(partyPosition: Int, undoPosition: Int) {
        adapters.get(partyPosition).undoCandidate(undoPosition)
    }

    inner class DPRViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: CandidateData) {
            party_thumbnail.loadUrl(item.logo.url, R.drawable.ic_avatar_placeholder)
            party_name.text = item.name
            party_number.text = "No. Urut ${item.serialNumber}"
            party_count_field.setText(item.partyCount.toString())
            if (!isIncrementEnable) {
                party_count_field.isEnabled = false
            }
            party_votes_count.text = item.totalCount.toString()
            party_inc_button.setOnClickListener {
                if (isIncrementEnable) {
                    val count = if (party_count_field.text.isNotEmpty()) {
                        party_count_field.text.toString().toLong()
                    } else {
                        0
                    }
                    party_count_field.setText(count.plus(1).toString())
                } else {
                    ToastUtil.show(itemView.context, "Perhitungan kamu telah dikirim dan tidak dapat diubah")
                }
            }
            RxTextView.textChanges(party_count_field)
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
                    val candidateCounts: MutableList<Long> = ArrayList()
                    item.candidates.forEachIndexed { index, candidate ->
                        candidateCounts.add(candidate.candidateCount)
                    }
                    item.partyCount = it
                    item.allCandidateCount = candidateCounts.sum()
                    item.totalCount = item.partyCount + item.allCandidateCount
                    party_votes_count.text = item.totalCount.toString()
                }
                .doOnError {
                    it.printStackTrace()
                }
                .subscribe()

            RxTextView.textChanges(party_votes_count)
                .skipInitialValue()
                .flatMap {
                    if (it.isEmpty()) {
                        Observable.just("0")
                    } else {
                        Observable.just(it)
                    }
                }
                .filter {
                    it.isNotEmpty()
                }
                .map {
                    it.toString()
                }
                .map {
                    it.toLong()
                }
                .subscribeOn(rxSchedulers.io())
                .observeOn(rxSchedulers.mainThread())
                .debounce(1000, TimeUnit.MILLISECONDS)
                .doOnNext {
                    listener?.saveRealCount(data as MutableList<CandidateData>, adapterPosition)
                }
                .doOnError {
                    it.printStackTrace()
                }
                .subscribe()
            undoRedoToolses.add(adapterPosition, UndoRedoTools(party_count_field))
            adapters.add(adapterPosition, DPRCandidateAdapter(rxSchedulers, isIncrementEnable))
            adapters[adapterPosition].listener = object : DPRCandidateAdapter.Listener {
                override fun onCandidateCountChange(candidateUndoPosition: Int) {
                    listener?.onCandidateChanged(adapterPosition, candidateUndoPosition)
                    val candidateCounts: MutableList<Long> = ArrayList()
                    item.candidates.forEach { candidate ->
                        candidateCounts.add(candidate.candidateCount)
                    }
                    val allCount = if (party_count_field.text.isNotEmpty()) {
                        party_count_field.text.toString().toLong()
                    } else {
                        0
                    } + candidateCounts.sum()
                    item.allCandidateCount = candidateCounts.sum()
                    item.totalCount = allCount
                    party_votes_count.text = item.totalCount.toString()
                }
            }
            candidates_container.layoutManager = LinearLayoutManager(itemView.context)
            candidates_container.adapter = adapters[adapterPosition]
            (candidates_container.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            adapters[adapterPosition].setDatas(item.candidates as MutableList<ItemModel>)
        }
    }

    interface Listener {
        fun saveRealCount(items: MutableList<CandidateData>, selectedPartyPosition: Int)
        fun onCandidateChanged(partyPosition: Int, candidateUndoPosition: Int)
    }
}