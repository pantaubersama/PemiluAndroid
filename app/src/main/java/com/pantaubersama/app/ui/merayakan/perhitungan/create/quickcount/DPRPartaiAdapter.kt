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
import com.pantaubersama.app.data.model.tps.RealCount
import com.pantaubersama.app.data.model.tps.candidate.CandidateData
import com.pantaubersama.app.utils.RxSchedulers
import com.pantaubersama.app.utils.extensions.inflate
import com.pantaubersama.app.utils.extensions.loadUrl
import io.reactivex.Observable
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.kandidat_partai_item.*
import java.util.concurrent.TimeUnit

class DPRPartaiAdapter(private val rxSchedulers: RxSchedulers) : BaseRecyclerAdapter() {
    var listener: Listener? = null
    var adapters: MutableList<DPRCandidateAdapter> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DPRViewHolder(parent.inflate(R.layout.kandidat_partai_item))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as DPRViewHolder).bind(data[position] as CandidateData)
    }

    fun updateData(realCount: RealCount) {
        realCount.parties.forEachIndexed { i, partyFromDB ->
            data.forEachIndexed { j, partyFromAdapter ->
                if (partyFromDB.id == (partyFromAdapter as CandidateData).id) {
                    val candidateCounts: MutableList<Int> = ArrayList()
                    realCount.candidates.forEachIndexed { k, candidateDb ->
                        partyFromAdapter.candidates.forEachIndexed { l, candidateData ->
                            if (candidateData.id == candidateDb.id) {
                                candidateCounts.add(candidateData.candidateCount)
                                candidateData.candidateCount = candidateDb.totalVote
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

    inner class DPRViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: CandidateData) {
            party_thumbnail.loadUrl(item.logo)
            party_name.text = item.name
            party_number.text = "No. Urut ${item.serialNumber}"
            party_count_field.setText(item.partyCount.toString())
            party_votes_count.text = item.totalCount.toString()
            party_inc_button.setOnClickListener {
                val count = party_count_field.text.toString().toInt()
                party_count_field.setText(count.plus(1).toString())
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
                    it.toInt()
                }
                .subscribeOn(rxSchedulers.io())
                .observeOn(rxSchedulers.mainThread())
                .doOnNext {
                    val candidateCounts: MutableList<Int> = ArrayList()
                    item.candidates.forEachIndexed { index, candidate ->
                        candidateCounts.add(candidate.candidateCount)
                    }
                    item.partyCount = it
                    item.allCandidateCount = candidateCounts.sum()
                    val allCount = item.partyCount + item.allCandidateCount
                    item.totalCount = allCount
                    party_votes_count.text = item.totalCount.toString()
                }
                .doOnError {
                    it.printStackTrace()
                }
                .subscribe()

            RxTextView.textChanges(party_votes_count)
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
                .debounce(1000, TimeUnit.MILLISECONDS)
                .doOnNext {
                    listener?.saveRealCount(data as MutableList<CandidateData>)
                }
                .doOnError {
                    it.printStackTrace()
                }
                .subscribe()
            adapters.add(adapterPosition, DPRCandidateAdapter(rxSchedulers))
            adapters[adapterPosition].listener = object : DPRCandidateAdapter.Listener {
                override fun onCandidateCountChange(candidateId: Int, totalCount: Int) {
                    val candidateCounts: MutableList<Int> = ArrayList()
                    item.candidates.forEachIndexed { index, candidate ->
                        candidateCounts.add(candidate.candidateCount)
                    }
                    val allCount = party_count_field.text.toString().toInt() + candidateCounts.sum()
                    party_votes_count.text = allCount.toString()
                }
            }
            candidates_container.layoutManager = LinearLayoutManager(itemView.context)
            candidates_container.adapter = adapters[adapterPosition]
            (candidates_container.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            adapters[adapterPosition].setDatas(item.candidates as MutableList<ItemModel>)
        }
    }

    interface Listener {
        fun saveRealCount(items: MutableList<CandidateData>)
    }
}