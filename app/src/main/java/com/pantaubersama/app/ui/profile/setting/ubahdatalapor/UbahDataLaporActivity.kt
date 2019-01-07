package com.pantaubersama.app.ui.profile.setting.ubahdatalapor

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.user.Informant
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.enable
import kotlinx.android.synthetic.main.activity_ubah_data_lapor.*
import javax.inject.Inject

class UbahDataLaporActivity : BaseActivity<UbahDataLaporPresenter>(), UbahDataLaporView {

    @Inject
    override lateinit var presenter: UbahDataLaporPresenter

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
        setupToolbar(true, getString(R.string.title_ubah_data_lapor), R.color.white, 4f)
        setupGenderSpinner()
//        setEditextDisable(ubah_data_lapor_no_ktp)
        presenter.getDataLapor()
        onClickAction()
    }

    private fun setupGenderSpinner() {
        val gender = arrayOf("Perempuan", "Laki-laki")
        ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            gender
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            ubah_data_lapor_jenis_kelamin.adapter = adapter
        }
    }

    override fun setLayout(): Int {
        return R.layout.activity_ubah_data_lapor
    }

    override fun showLoading() {
        loading.visibility = View.VISIBLE
    }

    override fun dismissLoading() {
        loading.visibility = View.GONE
    }

    private fun onClickAction() {
        ubah_data_lapor_submit.setOnClickListener {
            presenter?.updateDataLapor(
                ubah_data_lapor_no_ktp.text.toString(),
                ubah_data_lapor_tempat_lahir.text.toString(),
                ubah_data_lapor_tanggal_lahir.text.toString(),
                ubah_data_lapor_jenis_kelamin.selectedItemPosition,
                ubah_data_lapor_pekerjaan.text.toString(),
                ubah_data_lapor_kewarganegaraan.text.toString(),
                ubah_data_lapor_alamat.text.toString(),
                ubah_data_lapor_hp.text.toString()
            )
        }
    }

//    fun setEditextDisable(editext: EditText) {
//        editext.tag = editext.keyListener
//        editext.keyListener = null
//    }

//    fun setEditextEnable(editext: EditText) {
//        editext.setKeyListener(editext.getTag() as KeyListener)
//    }

    override fun setDataLapor(informant: Informant?) {
        ubah_data_lapor_no_ktp.setText(informant?.identityNumber)
        ubah_data_lapor_tempat_lahir.setText(informant?.pob)
        ubah_data_lapor_tanggal_lahir.setText(informant?.dob)
        informant?.gender?.let { ubah_data_lapor_jenis_kelamin.setSelection(it) }
        ubah_data_lapor_pekerjaan.setText(informant?.occupation)
        ubah_data_lapor_kewarganegaraan.setText(informant?.nationality)
        ubah_data_lapor_alamat.setText(informant?.address)
        ubah_data_lapor_hp.setText(informant?.phoneNumber)
    }

    override fun showFailedGetDataLaporAlert() {
        ToastUtil.show(this@UbahDataLaporActivity, "Gagal memuat data")
    }

    override fun disableView() {
        ubah_data_lapor_submit.enable(false)
        data_lapor_container.enable(false)
    }

    override fun enableView() {
        ubah_data_lapor_submit.enable(true)
        data_lapor_container.enable(true)
    }

    override fun showSuccessUpdateDataLaporAlert() {
        ToastUtil.show(this@UbahDataLaporActivity, "Berhasil memperbarui data")
    }

    override fun showFailedUpdateDataLaporAlert() {
        ToastUtil.show(this@UbahDataLaporActivity, "Gagal memperbarui data")
    }

    override fun finishActivity() {
        setResult(Activity.RESULT_OK)
        finish()
    }
}
