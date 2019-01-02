package com.pantaubersama.app.ui.linimasa.janjipolitik.filter

import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.model.cluster.ClusterItem
import com.pantaubersama.app.ui.clusterdialog.ClusterListDialog
import kotlinx.android.synthetic.main.activity_filter_janji_politik.*
import kotlinx.android.synthetic.main.item_cluster.*

class FilterJanjiPolitikActivity : BaseActivity<BasePresenter<*>>() {

    override fun initPresenter(): BasePresenter<*>? {
        return null
    }

    override fun statusBarColor(): Int? {
        return R.color.white
    }

    override fun fetchIntentExtra() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setupUI() {
        setupToolbar(true, getString(R.string.txt_filter), R.color.white, 4f)
        rl_cluster_container.setOnClickListener {
            ClusterListDialog.show(supportFragmentManager, object : ClusterListDialog.OnClickListener {
                override fun onClick(item: ClusterItem) {
                    selectCluster(item)
                }
            })
        }
    }

    private fun selectCluster(item: ClusterItem) {
        tv_cluster_placeholder.visibility = View.GONE
        layout_item_cluster.visibility = View.VISIBLE
        tv_cluster_name.text = item.name
        tv_cluster_member_count.setText("0 anggota")
    }

    override fun setLayout(): Int {
        return R.layout.activity_filter_janji_politik
    }

    override fun showLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_filter, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.action_reset -> {
                // reset
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
