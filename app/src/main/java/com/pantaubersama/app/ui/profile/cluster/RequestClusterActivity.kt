package com.pantaubersama.app.ui.profile.cluster

import android.content.Intent
import android.os.Bundle
import com.pantaubersama.app.CommonActivity
import com.pantaubersama.app.R
import kotlinx.android.synthetic.main.activity_request_cluster.*

class RequestClusterActivity : CommonActivity() {

    val CHANGE_CATEGORY = 1

    override fun statusBarColor(): Int? = R.color.white

    override fun setLayout(): Int = R.layout.activity_request_cluster

    override fun fetchIntentExtra() {
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, "Request Buat Cluster", R.color.white, 4f)
        onClickAction()
    }

    private fun onClickAction() {
        request_cluster_category.setOnClickListener {
            val intent = Intent(this@RequestClusterActivity, ClusterCategoryActivity::class.java)
            startActivityForResult(intent, CHANGE_CATEGORY)
        }
    }
}
