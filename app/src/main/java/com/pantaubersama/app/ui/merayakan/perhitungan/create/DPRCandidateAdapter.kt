package com.pantaubersama.app.ui.merayakan.perhitungan.create

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.data.model.kandidat.CandidateData
import com.pantaubersama.app.utils.extensions.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.kandidat_person_item.*

class DPRCandidateAdapter : BaseRecyclerAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CandidatesViewHolder(parent.inflate(R.layout.kandidat_person_item))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CandidatesViewHolder).bind(data[position] as CandidateData)
    }

    inner class CandidatesViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: CandidateData) {
            candidate_name.text = item.number.toString() + ". " + item.name
        }
    }
}