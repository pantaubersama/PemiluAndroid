package com.pantaubersama.app.ui.merayakan.rekapitulasi.daerah

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
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.merayakan.rekapitulasi.RekapitulasiAdapter
import com.pantaubersama.app.ui.merayakan.rekapitulasi.tpslist.TPSListActivity
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.enableLottie
import com.pantaubersama.app.utils.extensions.toDp
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.layout_common_recyclerview.*
import kotlinx.android.synthetic.main.layout_empty_state.*
import kotlinx.android.synthetic.main.layout_fail_state.*
import kotlinx.android.synthetic.main.layout_loading_state.*
import timber.log.Timber
import javax.inject.Inject

class RekapitulasiDaerahActivity : BaseActivity<RekapitulasiProvinsiPresenter>(), RekapitulasiProvinsiView {
    private lateinit var adapter: RekapitulasiAdapter
    private var parent: String? = null
    private var parentData: Rekapitulasi? = null

    @Inject
    override lateinit var presenter: RekapitulasiProvinsiPresenter

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun fetchIntentExtra() {
        parent = intent.getStringExtra("parent")
        parentData = intent.getSerializableExtra("parent_data") as Rekapitulasi
    }

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun setLayout(): Int {
        return R.layout.activity_rekapitulasi_daerah
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        var titleDummy = ""
        parentData?.region?.name?.let {
            titleDummy = it
        }
        setupToolbar(true, titleDummy, R.color.white, 4f)
        setupRekapitulasiList()
        loadData()
    }

    private fun loadData() {
        parentData?.region?.code?.let { it1 ->
            parent?.let { presenter.getRekapitulasi(it, it1) }
        }
    }

    private fun setupRekapitulasiList() {
        adapter = RekapitulasiAdapter()
        adapter.listener = object : RekapitulasiAdapter.Listener {
            override fun onClickItem(item: Rekapitulasi) {
                when (parent) {
                    "provinsi" -> RekapitulasiDaerahActivity.start(this@RekapitulasiDaerahActivity, "kabupaten", item, 465)
                    "kabupaten" -> RekapitulasiDaerahActivity.start(this@RekapitulasiDaerahActivity, "kecamatan", item, 466)
                    "kecamatan" -> TPSListActivity.start(this@RekapitulasiDaerahActivity, item, 467)
                }
            }
        }
        recycler_view.setPadding(0, 8f.toDp(this@RekapitulasiDaerahActivity), 0, 8f.toDp(this@RekapitulasiDaerahActivity))
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = adapter
        swipe_refresh.setOnRefreshListener {
            loadData()
            swipe_refresh.isRefreshing = false
        }
    }

    override fun bindRekapitulasi(data: MutableList<Rekapitulasi>) {
        recycler_view.visibleIf(true)
        adapter.setDatas(data as MutableList<ItemModel>)
    }

    override fun showFailedLoadRekapitulasiAlert() {
        ToastUtil.show(this@RekapitulasiDaerahActivity, "Gagal memuat rekapitulasi daerah")
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
        fun start(context: Context, from: String, item: Rekapitulasi, requsetCode: Int? = null) {
            val intent = Intent(context, RekapitulasiDaerahActivity::class.java)
            intent.putExtra("parent", from)
            intent.putExtra("parent_data", item)
            if (requsetCode != null) {
                (context as Activity).startActivityForResult(intent, requsetCode)
            } else {
                context.startActivity(intent)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}
