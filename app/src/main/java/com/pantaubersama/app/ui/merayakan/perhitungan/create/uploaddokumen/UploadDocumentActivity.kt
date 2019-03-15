package com.pantaubersama.app.ui.merayakan.perhitungan.create.uploaddokumen

import android.os.Bundle
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.di.component.ActivityComponent
import kotlinx.android.synthetic.main.activity_upload_document.*
import javax.inject.Inject

class UploadDocumentActivity : BaseActivity<UploadDocumentPresenter>(), UploadDocumentView {

    @Inject
    override lateinit var presenter: UploadDocumentPresenter

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun setLayout(): Int {
        return R.layout.activity_upload_document
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, "Unggah", R.color.white, 4f)
        save_button.setOnClickListener {
            finish()
        }
    }

    override fun showLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
