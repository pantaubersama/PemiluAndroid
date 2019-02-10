package com.pantaubersama.app.ui.menguji

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.data.model.debat.DebatItem
import com.pantaubersama.app.utils.extensions.inflate

class CarouselDebatAdapter: RecyclerView.Adapter<CarouselDebatViewHolder>() {

    var debatItems: List<DebatItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = debatItems.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselDebatViewHolder {
        return CarouselDebatViewHolder(parent.inflate(R.layout.item_carousel_debat))
    }

    override fun onBindViewHolder(holder: CarouselDebatViewHolder, position: Int) {
        holder.bind(debatItems[position])
    }
}