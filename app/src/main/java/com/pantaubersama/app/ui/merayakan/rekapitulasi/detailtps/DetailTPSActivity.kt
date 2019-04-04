package com.pantaubersama.app.ui.merayakan.rekapitulasi.detailtps

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.rekapitulasi.Percentage
import com.pantaubersama.app.data.model.tps.TPS
import com.pantaubersama.app.data.model.tps.c1.C1Form
import com.pantaubersama.app.data.model.tps.image.Image
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.utils.PantauConstants.Merayakan.TPS_DATA
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.loadUrl
import kotlinx.android.synthetic.main.activity_detail_tps.*
import kotlinx.android.synthetic.main.rekapitulasi_images_layout.view.*
import javax.inject.Inject

class DetailTPSActivity : BaseActivity<DetailTPSPresenter>(), DetailTPSView {

    @Inject
    override lateinit var presenter: DetailTPSPresenter

    private var tps: TPS? = null

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun fetchIntentExtra() {
        tps = intent.getSerializableExtra(TPS_DATA) as TPS
    }

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun setLayout(): Int {
        return R.layout.activity_detail_tps
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, "TPS", R.color.white, 4f)
        bindTpsData()
        loadData()
        swipe_refresh.setOnRefreshListener {
            loadData()
        }
    }

    private fun bindTpsData() {
        tps?.let {
            user_avatar.loadUrl(it.user.avatar?.url, R.drawable.ic_avatar_placeholder)
            user_name.text = it.user.fullName
            user_cluster.text = it.user.cluster?.name
            tps_number.text = "TPS ${it.tps}"
            tps_address.text = "${it.province.name}, ${it.regency.name}, ${it.district.name}, ${it.village.name}"
        }
    }

    private fun loadData() {
        swipe_refresh.isRefreshing = false
        tps?.id?.let {
            tps?.village?.code?.let { it1 -> tps?.tps?.let { it2 -> presenter.getRekapitulasi(it, it1, it2) } }
        }
    }

    override fun showFailedGetRealCountAlert() {
        failed_alert.visibility = View.VISIBLE
    }

    override fun bindRealCount(rekapitulasi: Percentage) {
        paslon_1_percentage.text = String.format("%.2f", rekapitulasi.candidates?.get(0)?.percentage) + "%"
        paslon_1_votes_count.text = "${rekapitulasi.candidates?.get(0)?.totalVote} suara"
        paslon_2_percentage.text = String.format("%.2f", rekapitulasi.candidates?.get(1)?.percentage) + "%"
        paslon_2_votes_count.text = "${rekapitulasi.candidates?.get(1)?.totalVote} suara"
        invalid_vite_count.text = rekapitulasi.invalidVote?.total.toString()
        votes_count.text = rekapitulasi.totalVote.toString()
    }

    override fun showLoading() {
        progress_bar.visibility = View.VISIBLE
    }

    override fun dismissLoading() {
        progress_bar.visibility = View.GONE
    }

    override fun showFailedGetImagesAlert() {
        ToastUtil.show(this@DetailTPSActivity, "Gagal memuat dokumen gambar")
    }

    override fun bindImages(images: MutableList<Image>) {
        if (images.size != 0) {
            presiden_c1_container.visibility = View.VISIBLE
            images.forEach {
                val child = layoutInflater.inflate(R.layout.rekapitulasi_images_layout, null)
                child.c1_image.loadUrl(it.file.url, R.drawable.ic_avatar_placeholder)
                presiden_images_container.addView(child)
            }
        }
    }

    override fun showFailedGetC1SummaryAlert(message: String?) {
        if (message != "Belum ada") {
            ToastUtil.show(this@DetailTPSActivity, "Gagal memuat rekapitulasi C1")
        }
    }

    override fun bindC1Summary(c1Form: C1Form) {
        dpt_a3_kpu_l_count.text = c1Form.a3L.toString()
        dpt_a3_kpu_p_count.text = c1Form.a3P.toString()
        dpt_a3_kpu_l_p_count.text = (c1Form.a3L + c1Form.a3P).toString()
        dpt_b_a4_kpu_l_count.text = c1Form.a4L.toString()
        dpt_b_a4_kpu_p_count.text = c1Form.a4P.toString()
        dpt_b_a4_kpu_l_p_count.text = (c1Form.a4L + c1Form.a4P).toString()
        dpk_a_kpu_l_count.text = c1Form.aDpkL.toString()
        dpk_a_kpu_p_count.text = c1Form.aDpkP.toString()
        dpk_a_kpu_l_p_count.text = (c1Form.aDpkL + c1Form.aDpkP).toString()
        a1_a2_a3_l.text = (c1Form.a3L + c1Form.a4L + c1Form.aDpkL).toString()
        a1_a2_a3_p.text = (c1Form.a3P + c1Form.a4P + c1Form.aDpkP).toString()
        a1_a2_a3_l_p.text = ((c1Form.a3L + c1Form.a4L + c1Form.aDpkL) + (c1Form.a3P + c1Form.a4P + c1Form.aDpkP)).toString()
        dpt_c7_l_count.text = c1Form.c7DptL.toString()
        dpt_c7_p_count.text = c1Form.c7DptP.toString()
        dpt_c7_l_p_count.text = (c1Form.c7DptL + c1Form.c7DptP).toString()
        dpt_b_c7_l_count.text = c1Form.c7DptBL.toString()
        dpt_b_c7_p_count.text = c1Form.c7DptBP.toString()
        dpt_b_c7_l_p_count.text = (c1Form.c7DptBL + c1Form.c7DptBP).toString()
        dpk_c7_l_count.text = c1Form.c7DpkL.toString()
        dpk_c7_p_count.text = c1Form.c7DpkP.toString()
        dpk_c7_l_p_count.text = (c1Form.c7DpkL + c1Form.c7DpkP).toString()
        b1_b2_b3_l.text = (c1Form.c7DptL + c1Form.c7DptBL + c1Form.c7DpkL).toString()
        b1_b2_b3_p.text = (c1Form.c7DptP + c1Form.c7DptBP + c1Form.c7DpkP).toString()
        b1_b2_b3_l_p.text = ((c1Form.c7DptL + c1Form.c7DptBL + c1Form.c7DpkL) + (c1Form.c7DptP + c1Form.c7DptBP + c1Form.c7DpkP)).toString()
        disabilitas_l_count.text = c1Form.disabilitasTerdaftarL.toString()
        disabilitas_p_count.text = c1Form.disabilitasTerdaftarP.toString()
        disabilitas_l_p_count.text = (c1Form.disabilitasTerdaftarL + c1Form.disabilitasTerdaftarP).toString()
        disabilitas_l_voted_count.text = c1Form.disabilitasHakPilihL.toString()
        disabilitas_p_voted_count.text = c1Form.disabilitasHakPilihP.toString()
        disabilitas_l_p_voted_count.text = (c1Form.disabilitasHakPilihL + c1Form.disabilitasHakPilihP).toString()
        rejected_documents_count.text = c1Form.suratDikembalikan.toString()
        unused_documents_count.text = c1Form.suratTidakDigunakan.toString()
        used_documents_count.text = c1Form.suratDigunakan.toString()
        accepted_documents_count.text = (c1Form.suratDikembalikan + c1Form.suratTidakDigunakan + c1Form.suratDigunakan).toString()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_close, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.close_action -> {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun start(context: Context, item: TPS, requestCode: Int? = null) {
            val intent = Intent(context, DetailTPSActivity::class.java)
            intent.putExtra(TPS_DATA, item)
            if (requestCode != null) {
                (context as Activity).startActivityForResult(intent, requestCode)
            } else {
                context.startActivity(intent)
            }
        }
    }
}