package com.pantaubersama.app.ui.penpol.kuis.result

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.utils.extensions.color
import com.pantaubersama.app.utils.extensions.inflate
import com.pantaubersama.app.utils.spannable
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_kuis_answer_key.*

class KuisAnswerKeyAdapter : RecyclerView.Adapter<KuisAnswerKeyAdapter.ViewHolder>() {

    var items: Int = 0
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_kuis_answer_key))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }

    class ViewHolder(override val containerView: View)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {

        init {
            tv_user_answer.text = spannable {
                +"Jawabanmu : "
                textColor(containerView.context.color(R.color.orange)) { +"Prabowo - Sandi" }
            }.toCharSequence()
        }
    }
}