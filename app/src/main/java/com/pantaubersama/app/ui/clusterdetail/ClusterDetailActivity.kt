package com.pantaubersama.app.ui.clusterdetail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.cluster.ClusterItem
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.enableLottie
import com.pantaubersama.app.utils.extensions.loadUrl
import kotlinx.android.synthetic.main.activity_cluster_detail.*
import kotlinx.android.synthetic.main.item_loading.*
import javax.inject.Inject

class ClusterDetailActivity : BaseActivity<ClusterDetailPresenter>(), ClusterDetailView {
    private lateinit var clusterId: String
    @Inject
    override lateinit var presenter: ClusterDetailPresenter

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun fetchIntentExtra() {
        clusterId = intent.getStringExtra(PantauConstants.Cluster.CLUSTER_ID)
    }

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun setLayout(): Int {
        return R.layout.activity_cluster_detail
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        presenter.getClusterById(clusterId)
        swipe_refresh.setOnRefreshListener {
            presenter.getClusterById(clusterId)
            swipe_refresh.isRefreshing = false
        }
        close_button.setOnClickListener {
            finish()
        }
    }

    override fun showFailedGetClusterAlert() {
        ToastUtil.show(this@ClusterDetailActivity, "Gagal memuat data cluster")
    }

    @SuppressLint("SetTextI18n")
    override fun bindClusterData(clusterItem: ClusterItem) {
        cluster_image.loadUrl(clusterItem.image?.medium?.url, R.drawable.ic_avatar_placeholder)
        cluster_name.text = clusterItem.name
        cluster_category.text = clusterItem.category?.name
        cluster_member_count.text = clusterItem.memberCount.toString() + " Anggota"
        cluster_description.text = clusterItem.description
    }

    override fun showLoading() {
//        loading.visibility = View.VISIBLE
        lottie_loading.enableLottie(true)
    }

    override fun dismissLoading() {
//        loading.visibility = View.GONE
        lottie_loading.enableLottie(false)
    }

    companion object {
        fun start(context: Context, clusterId: String) {
            val intent = Intent(context, ClusterDetailActivity::class.java)
            intent.putExtra(PantauConstants.Cluster.CLUSTER_ID, clusterId)
            context.startActivity(intent)
        }
    }
}
