package com.pantaubersama.app.ui.merayakan.perhitungan.create.perhitunganhome

import android.content.Intent
import android.os.Bundle
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.merayakan.perhitungan.create.c1.C1FormActivity
import com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.dprri.PerhitunganDPRRIActivity
import com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.presiden.PerhitunganPresidenActivity
import com.pantaubersama.app.ui.merayakan.perhitungan.create.uploaddokumen.UploadDocumentActivity
import kotlinx.android.synthetic.main.activity_perhitunganmain.*
import javax.inject.Inject

class PerhitunganMainActivity : BaseActivity<PerhitunganMainPresenter>(), PerhitunganMainView {

    @Inject
    override lateinit var presenter: PerhitunganMainPresenter

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun setLayout(): Int {
        return R.layout.activity_perhitunganmain
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, "Perhitungan", R.color.white, 4f)
        president_counter_action.setOnClickListener {
            startActivity(Intent(this@PerhitunganMainActivity, PerhitunganPresidenActivity::class.java))
        }
        president_c1_action.setOnClickListener {
            val intent = Intent(this@PerhitunganMainActivity, C1FormActivity::class.java)
            intent.putExtra("c1_type", "presiden")
            startActivity(intent)
        }
        dpr_ri_counter_action.setOnClickListener {
            startActivity(Intent(this@PerhitunganMainActivity, PerhitunganDPRRIActivity::class.java))
        }
        dpr_ri_c1_action.setOnClickListener {
            val intent = Intent(this@PerhitunganMainActivity, C1FormActivity::class.java)
            intent.putExtra("c1_type", "dpr_ri")
            startActivity(intent)
        }
//        dpd_action.setOnClickListener {
//            startActivity(Intent(this@PerhitunganMainActivity, PerhitunganDPDActivity::class.java))
//        }
//        dpr_provinsi_action.setOnClickListener {
//            startActivity(Intent(this@PerhitunganMainActivity, PerhitunganDPRDProvinsiActivity::class.java))
//        }
//        dpr_kabupaten_action.setOnClickListener {
//            startActivity(Intent(this@PerhitunganMainActivity, PerhitunganDPRDKabupatenActivity::class.java))
//        }
        upload_container.setOnClickListener {
            startActivity(Intent(this@PerhitunganMainActivity, UploadDocumentActivity::class.java))
        }
    }

    override fun showLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
