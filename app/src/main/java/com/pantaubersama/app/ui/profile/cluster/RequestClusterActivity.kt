package com.pantaubersama.app.ui.profile.cluster

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.pantaubersama.app.CommonActivity
import com.pantaubersama.app.R
import com.pantaubersama.app.data.model.cluster.Category
import com.pantaubersama.app.ui.profile.cluster.categery.ClusterCategoryActivity
import com.pantaubersama.app.utils.PantauConstants
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CHANGE_CATEGORY) {
                val category = data?.getSerializableExtra(PantauConstants.Cluster.CATEGORY) as Category
                partai_selected.text = category.name
            }
        }
    }
}
