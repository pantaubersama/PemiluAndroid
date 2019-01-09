package com.pantaubersama.app.ui.profile.cluster.requestcluster

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.cluster.Category
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.profile.cluster.categery.ClusterCategoryActivity
import com.pantaubersama.app.ui.widget.ImageChooserTools
import com.pantaubersama.app.utils.PantauConstants
import kotlinx.android.synthetic.main.activity_request_cluster.*
import javax.inject.Inject

class RequestClusterActivity : BaseActivity<RequestClusterPresenter>() {

    val CHANGE_CATEGORY = 1

    @Inject
    override lateinit var presenter: RequestClusterPresenter

    override fun statusBarColor(): Int? = R.color.white

    override fun setLayout(): Int = R.layout.activity_request_cluster

    override fun fetchIntentExtra() {
        // not used yed
    }

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
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
        add_image.setOnClickListener {
            ImageChooserTools.showDialog(this@RequestClusterActivity)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CHANGE_CATEGORY) {
                val category = data?.getSerializableExtra(PantauConstants.Cluster.CATEGORY) as Category
                partai_selected.text = category.name
            } else if (requestCode == PantauConstants.RequestCode.RC_CAMERA) {
                if (data != null) {
                    val file = ImageChooserTools.proccedImageFromCamera(data)
                }
            } else if (requestCode == PantauConstants.RequestCode.RC_STORAGE) {
                if (data != null) {
                    val file = ImageChooserTools.proccedImageFromStorage(data, this@RequestClusterActivity)
                }
            }
        }
    }

    override fun showLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
