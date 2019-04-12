package com.pantaubersama.app.ui.notification

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.CommonActivity
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.notification.NotificationWhole
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.penpol.kuis.detail.DetailKuisActivity
import com.pantaubersama.app.ui.penpol.tanyakandidat.detail.DetailTanyaKandidatActivity
import com.pantaubersama.app.ui.profile.setting.badge.detail.DetailBadgeActivity
import com.pantaubersama.app.utils.ChromeTabUtil
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.activity_notif.*
import kotlinx.android.synthetic.main.catatan_tab_item.*
import timber.log.Timber
import javax.inject.Inject

class NotifActivity : CommonActivity() {
    private lateinit var tabAdapter: NotifActivity.NotifTabAdapter
    private var selectedTab: String = ""
    private var all: NotifChildFragment = NotifChildFragment.newInstance("Semua Notifikasi")
    private var event: NotifChildFragment = NotifChildFragment.newInstance("Event")

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun setLayout(): Int {
        return R.layout.activity_notif
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, getString(R.string.title_notification), R.color.white, 4f)
        setupTabRecyclerview()
    }

    private fun setupTabRecyclerview() {
        notif_recycler_view_menu.layoutManager =
                LinearLayoutManager(this@NotifActivity, LinearLayoutManager.HORIZONTAL, false)
        tabAdapter = NotifTabAdapter()
        notif_recycler_view_menu.adapter = tabAdapter
        tabAdapter.setSelected(0)
    }

    inner class NotifTabAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private val tabs: MutableList<String> = ArrayList()


        init {
            tabs.add("Semua Notifikasi")
//            tabs.add("Wordstadium")
            tabs.add("Event")
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return NotifTabViewHolder(parent.inflate(R.layout.catatan_tab_item))
        }

        override fun getItemCount(): Int {
            return tabs.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as NotifTabViewHolder).bind(tabs[position], position)
        }

        fun setSelected(i: Int) {
            selectedTab = tabs[i]
        }

        inner class NotifTabViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
            fun bind(data: String, position: Int) {
                tab_text.text = data
                if (data == selectedTab) {
                    item.setBackgroundResource(R.drawable.rounded_tab_selected)
                    tab_text.setTextColor(ContextCompat.getColor(this@NotifActivity, R.color.white))
                } else {
                    item.setBackgroundResource(R.drawable.rounded_tab_notselected)
                    tab_text.setTextColor(ContextCompat.getColor(this@NotifActivity, R.color.gray_5))
                    when (data) {
                        "Semua Notifikasi" -> supportFragmentManager.beginTransaction()
                            .replace(R.id.container, all).commit()
                        "Event" -> supportFragmentManager.beginTransaction()
                            .replace(R.id.container, event).commit()
                    }
                }
                itemView.setOnClickListener {
                    selectedTab = data
                    notifyDataSetChanged()
                }
            }
        }
    }
}
