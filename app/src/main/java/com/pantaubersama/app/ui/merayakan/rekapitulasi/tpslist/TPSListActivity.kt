package com.pantaubersama.app.ui.merayakan.rekapitulasi.tpslist

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.rekapitulasi.Rekapitulasi
import com.pantaubersama.app.data.model.tps.TPS
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.merayakan.rekapitulasi.detailtps.DetailTPSActivity
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.enableLottie
import com.pantaubersama.app.utils.extensions.toDp
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.layout_common_recyclerview.*
import kotlinx.android.synthetic.main.layout_empty_state.*
import kotlinx.android.synthetic.main.layout_fail_state.*
import kotlinx.android.synthetic.main.layout_loading_state.*
import javax.inject.Inject

class TPSListActivity : BaseActivity<TPSListPresenter>(), TPSListView {
    private lateinit var adapter: TPSAdapter

    @Inject
    override lateinit var presenter: TPSListPresenter

    private var rekapitulasi: Rekapitulasi? = null
    private var page = 1
    private var perPage = 25

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun fetchIntentExtra() {
        rekapitulasi = intent.getSerializableExtra("rekapitulasi") as Rekapitulasi
    }

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun setLayout(): Int {
        return R.layout.activity_tpslist
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        var title = ""
        rekapitulasi?.let {
            title = it.region.name
        }
        setupToolbar(true, title, R.color.white, 4f)
        setupTPSList()
        loadData()
    }

    private fun setupTPSList() {
        adapter = TPSAdapter()
        adapter.listener = object : TPSAdapter.Listener {
            override fun onClickItem(item: TPS) {
                DetailTPSActivity.start(this@TPSListActivity, item, 468)
            }
        }
        adapter.addSupportLoadMore(recycler_view, 5) {
            adapter.setLoading()
            loadData()
        }
        recycler_view.setPadding(0, 8f.toDp(this@TPSListActivity), 0, 8f.toDp(this@TPSListActivity))
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = adapter
        swipe_refresh.setOnRefreshListener {
            loadData()
        }
    }

    private fun loadData() {
        swipe_refresh.isRefreshing = false
        rekapitulasi?.let {
            presenter.getTPSListData(page, perPage, it.region.code)
        }
    }

    override fun bindTpses(tpses: MutableList<TPS>) {
        recycler_view.visibleIf(true)
        adapter.setDatas(tpses as MutableList<ItemModel>)
    }

    override fun showEmptyDataAlert() {
        view_empty_state.enableLottie(true, lottie_empty_state)
    }

    override fun setDataEnd() {
        adapter.setDataEnd(true)
    }

    override fun bindNextTpses(tpses: MutableList<TPS>) {
        adapter.setLoaded()
        adapter.addData(tpses as MutableList<ItemModel>)
    }

    override fun showEmptyNextDataAlert() {
        ToastUtil.show(this@TPSListActivity, "Gagal memuat lebih banyak data")
    }

    override fun showFailedLoadDataAlert() {
        view_fail_state.enableLottie(true, lottie_fail_state)
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
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun start(context: Context, item: Rekapitulasi, requstCode: Int? = null) {
            val intent = Intent(context, TPSListActivity::class.java)
            intent.putExtra("rekapitulasi", item)
            if (requstCode != null) {
                (context as Activity).startActivityForResult(intent, requstCode)
            } else {
                context.startActivity(intent)
            }
        }
    }
}
