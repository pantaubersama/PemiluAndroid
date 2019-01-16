package com.pantaubersama.app.ui.note

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.capres.PaslonData
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.note.presiden.PresidenFragment
import com.pantaubersama.app.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_catatan_pilihanku.*
import javax.inject.Inject

class CatatanPilihanActivity : BaseActivity<CatatanPilihanPresenter>(), CatatanPilihanView {
    private var paslonSelected: Int? = null

    @Inject
    override lateinit var presenter: CatatanPilihanPresenter

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    val presidenFragment = PresidenFragment.newInstance()

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun fetchIntentExtra() {
        // ok
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(false, getString(R.string.title_catatan_pilihanku), R.color.white, 4f)
        presidenFragment.listener = object : PresidenFragment.Listener {
            override fun onPaslonSelect(paslonData: PaslonData) {
                paslonSelected = paslonData.paslonNumber
            }
        }
        replaceFragment(presidenFragment)
        pilpress_tab.setOnClickListener {
            replaceFragment(presidenFragment)
        }
        catatan_pilihanku_ok.setOnClickListener {
            paslonSelected?.let { presenter.submitCatatanku(it) }
        }
    }

    override fun setLayout(): Int {
        return R.layout.activity_catatan_pilihanku
    }

    override fun showLoading() {
        showProgressDialog("Menyimpan pilihan")
    }

    override fun dismissLoading() {
        dismissProgressDialog()
    }

    private fun replaceFragment(fragment: Fragment) {
        // sebelum tab partai aktif
        supportFragmentManager.beginTransaction()
                .replace(R.id.catatan_pilihanku_container, fragment)
                .commit()
    }

    override fun showSuccessSubmitCatatanAlert() {
        ToastUtil.show(this@CatatanPilihanActivity, "pilihan tersimpan")
    }

    override fun finishActivity() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun showFailedSubmitCatatanAlert() {
        ToastUtil.show(this@CatatanPilihanActivity, "Gagal menyimpan pilihan")
    }
}
