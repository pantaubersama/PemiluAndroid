package com.pantaubersama.app.ui.profile.cluster.categery

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.base.viewholder.LoadingViewHolder
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.cluster.Category
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.extensions.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.category_item.*

class CategoriesAdapter : BaseRecyclerAdapter<ItemModel, RecyclerView.ViewHolder>() {
    var listener: CategoriesAdapter.Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == PantauConstants.ItemModel.CATEGORY_ITEM) {
            CategoriesViewHolder(parent.inflate(R.layout.category_item))
        } else {
            LoadingViewHolder(parent.inflate(R.layout.item_loading))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? CategoriesViewHolder)?.bind(data[position] as Category)
        (holder as? LoadingViewHolder)?.bind()
    }

    inner class CategoriesViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(category: Category) {
            category_name.text = category.name
            itemView.setOnClickListener {
                listener?.onClick(category)
            }
        }
    }

    interface Listener {
        fun onClick(category: Category)
    }
}