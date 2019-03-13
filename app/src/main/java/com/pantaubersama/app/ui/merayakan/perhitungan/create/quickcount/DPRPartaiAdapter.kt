package com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.tps.candidate.CandidateData
import com.pantaubersama.app.utils.RxSchedulers
import com.pantaubersama.app.utils.extensions.inflate
import com.pantaubersama.app.utils.extensions.loadUrl
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.kandidat_partai_item.*
import timber.log.Timber

class DPRPartaiAdapter(private val rxSchedulers: RxSchedulers) : BaseRecyclerAdapter() {
    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DPRViewHolder(parent.inflate(R.layout.kandidat_partai_item))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as DPRViewHolder).bind(data[position] as CandidateData)
    }

    fun updateData(totalVote: Int, partyPosition: Int) {
        (data[partyPosition] as CandidateData).totalCount = totalVote
        notifyItemChanged(partyPosition)
    }

    inner class DPRViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: CandidateData) {
            party_thumbnail.loadUrl(item.logo)
            party_name.text = item.name
            party_number.text = "No. Urut ${item.serialNumber}"
            if (item.totalCount != 0) {
                party_count_field.setText(item.totalCount.toString())
            }
            party_inc_button.setOnClickListener {
                val count = party_count_field.text.toString().toInt()
                party_count_field.setText(count.plus(1).toString())
            }
            RxTextView.textChanges(party_count_field)
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
                    listener?.onPartyCountChange(item.id, it, adapterPosition)
                }
                .doOnError {
                    it.printStackTrace()
                }
                .subscribe()
            val adapter = DPRCandidateAdapter()
            adapter.listener = object : DPRCandidateAdapter.Listener {
            }
            candidates_container.layoutManager = LinearLayoutManager(itemView.context)
            candidates_container.adapter = adapter
            adapter.setDatas(item.candidates as MutableList<ItemModel>)
        }
    }

    interface Listener {
        fun onPartyCountChange(partyId: Int, partyCount: Int, partyPosition: Int)

    }
}