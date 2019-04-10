package com.pantaubersama.app.ui.wordstadium.challenge.direct

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.data.model.wordstadium.LawanDebat
import com.pantaubersama.app.utils.extensions.inflate
import com.pantaubersama.app.utils.extensions.loadUrl
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.person_item.*

class LawanDebatAdapter : BaseRecyclerAdapter() {
    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PersonViewHolder(parent.inflate(R.layout.person_item))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? PersonViewHolder)?.bind(data[position] as LawanDebat)
    }

    inner class PersonViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(lawanDebat: LawanDebat) {
            if (lawanDebat.name == null) {
                person_avatar.loadUrl(lawanDebat.avatar.url)
                person_name.text = lawanDebat.fullName
                person_username.text = lawanDebat.username
            } else {
                person_avatar.loadUrl(lawanDebat.profileImageUrl)
                person_name.text = lawanDebat.name
                person_username.text = lawanDebat.screenName
            }
            itemView.setOnClickListener {
                listener?.onClickItem(lawanDebat)
            }
        }
    }

    interface Listener {
        fun onClickItem(lawanDebat: LawanDebat)
    }
}