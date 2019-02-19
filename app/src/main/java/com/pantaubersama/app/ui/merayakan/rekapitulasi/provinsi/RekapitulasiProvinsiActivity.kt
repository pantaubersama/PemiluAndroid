package com.pantaubersama.app.ui.merayakan.rekapitulasi.provinsi

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.rekapitulasi.RekapitulasiData
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.merayakan.rekapitulasi.RekapitulasiAdapter
import com.pantaubersama.app.utils.extensions.enableLottie
import com.pantaubersama.app.utils.extensions.toDp
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.layout_common_recyclerview.*
import kotlinx.android.synthetic.main.layout_empty_state.*
import kotlinx.android.synthetic.main.layout_fail_state.*
import kotlinx.android.synthetic.main.layout_loading_state.*
import javax.inject.Inject

class RekapitulasiProvinsiActivity : BaseActivity<RekapitulasiProvinsiPresenter>(), RekapitulasiProvinsiView {
    private lateinit var adapter: RekapitulasiAdapter

    @Inject
    override lateinit var presenter: RekapitulasiProvinsiPresenter

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun setLayout(): Int {
        return R.layout.activity_rekapitulasi_provinsi
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, "Yogyakarta", R.color.white, 4f)
        setupRekapitulasiList()
        presenter.getRekapitulasiData()
    }

    private fun setupRekapitulasiList() {
        adapter = RekapitulasiAdapter()
        recycler_view.setPadding(0, 8f.toDp(this@RekapitulasiProvinsiActivity), 0, 8f.toDp(this@RekapitulasiProvinsiActivity))
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = adapter
        swipe_refresh.setOnRefreshListener {
            presenter.getRekapitulasiData()
            swipe_refresh.isRefreshing = false
        }
    }

    override fun bindRekapitulasi(data: MutableList<RekapitulasiData>) {
        recycler_view.visibleIf(true)
        adapter.setDatas(data as MutableList<ItemModel>)
    }

    override fun showLoading() {
        lottie_loading.enableLottie(true, lottie_loading)
        view_empty_state.enableLottie(false, lottie_empty_state)
        view_fail_state.enableLottie(false, lottie_fail_state)
        recycler_view.visibleIf(false)
    }

    override fun dismissLoading() {
        lottie_loading.enableLottie(false, lottie_loading)
        recycler_view.visibleIf(false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_close, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.close_action -> {
//                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
