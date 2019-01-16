package com.pantaubersama.app.ui.penpol.tanyakandidat.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.di.component.ActivityComponent
import javax.inject.Inject

class DetailTanyaKandidat : BaseActivity<DetailTanyaKandidatPresenter>() {
    @Inject override lateinit var presenter: DetailTanyaKandidatPresenter

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun statusBarColor(): Int? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setLayout(): Int = R.layout.activity_detail_tanya_kandidat

    override fun setupUI(savedInstanceState: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
