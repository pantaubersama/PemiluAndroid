package com.pantaubersama.app.ui.merayakan.perhitungan.create.perhitunganhome

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.tps.TPS
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.merayakan.perhitungan.create.c1.C1FormActivity
import com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.dpd.PerhitunganDPDActivity
import com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.dprdkabupaten.PerhitunganDPRDKabupatenActivity
import com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.dprdprovinsi.PerhitunganDPRDProvinsiActivity
import com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.dprri.PerhitunganDPRRIActivity
import com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.presiden.PerhitunganPresidenActivity
import com.pantaubersama.app.ui.merayakan.perhitungan.create.tpsdata.DataTPSActivity
import com.pantaubersama.app.ui.merayakan.perhitungan.create.uploaddokumen.UploadDocumentActivity
import com.pantaubersama.app.ui.widget.ConfirmationDialog
import com.pantaubersama.app.ui.widget.OptionDialog
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_perhitunganmain.*
import javax.inject.Inject

class PerhitunganMainActivity : BaseActivity<PerhitunganMainPresenter>(), PerhitunganMainView, View.OnClickListener {
    private var tps: TPS? = null

    @Inject
    override lateinit var presenter: PerhitunganMainPresenter

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun fetchIntentExtra() {
        intent.getSerializableExtra(PantauConstants.Merayakan.TPS_DATA)?.let {
            tps = it as TPS
        }
    }

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun setLayout(): Int {
        return R.layout.activity_perhitunganmain
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, "Perhitungan", R.color.white, 4f)
        bindData(tps)
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

    private fun bindData(tps: TPS?) {
        tps_number.text = "TPS ${tps?.tps}"
        province_text.text = tps?.province?.name
        regency_text.text = tps?.regency?.name
        district_text.text = tps?.district?.name
        village_text.text = tps?.village?.name
        tps_options.setOnClickListener {
            val dialog = OptionDialog(this@PerhitunganMainActivity, R.layout.layout_option_dialog_tps)
            dialog.show()
            dialog.listener = object : OptionDialog.DialogListener {
                override fun onClick(viewId: Int) {
                    when (viewId) {
                        R.id.edit_tps_data_action -> {
                            val intent = Intent(this@PerhitunganMainActivity, DataTPSActivity::class.java)
                            intent.putExtra("tps_data", tps)
                            startActivityForResult(intent, PantauConstants.Merayakan.CREATE_PERHITUNGAN_REQUEST_CODE)
                            dialog.dismiss()
                        }
                        R.id.delete_tanya_kandidat_item_action -> {
                            ConfirmationDialog.Builder()
                                .with(this@PerhitunganMainActivity)
                                .setDialogTitle("Hapus Perhitungan")
                                .setAlert("Apakah kamu yakin untuk menghapus item ini?")
                                .setCancelText("Batal")
                                .setOkText("Ya, Hapus")
                                .addOkListener(object : ConfirmationDialog.DialogOkListener {
                                    override fun onClickOk() {
                                        tps?.let { presenter.deletePerhitungan(it) }
                                    }
                                })
                                .show()
                            dialog.dismiss()
                        }
                    }
                }
            }
        }
    }

    override fun onClick(view: View) {
        when (view) {
            president_counter_action -> {
                val intent = Intent(this@PerhitunganMainActivity, PerhitunganPresidenActivity::class.java)
                intent.putExtra(PantauConstants.Merayakan.TPS_DATA, tps)
                startActivity(intent)
            }
            president_c1_action -> {
                val intent = Intent(this@PerhitunganMainActivity, C1FormActivity::class.java)
                intent.putExtra(PantauConstants.Merayakan.TPS_DATA, tps)
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
        showProgressDialog("Menghapus data")
    }

    override fun dismissLoading() {
        dismissProgressDialog()
    }

    override fun onBackPressed() {
        finishSection()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finishSection()
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PantauConstants.Merayakan.CREATE_PERHITUNGAN_REQUEST_CODE) {
                tps?.id?.let { presenter.getTps(it) }
            }
        }
    }

    override fun bindTps(tps: TPS) {
        setResult(Activity.RESULT_OK)
        bindData(tps)
    }

    override fun showFailedDeleteTpsAlert() {
        ToastUtil.show(this@PerhitunganMainActivity, "Gagal menghapus data TPS")
    }

    override fun onSuccessDeleteTps() {
        finishSection()
    }

    private fun finishSection() {
        setResult(Activity.RESULT_OK)
        finish()
    }
}
