package com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.partai.PoliticalParty
import com.pantaubersama.app.utils.extensions.inflate
import com.pantaubersama.app.utils.extensions.loadUrl
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.kandidat_partai_item.*

class DPRPartaiAdapter : BaseRecyclerAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DPRViewHolder(parent.inflate(R.layout.kandidat_partai_item))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as DPRViewHolder).bind(data[position] as PoliticalParty)
    }

    inner class DPRViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: PoliticalParty) {
            party_thumbnail.loadUrl(item.image?.url)
            party_name.text = item.name
            party_number.text = "No. Urut " + item.number.toString()
            party_inc_button.setOnClickListener {
                val count = party_count_field.text.toString().toInt()
                party_count_field.setText(count.plus(1).toString())
            }
            val adapter = DPRCandidateAdapter()
            adapter.listener = object : DPRCandidateAdapter.Listener {

            }
            candidates_container.layoutManager = LinearLayoutManager(itemView.context)
            candidates_container.adapter = adapter
            adapter.setDatas(item.members as MutableList<ItemModel>)
        }
    }
}