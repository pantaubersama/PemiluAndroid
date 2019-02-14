package com.pantaubersama.app.ui.notification

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.notification.NotificationWhole
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.activity_notif.*
import kotlinx.android.synthetic.main.catatan_tab_item.*
import javax.inject.Inject

class NotifActivity : BaseActivity<NotifPresenter>(), NotifView {

    private lateinit var tabAdapter: NotifTabAdapter
    var selectedTab: String = ""
    private lateinit var notificationAdapter: NotifAdapter
    private var allData: MutableList<NotificationWhole> = ArrayList()
    private var onlyEvent: MutableList<NotificationWhole> = ArrayList()

    @Inject
    override lateinit var presenter: NotifPresenter

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun setLayout(): Int {
        return R.layout.activity_notif
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, getString(R.string.title_notification), R.color.white, 4f)
        setupTabRecyclerview()
        setupNotifications()
        presenter.getNotifications()
    }

    private fun setupNotifications() {
        notificationAdapter = NotifAdapter()
        notifications.layoutManager = LinearLayoutManager(this@NotifActivity)
        notifications.adapter = notificationAdapter
        swipe_refresh.setOnRefreshListener {
            presenter.getNotifications()
            swipe_refresh.isRefreshing = false
        }
        tabAdapter.setSelected(0)
    }

    override fun showFailedLoadNotificationAlert() {
        ToastUtil.show(this@NotifActivity, "Gagal memuat notifikasi")
    }

    override fun bindNotifications(notifications: MutableList<NotificationWhole>) {
        allData.addAll(notifications)
        notificationAdapter.setDatas(allData as MutableList<ItemModel>)
        for (notif in notifications) {
            if (notif.data.notif_type != null) {
                if (notif.data.notif_type == "broadcasts") {
                    onlyEvent.add(notif)
                }
            } else if (notif.data.payload.notif_type != null) {
                if (notif.data.payload.notif_type == "broadcasts") {
                    onlyEvent.add(notif)
                }
            }
        }
    }

    override fun showEmptyDataAlert() {
        notif_blank.visibility = View.VISIBLE
    }

    override fun showLoading() {
        progress_bar.visibility = View.VISIBLE
    }

    override fun dismissLoading() {
        progress_bar.visibility = View.GONE
    }

    private fun setupTabRecyclerview() {
        notif_recycler_view_menu.layoutManager =
                LinearLayoutManager(this@NotifActivity, LinearLayoutManager.HORIZONTAL, false)
        tabAdapter = NotifTabAdapter()
        notif_recycler_view_menu.adapter = tabAdapter
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
                }
                itemView.setOnClickListener {
                    selectedTab = data
                    notifyDataSetChanged()
                    setPage(position)
                }
            }
        }
    }

    private fun setPage(position: Int) {
        if (position == 0) {
            notificationAdapter.setDatas(allData as MutableList<ItemModel>)
        } else {
            notificationAdapter.setDatas(onlyEvent as MutableList<ItemModel>)
        }
    }
}
