package com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.data.model.tps.candidate.Candidate
import com.pantaubersama.app.utils.extensions.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.kandidat_person_item.*

class DPRCandidateAdapter : BaseRecyclerAdapter() {
    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CandidatesViewHolder(parent.inflate(R.layout.kandidat_person_item))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CandidatesViewHolder).bind(data[position] as Candidate)
    }

    inner class CandidatesViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: Candidate) {
            candidate_name.text = "${item.serialNumber}. ${item.name}"
            candidate_inc_button.setOnClickListener {
                val count = candidate_count_field.text.toString().toInt()
                candidate_count_field.setText(count.plus(1).toString())
            }
        }
    }

    interface Listener
}