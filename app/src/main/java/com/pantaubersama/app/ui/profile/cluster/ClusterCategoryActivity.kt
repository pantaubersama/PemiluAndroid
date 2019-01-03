package com.pantaubersama.app.ui.profile.cluster

import android.app.AlertDialog
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import kotlinx.android.synthetic.main.activity_cluster_category.*
import kotlinx.android.synthetic.main.add_category_dialog.view.*

class ClusterCategoryActivity : BaseActivity<ClusterCategoryPresenter>(), ClusterCategoryView {

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun fetchIntentExtra() {
        // ok
    }

    override fun initPresenter(): ClusterCategoryPresenter? {
        return ClusterCategoryPresenter()
    }

    override fun setupUI() {

        onclickAction()
    }

    private fun onclickAction() {
        cluster_category_back.setOnClickListener {
            finish()
        }
        cluster_category_add.setOnClickListener {
            addCategoryDialog()
        }
    }

    private fun addCategoryDialog() {
        val builder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.add_category_dialog, null)
        val textCategory = view.add_category_text
        builder.setView(view)
        val dialog: AlertDialog = builder.create()
        view.add_category_buat.setOnClickListener {
            // buat
        }
        view.add_category_batal.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun setLayout(): Int {
        return R.layout.activity_cluster_category
    }

    override fun showLoading() {
        // show
    }

    override fun dismissLoading() {
        // dismiss
    }
}