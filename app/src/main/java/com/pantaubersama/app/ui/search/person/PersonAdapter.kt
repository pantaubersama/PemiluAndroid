package com.pantaubersama.app.ui.search.person

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.base.viewholder.LoadingViewHolder
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.utils.PantauConstants.ItemModel.TYPE_USER
import com.pantaubersama.app.utils.extensions.inflate
import com.pantaubersama.app.utils.extensions.loadUrl
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.person_item.*

class PersonAdapter : BaseRecyclerAdapter() {
    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_USER -> PersonViewHolder(parent.inflate(R.layout.person_item))
            else -> LoadingViewHolder(parent.inflate(R.layout.item_loading))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? PersonViewHolder)?.bind(data[position] as Profile)
        (holder as? LoadingViewHolder)?.bind()
    }

    inner class PersonViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(profile: Profile) {
            person_avatar.loadUrl(profile.avatar?.thumbnail?.url)
            person_name.text = profile.fullName
            person_username.text = profile.username
            itemView.setOnClickListener {
                listener?.onClickItem(profile)
            }
        }
    }

    interface Listener {
        fun onClickItem(profile: Profile)
    }
}