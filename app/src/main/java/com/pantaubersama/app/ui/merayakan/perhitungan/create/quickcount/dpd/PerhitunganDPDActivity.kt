package com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.dpd

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.kandidat.CandidateData
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.merayakan.perhitungan.create.quickcount.DPRCandidateAdapter
import kotlinx.android.synthetic.main.activity_perhitungan_dpd.*
import javax.inject.Inject

class PerhitunganDPDActivity : BaseActivity<PerhitunganDPDPresenter>(), PerhitunganDPDView {
    private lateinit var adapter: DPRCandidateAdapter

    @Inject
    override lateinit var presenter: PerhitunganDPDPresenter

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun setLayout(): Int {
        return R.layout.activity_perhitungan_dpd
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, "DPR RI", R.color.white, 4f)
        setupDPDList()
        presenter.getDPDData()
        golput_inc_button.setOnClickListener {
            val count = golput_count_field.text.toString().toInt()
            golput_count_field.setText(count.plus(1).toString())
        }
        save_button.setOnClickListener {
            finish()
        }
    }

    private fun setupDPDList() {
        adapter = DPRCandidateAdapter()
        dpd_list.layoutManager = LinearLayoutManager(this@PerhitunganDPDActivity)
        dpd_list.adapter = adapter
    }

    override fun bindData(candidates: MutableList<CandidateData>) {
        adapter.setDatas(candidates as MutableList<ItemModel>)
    }

    override fun showLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_undo, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.undo_action -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
