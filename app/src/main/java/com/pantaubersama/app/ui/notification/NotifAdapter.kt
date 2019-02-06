package com.pantaubersama.app.ui.notification

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.utils.extensions.inflate

class NotifAdapter : BaseRecyclerAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // return view berdasarkan tipe viewType, misal badge, quiz, atau pertanyaan
        // badge menggunakan item_notif_badge
        // quiz baru menggunakan item_notif_new_quiz
        // badge karena pertanyaan menggunakan item_notif_question
        return BadgeHolder(parent.inflate(R.layout.item_notif_badge))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    inner class BadgeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // sample holder
    }
}