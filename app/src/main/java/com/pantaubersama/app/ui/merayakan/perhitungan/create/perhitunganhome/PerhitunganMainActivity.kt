package com.pantaubersama.app.ui.merayakan.perhitungan.create.perhitunganhome

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.merayakan.perhitungan.create.c1.C1FormActivity
import com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.dpd.PerhitunganDPDActivity
import com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.dprdkabupaten.PerhitunganDPRDKabupatenActivity
import com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.dprdprovinsi.PerhitunganDPRDProvinsiActivity
import com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.dprri.PerhitunganDPRRIActivity
import com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.presiden.PerhitunganPresidenActivity
import com.pantaubersama.app.ui.merayakan.perhitungan.create.uploaddokumen.UploadDocumentActivity
import com.pantaubersama.app.utils.PantauConstants
import kotlinx.android.synthetic.main.activity_perhitunganmain.*
import javax.inject.Inject

class PerhitunganMainActivity : BaseActivity<PerhitunganMainPresenter>(), PerhitunganMainView, View.OnClickListener {

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
        president_counter_action.setOnClickListener(this)
        president_c1_action.setOnClickListener(this)
        dpr_ri_counter_action.setOnClickListener(this)
        dpr_ri_c1_action.setOnClickListener(this)
        dpd_counter_action.setOnClickListener(this)
        dpd_c1_action.setOnClickListener(this)
        dprd_provinsi_counter_action.setOnClickListener(this)
        dprd_provinsi_c1_action.setOnClickListener(this)
        dprd_kabupaten_counter_action.setOnClickListener(this)
        dprd_kabupaten_c1_action.setOnClickListener(this)
//        dpr_provinsi_action.setOnClickListener {
//            startActivity(Intent(this@PerhitunganMainActivity, PerhitunganDPRDProvinsiActivity::class.java))
//        }
//        dpr_kabupaten_action.setOnClickListener {
//            startActivity(Intent(this@PerhitunganMainActivity, PerhitunganDPRDKabupatenActivity::class.java))
//        }
        upload_container.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view) {
            president_counter_action -> {
                startActivity(Intent(this@PerhitunganMainActivity, PerhitunganPresidenActivity::class.java))
            }
            president_c1_action -> {
                val intent = Intent(this@PerhitunganMainActivity, C1FormActivity::class.java)
                intent.putExtra(PantauConstants.Merayakan.C1_MODEL_TYPE, "presiden")
                startActivity(intent)
            }
            dpr_ri_counter_action -> {
                startActivity(Intent(this@PerhitunganMainActivity, PerhitunganDPRRIActivity::class.java))
            }
            dpr_ri_c1_action -> {
                val intent = Intent(this@PerhitunganMainActivity, C1FormActivity::class.java)
                intent.putExtra(PantauConstants.Merayakan.C1_MODEL_TYPE, "dpr_ri")
                startActivity(intent)
            }
            dpd_counter_action -> {
                startActivity(Intent(this@PerhitunganMainActivity, PerhitunganDPDActivity::class.java))
            }
            dpd_c1_action -> {
                val intent = Intent(this@PerhitunganMainActivity, C1FormActivity::class.java)
                intent.putExtra(PantauConstants.Merayakan.C1_MODEL_TYPE, "dpd")
                startActivity(intent)
            }
            dprd_provinsi_counter_action -> {
                startActivity(Intent(this@PerhitunganMainActivity, PerhitunganDPRDProvinsiActivity::class.java))
            }
            dprd_provinsi_c1_action -> {
                val intent = Intent(this@PerhitunganMainActivity, C1FormActivity::class.java)
                intent.putExtra(PantauConstants.Merayakan.C1_MODEL_TYPE, "dprd_provinsi")
                startActivity(intent)
            }
            dprd_kabupaten_counter_action -> {
                startActivity(Intent(this@PerhitunganMainActivity, PerhitunganDPRDKabupatenActivity::class.java))
            }
            dprd_kabupaten_c1_action -> {
                val intent = Intent(this@PerhitunganMainActivity, C1FormActivity::class.java)
                intent.putExtra(PantauConstants.Merayakan.C1_MODEL_TYPE, "dprd_kabupaten")
                startActivity(intent)
            }
            upload_container -> {
                startActivity(Intent(this@PerhitunganMainActivity, UploadDocumentActivity::class.java))
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
