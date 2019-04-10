package com.pantaubersama.app.ui.merayakan.perhitungan.create.perhitunganhome

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.* // ktlint-disable
import com.pantaubersama.app.R
import com.pantaubersama.app.background.uploadtps.UploadTpsService
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.tps.TPS
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.merayakan.perhitungan.create.c1.C1FormActivity
import com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.dpd.PerhitunganDPDActivity
import com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.dpr.PerhitunganDPRActivity
import com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.presiden.PerhitunganPresidenActivity
import com.pantaubersama.app.ui.merayakan.perhitungan.create.tpsdata.DataTPSActivity
import com.pantaubersama.app.ui.merayakan.perhitungan.create.uploaddokumen.UploadDocumentActivity
import com.pantaubersama.app.ui.widget.ConfirmationDialog
import com.pantaubersama.app.ui.widget.OptionDialog
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_perhitunganmain.*
import kotlinx.android.synthetic.main.publish_confirmation_dialog.*
import javax.inject.Inject
import androidx.core.content.ContextCompat

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
        submit_button.setOnClickListener(this)
        submitted_button.setOnClickListener(this)
    }

    private fun bindData(tps: TPS?) {
        tps_number.text = "TPS ${tps?.tps}"
        province_text.text = tps?.province?.name
        regency_text.text = tps?.regency?.name
        district_text.text = tps?.district?.name
        village_text.text = tps?.village?.name
        tps_options.setOnClickListener {
            val dialog = OptionDialog(this@PerhitunganMainActivity, R.layout.layout_option_dialog_tps)
            if (tps?.status == "published") {
                dialog.removeItem(R.id.edit_tps_data_action)
            } else if (tps?.status == "sandbox") {
                dialog.removeItem(R.id.delete_tps_item_action)
            }
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
                        R.id.delete_tps_item_action -> {
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
        if (tps?.status == "published") {
            submit_button.visibility = View.GONE
            submitted_button.visibility = View.VISIBLE
        } else if (tps?.status == "local") {
            submit_button.visibility = View.VISIBLE
            submitted_button.visibility = View.GONE
        } else {
            submit_button.visibility = View.VISIBLE
            submitted_button.visibility = View.GONE
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
                val intent = Intent(this@PerhitunganMainActivity, PerhitunganDPRActivity::class.java)
                intent.putExtra(PantauConstants.Merayakan.TPS_DATA, tps)
                intent.putExtra(PantauConstants.Merayakan.REAL_COUNT_TYPE, "dpr")
                startActivity(intent)
            }
            dpr_ri_c1_action -> {
                val intent = Intent(this@PerhitunganMainActivity, C1FormActivity::class.java)
                intent.putExtra(PantauConstants.Merayakan.TPS_DATA, tps)
                intent.putExtra(PantauConstants.Merayakan.C1_MODEL_TYPE, "dpr")
                startActivity(intent)
            }
            dpd_counter_action -> {
                val intent = Intent(this@PerhitunganMainActivity, PerhitunganDPDActivity::class.java)
                intent.putExtra(PantauConstants.Merayakan.TPS_DATA, tps)
                intent.putExtra(PantauConstants.Merayakan.REAL_COUNT_TYPE, "dpd")
                startActivity(intent)
            }
            dpd_c1_action -> {
                val intent = Intent(this@PerhitunganMainActivity, C1FormActivity::class.java)
                intent.putExtra(PantauConstants.Merayakan.TPS_DATA, tps)
                intent.putExtra(PantauConstants.Merayakan.C1_MODEL_TYPE, "dpd")
                startActivity(intent)
            }
            dprd_provinsi_counter_action -> {
                val intent = Intent(this@PerhitunganMainActivity, PerhitunganDPRActivity::class.java)
                intent.putExtra(PantauConstants.Merayakan.TPS_DATA, tps)
                intent.putExtra(PantauConstants.Merayakan.REAL_COUNT_TYPE, "provinsi")
                startActivity(intent)
            }
            dprd_provinsi_c1_action -> {
                val intent = Intent(this@PerhitunganMainActivity, C1FormActivity::class.java)
                intent.putExtra(PantauConstants.Merayakan.TPS_DATA, tps)
                intent.putExtra(PantauConstants.Merayakan.C1_MODEL_TYPE, "provinsi")
                startActivity(intent)
            }
            dprd_kabupaten_counter_action -> {
                val intent = Intent(this@PerhitunganMainActivity, PerhitunganDPRActivity::class.java)
                intent.putExtra(PantauConstants.Merayakan.TPS_DATA, tps)
                intent.putExtra(PantauConstants.Merayakan.REAL_COUNT_TYPE, "kabupaten")
                startActivity(intent)
            }
            dprd_kabupaten_c1_action -> {
                val intent = Intent(this@PerhitunganMainActivity, C1FormActivity::class.java)
                intent.putExtra(PantauConstants.Merayakan.TPS_DATA, tps)
                intent.putExtra(PantauConstants.Merayakan.C1_MODEL_TYPE, "kabupaten")
                startActivity(intent)
            }
            upload_container -> {
                val intent = Intent(this@PerhitunganMainActivity, UploadDocumentActivity::class.java)
                intent.putExtra(PantauConstants.Merayakan.TPS_DATA, tps)
                startActivity(Intent(intent))
            }
            submit_button -> {
                if (tps?.status != "sandbox") {
                    showPublishConfirmationDialog()
                } else {
                    finishSection()
                }
            }
            submitted_button -> {
                ToastUtil.show(this@PerhitunganMainActivity, "RealCountData Telah Terkirim")
            }
        }
    }

    private fun showPublishConfirmationDialog() {
        val dialog = Dialog(this)
        val dialogLayout = LayoutInflater.from(this@PerhitunganMainActivity).inflate(R.layout.publish_confirmation_dialog, null)
        dialog.setContentView(dialogLayout)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setOnKeyListener { dialogInterface, i, keyEvent ->
            if (i == KeyEvent.KEYCODE_BACK) {
                dialog.dismiss()
                true
            } else {
                false
            }
        }
        dialog.setCanceledOnTouchOutside(true)
        dialog.no_button.setOnClickListener {
            dialog.dismiss()
        }
        dialog.yes_button.setOnClickListener {
            dialog.dismiss()
            tps?.id?.let {
                val intent = Intent(this@PerhitunganMainActivity, UploadTpsService::class.java)
                intent.putExtra("tps_id", it)
                ContextCompat.startForegroundService(this@PerhitunganMainActivity, intent)
                finishSection()
            }
        }
        val lp = WindowManager.LayoutParams()
        val window = dialog.window
        lp.copyFrom(window?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window?.attributes = lp
        lp.gravity = Gravity.CENTER
        window?.attributes = lp
        dialog.show()
    }

    override fun showLoading() {
        showProgressDialog("Menghapus data")
    }

    override fun dismissLoading() {
        dismissProgressDialog()
    }

    override fun onBackPressed() {
        if (intent.getBooleanExtra("is_new", false)) {
            setResult(Activity.RESULT_OK)
        }
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                if (intent.getBooleanExtra("is_new", false)) {
                    setResult(Activity.RESULT_OK)
                }
                finish()
            }
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PantauConstants.Merayakan.CREATE_PERHITUNGAN_REQUEST_CODE) {
                setResult(Activity.RESULT_OK)
                tps?.id?.let { presenter.getTps(it) }
            }
        }
    }

    override fun bindTps(tps: TPS) {
//        setResult(Activity.RESULT_OK)
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
