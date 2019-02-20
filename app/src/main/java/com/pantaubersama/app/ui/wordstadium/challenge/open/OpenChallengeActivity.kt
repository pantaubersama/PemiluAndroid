package com.pantaubersama.app.ui.wordstadium.challenge.open

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.data.model.wordstadium.BidangKajian
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.wordstadium.InfoDialog
import com.pantaubersama.app.utils.extensions.enable
import com.pantaubersama.app.utils.extensions.loadUrl
import kotlinx.android.synthetic.main.activity_open_challenge.*
import javax.inject.Inject

class OpenChallengeActivity : BaseActivity<OpenChallengePresenter>(), OpenChallengeView {

    @Inject
    override lateinit var presenter: OpenChallengePresenter

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun setLayout(): Int {
        return R.layout.activity_open_challenge
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, getString(R.string.title_open_challenge), R.color.white, 4f)
        presenter.getUserProfile()
        actionClick()
        bidangKajianActive()
    }

    override fun showLoading() {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
        //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showProfile(profile: Profile) {
        open_challenge_avatar.loadUrl(profile.avatar.medium?.url, R.drawable.ic_avatar_placeholder)
        open_challenge_name.text = profile.name
        open_challenge_username.text = profile.username
    }

    override fun onSuccessOpenChallenge() {

    }

    fun actionClick() {
        info_bidang_kajian.setOnClickListener {
            val infoDialog = InfoDialog.newInstance(R.layout.info_bidang_kajian)
            infoDialog.show(supportFragmentManager, "info-bidang-kajian")
        }
        info_pernyataan.setOnClickListener {
            val infoDialog = InfoDialog.newInstance(R.layout.info_pernyataan)
            infoDialog.show(supportFragmentManager, "info-pernyataan")
        }
        info_date_time.setOnClickListener {
            val infoDialog = InfoDialog.newInstance(R.layout.info_date_time)
            infoDialog.show(supportFragmentManager, "info-date_time")
        }
        info_saldo_waktu.setOnClickListener {
            val infoDialog = InfoDialog.newInstance(R.layout.info_saldo_waktu)
            infoDialog.show(supportFragmentManager, "info-saldo_waktu")
        }

        pilih_bid_kajian.setOnClickListener {
            BidangKajianDialog.show(supportFragmentManager, object : BidangKajianDialog.DialogListener {
                override fun onClickItem(bidangKajian: BidangKajian) {
                    bid_kajian.text = bidangKajian.bidangKajian
                    pilih_bid_kajian.visibility = View.GONE
                    bid_kajian.visibility = View.VISIBLE
                    bidangKajianDone()
                }

            })
        }

        et_pernyataan.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {

            }

            override fun beforeTextChanged(textString: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(textString: CharSequence?, p1: Int, p2: Int, p3: Int) {
                var currentLength = textString?.length
                tv_max_character.text = "$currentLength/160"
            }

        })

        link_pernyataan.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this@OpenChallengeActivity)
            val view = LayoutInflater.from(this).inflate(R.layout.input_link, null)
            val btnBatal = view.findViewById<View>(R.id.input_link_batal)
            val btnOke = view.findViewById<View>(R.id.input_link_ok)
            val text: EditText = view.findViewById<View>(R.id.input_link_text) as EditText
            alertDialog.setView(view)
            val dialog = alertDialog.create()
            dialog.show()
            btnBatal.setOnClickListener {
                dialog.dismiss()
            }
            btnOke.setOnClickListener {
                val link = text.text.toString()
                dialog.dismiss()
            }
        }
    }

    fun bidangKajianActive() {
        open_challenge_next.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_dark_1))
        open_challenge_next.enable(false)

        pilih_bid_kajian.visibility = View.VISIBLE
        bid_kajian.visibility = View.GONE
        check_bid_kajian.setImageResource(R.drawable.check_active)
        check_bid_kajian.visibility = View.VISIBLE
        info_bidang_kajian.visibility = View.VISIBLE
        line_bid_kajian.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_dark_1))

        ll_pernyataan_container.visibility = View.VISIBLE
        check_pernyataan.setImageResource(R.drawable.check_inactive)
        check_pernyataan.visibility = View.VISIBLE
        info_pernyataan.visibility = View.GONE
        line_pernyataan.visibility = View.GONE
        ll_pernyataan.visibility = View.GONE
    }

    fun bidangKajianDone() {
        check_bid_kajian.setImageResource(R.drawable.check_done)
        line_bid_kajian.setBackgroundColor(ContextCompat.getColor(this, R.color.orange_2))
        pilih_bid_kajian.visibility = View.GONE
        bid_kajian.visibility = View.VISIBLE

        ll_pernyataan_container.visibility = View.VISIBLE
        ll_pernyataan.visibility = View.VISIBLE
        check_pernyataan.setImageResource(R.drawable.check_active)
        check_pernyataan.visibility = View.VISIBLE
        line_pernyataan.visibility = View.VISIBLE
        info_pernyataan.visibility = View.VISIBLE

        ll_date_time_continer.visibility = View.VISIBLE
        ll_date_time.visibility = View.GONE
        info_date_time.visibility = View.GONE
    }

    fun pernyataanDone() {
        check_pernyataan.setImageResource(R.drawable.check_done)
        line_pernyataan.setBackgroundColor(ContextCompat.getColor(this, R.color.orange_2))

        ll_date_time.visibility = View.VISIBLE
        info_date_time.visibility = View.VISIBLE
        check_date_time.setImageResource(R.drawable.check_active)

        ll_saldo_waktu_container.visibility = View.VISIBLE
        ll_saldo_waktu.visibility = View.GONE
        info_saldo_waktu.visibility = View.GONE
    }

    fun dateTimeDone() {
        check_date_time.setImageResource(R.drawable.check_done)
        line_date_time.setBackgroundColor(ContextCompat.getColor(this, R.color.orange_2))

        ll_saldo_waktu.visibility = View.VISIBLE
        info_saldo_waktu.visibility = View.VISIBLE
        check_saldo_waktu.setImageResource(R.drawable.check_active)
    }

    fun saldoWaktuDone() {
        check_saldo_waktu.setImageResource(R.drawable.check_done)
        line_saldo_waktu.setBackgroundColor(ContextCompat.getColor(this, R.color.orange_2))

        open_challenge_next.setBackgroundColor(ContextCompat.getColor(this, R.color.orange_2))
        open_challenge_next.enable(true)
    }
}
