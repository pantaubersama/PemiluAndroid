package com.pantaubersama.app.ui.notification

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.CommonActivity
import com.pantaubersama.app.R
import com.pantaubersama.app.utils.extensions.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.activity_notif.*
import kotlinx.android.synthetic.main.catatan_tab_item.*

class NotifActivity : CommonActivity() {
    private lateinit var tabAdapter: NotifActivity.NotifTabAdapter
    private var selectedTab: String? = null
    private lateinit var all: NotifChildFragment
    private lateinit var event: NotifChildFragment

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun setLayout(): Int {
        return R.layout.activity_notif
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        savedInstanceState?.getString("selected_tab")?.let {
            selectedTab = it
        }
        setupToolbar(true, getString(R.string.title_notification), R.color.white, 4f)
        initFragment()
//        setupTabRecyclerview()
        // hide notifikasi event
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, all).commit()
//        tabAdapter.setSelected(selectedTab ?: "Semua Notifikasi")
    }

    private fun initFragment() {
        all = NotifChildFragment.newInstance("Semua Notifikasi")
        event = NotifChildFragment.newInstance("Event")
    }

    private fun setupTabRecyclerview() {
        notif_recycler_view_menu.layoutManager =
                LinearLayoutManager(this@NotifActivity, LinearLayoutManager.HORIZONTAL, false)
        tabAdapter = NotifTabAdapter()
        notif_recycler_view_menu.adapter = tabAdapter
    }

    inner class NotifTabAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        val tabs: MutableList<String> = ArrayList()

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

        fun setSelected(selected: String) {
            selectedTab = selected
            notifyDataSetChanged()
        }

        inner class NotifTabViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
            fun bind(data: String, position: Int) {
                tab_text.text = data
                if (data == selectedTab) {
                    item.setBackgroundResource(R.drawable.rounded_tab_selected)
                    tab_text.setTextColor(ContextCompat.getColor(this@NotifActivity, R.color.white))
                    when (data) {
                        "Semua Notifikasi" -> supportFragmentManager.beginTransaction()
                            .replace(R.id.container, all).commit()
                        "Event" -> supportFragmentManager.beginTransaction()
                            .replace(R.id.container, event).commit()
                    }
                } else {
                    item.setBackgroundResource(R.drawable.rounded_tab_notselected)
                    tab_text.setTextColor(ContextCompat.getColor(this@NotifActivity, R.color.gray_5))
                }
                itemView.setOnClickListener {
                    selectedTab = data
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("selected_tab", selectedTab)
        super.onSaveInstanceState(outState)
    }
}
