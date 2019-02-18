package com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.dprdkabupaten

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.partai.PoliticalParty
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.DPRPartaiAdapter
import kotlinx.android.synthetic.main.activity_perhitungan_dprdkabupaten.*
import javax.inject.Inject

class PerhitunganDPRDKabupatenActivity : BaseActivity<PerhitunganDPRDKabupatenPresenter>(), PerhitunganDPRDKabupatenView {
    private lateinit var adapter: DPRPartaiAdapter

    @Inject
    override lateinit var presenter: PerhitunganDPRDKabupatenPresenter

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun setLayout(): Int {
        return R.layout.activity_perhitungan_dprdkabupaten
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, "DPRD KABUPATEN", R.color.white, 4f)
        setupDPRDKabupatenList()
        presenter.getDPRRProvinsiData()
        golput_inc_button.setOnClickListener {
            val count = golput_count_field.text.toString().toInt()
            golput_count_field.setText(count.plus(1).toString())
        }
        save_button.setOnClickListener {
            finish()
        }
    }

    private fun setupDPRDKabupatenList() {
        adapter = DPRPartaiAdapter()
        dpr_list.layoutManager = LinearLayoutManager(this)
        dpr_list.adapter = adapter
    }

    override fun bindData(parties: MutableList<PoliticalParty>) {
        adapter.setDatas(parties as MutableList<ItemModel>)
    }

    override fun showLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
