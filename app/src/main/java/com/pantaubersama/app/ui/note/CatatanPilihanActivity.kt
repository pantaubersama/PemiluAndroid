package com.pantaubersama.app.ui.note

import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity

class CatatanPilihanActivity : BaseActivity<CatatanPilihanPresenter>(), CatatanPilihanView {

    val presidenFragment = PresidenFragment.newInstance()

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun fetchIntentExtra() {
        // ok
    }

    override fun initPresenter(): CatatanPilihanPresenter? {
        return CatatanPilihanPresenter()
    }

    override fun setupUI() {
        setupToolbar(false, getString(R.string.title_catatan_pilihanku), R.color.white, 4f)
        showFragment()
    }

    override fun setLayout(): Int {
        return R.layout.activity_catatan_pilihanku
    }

    override fun showLoading() {
        // show
    }

    override fun dismissLoading() {
        // dismiss
    }

    private fun showFragment() {
        // sebelum tab partai aktif
        supportFragmentManager.beginTransaction()
                .replace(R.id.catatan_pilihanku_container, presidenFragment)
                .commit()
    }
}
