package com.pantaubersama.app.ui.notification

import android.os.Bundle
import android.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.notification.NotificationWhole
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.penpol.kuis.detail.DetailKuisActivity
import com.pantaubersama.app.ui.penpol.tanyakandidat.detail.DetailTanyaKandidatActivity
import com.pantaubersama.app.ui.profile.setting.badge.detail.DetailBadgeActivity
import com.pantaubersama.app.utils.ChromeTabUtil
import com.pantaubersama.app.utils.ToastUtil
import kotlinx.android.synthetic.main.fragment_notif_child.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 *
 */
class NotifChildFragment : BaseFragment<NotifPresenter>(), NotifView {
    var selectedTab: String = ""
    private lateinit var notificationAdapter: NotifAdapter
    private var page: Int = 1
    private var perPage = 20

    @Inject
    override lateinit var presenter: NotifPresenter

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun fetchArguments(args: Bundle?) {
        args?.getString("selected_tab")?.let {
            selectedTab = it
        } ?: "Semua Notifikasi"
    }

    override fun setLayout(): Int {
        return R.layout.fragment_notif_child
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        setupNotifications()
        presenter.getNotifications(page, perPage, selectedTab)
    }

    private fun setupNotifications() {
        notificationAdapter = NotifAdapter()
        notificationAdapter.listener = object : NotifAdapter.Listener {
            override fun onClickTanyaKandidat(id: String) {
                val intent = DetailTanyaKandidatActivity.setIntent(requireContext(), id)
                startActivity(intent)
            }

            override fun onClickBadge(id: String) {
                val intent = DetailBadgeActivity.setIntent(requireContext(), id)
                startActivity(intent)
            }

            override fun onClickQuiz(id: String) {
                val intent = DetailKuisActivity.setIntent(requireContext(), id)
                startActivity(intent)
            }

            override fun onClickBroadcast(link: String) {
                ChromeTabUtil(requireContext()).forceLoadUrl(link)
            }
        }
        notifications.layoutManager = LinearLayoutManager(requireContext())
        notificationAdapter.addSupportLoadMore(notifications, 5) {
            notificationAdapter.setLoading()
            presenter.getNotifications(it, perPage, selectedTab)
        }
        notifications.adapter = notificationAdapter
        swipe_refresh.setOnRefreshListener {
            presenter.getNotifications(page, perPage, selectedTab)
            swipe_refresh.isRefreshing = false
            notificationAdapter.setDataEnd(false)
        }
    }

    override fun bindNotifications(notifications: MutableList<NotificationWhole>) {
        notificationAdapter.setDatas(notifications as MutableList<ItemModel>)
    }

    override fun setDataEnd() {
        notificationAdapter.setDataEnd(true)
    }

    override fun bindNextNotifications(notifications: MutableList<NotificationWhole>) {
        notificationAdapter.setLoaded()
        notificationAdapter.addData(notifications as MutableList<ItemModel>)
    }

    override fun showEmptyDataAlert() {
        notificationAdapter.setLoaded()
        notif_blank.visibility = View.VISIBLE
    }

    override fun showLoading() {
        progress_bar.visibility = View.VISIBLE
    }

    override fun dismissLoading() {
        progress_bar.visibility = View.GONE
    }

    override fun showFailedLoadNotificationAlert() {
        ToastUtil.show(requireContext(), "Gagal memuat notifikasi")
    }

    companion object {
        var TAG: String = ""

        fun newInstance(selectedTab: String): NotifChildFragment {
            TAG = selectedTab
            val bundle = Bundle()
            bundle.putString("selected_tab", selectedTab)
            val child = NotifChildFragment()
            child.arguments = bundle
            return child
        }
    }
}
