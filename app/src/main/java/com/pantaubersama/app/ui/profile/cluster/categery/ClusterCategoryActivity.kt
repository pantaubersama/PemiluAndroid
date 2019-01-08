package com.pantaubersama.app.ui.profile.cluster.categery

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.enable
import kotlinx.android.synthetic.main.activity_cluster_category.*
import kotlinx.android.synthetic.main.add_category_dialog.*
import javax.inject.Inject

class ClusterCategoryActivity : BaseActivity<ClusterCategoryPresenter>(), ClusterCategoryView {
    @Inject
    override lateinit var presenter: ClusterCategoryPresenter
    private lateinit var newCategoryDialog: Dialog

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun fetchIntentExtra() {
        // ok
    }

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupNewCategoryDialog()
        onclickAction()
    }

    private fun onclickAction() {
        cluster_category_back.setOnClickListener {
            finish()
        }
        cluster_category_add.setOnClickListener {
            newCategoryDialog.show()
        }
    }

    private fun setupNewCategoryDialog() {
        newCategoryDialog = Dialog(this@ClusterCategoryActivity)
        newCategoryDialog.setContentView(R.layout.add_category_dialog)
        newCategoryDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        newCategoryDialog.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                newCategoryDialog.dismiss()
                true
            } else {
                false
            }
        }
        newCategoryDialog.setCanceledOnTouchOutside(true)
        val lp = WindowManager.LayoutParams()
        val window = newCategoryDialog.window
        lp.copyFrom(window?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window?.attributes = lp
        lp.gravity = Gravity.CENTER
        window?.attributes = lp
        newCategoryDialog.add_category_buat.setOnClickListener {
            presenter.createNewCategory(newCategoryDialog.add_category_text.text.toString())
            newCategoryDialog.dismiss()
        }
        newCategoryDialog.add_category_batal.setOnClickListener {
            newCategoryDialog.dismiss()
        }
    }

    override fun setLayout(): Int {
        return R.layout.activity_cluster_category
    }

    override fun showLoading() {

    }

    override fun dismissLoading() {

    }

    override fun showAddCategoryLoading() {
        newCategoryDialog.loading.visibility = View.VISIBLE
    }

    override fun disableAddCategoryView() {
        newCategoryDialog.new_category_container.enable(false)
    }

    override fun onSuccessAddNewCategory() {
        newCategoryDialog.dismiss()
    }

    override fun enableAddCategoryView() {
        newCategoryDialog.new_category_container.enable(true)
    }

    override fun showFailedAddCategoryAlert() {
        ToastUtil.show(this@ClusterCategoryActivity, getString(R.string.failed_add_category_alert))
    }

    override fun dismissAddCategoryLoading() {
        newCategoryDialog.loading.visibility = View.GONE
    }
}