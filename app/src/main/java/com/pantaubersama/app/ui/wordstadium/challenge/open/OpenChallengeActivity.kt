package com.pantaubersama.app.ui.wordstadium.challenge.open

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.urlpreview.UrlItem
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.data.model.wordstadium.BidangKajian
import com.pantaubersama.app.data.model.wordstadium.Challenge
import com.pantaubersama.app.data.model.wordstadium.OEmbedLink
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.widget.PreviewWebViewClient
import com.pantaubersama.app.ui.wordstadium.InfoDialog
import com.pantaubersama.app.ui.wordstadium.challenge.CreateChallengeActivity
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.enable
import com.pantaubersama.app.utils.extensions.loadUrl
import com.pantaubersama.app.utils.extensions.visibleIf
import com.pantaubersama.app.utils.previewTweet
import com.pantaubersama.app.utils.previewUrl
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import kotlinx.android.synthetic.main.activity_open_challenge.*
import kotlinx.android.synthetic.main.close_challenge.view.*
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject

class OpenChallengeActivity : BaseActivity<OpenChallengePresenter>(), OpenChallengeView {

    var mYear: Int = 0
    var mMonth: Int = 0
    var mDay: Int = 0
    var mHour: Int = 0
    var mMinute: Int = 0
    var mDateString: String? = null
    var mTimeString: String? = null
    var mLink: String = ""
    var oEmbedLink: OEmbedLink? = null

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
        open_challenge_avatar.loadUrl(profile.avatar?.medium?.url, R.drawable.ic_avatar_placeholder)
        open_challenge_name.text = profile.fullName
        open_challenge_username.text = profile.username
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
                if (text!!.length > 6) {
                    pernyataanDone()
                } else {
                    bidangKajianActive()
                    bidangKajianDone()
                    ll_date_time.visibility = View.GONE
                    ll_saldo_waktu_container.visibility = View.GONE
                }
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
                val url = text.text.toString()
                mLink = url
                dialog.dismiss()
                presenter.getTweetPreview(url)
            }
        }

        ll_date.setOnClickListener {
            val c = Calendar.getInstance()
            mYear = c.get(Calendar.YEAR)
            mMonth = c.get(Calendar.MONTH)
            mDay = c.get(Calendar.DAY_OF_MONTH)

            val mDays = arrayOf("Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu")

            val datePickerDialog = DatePickerDialog(this,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        val calendar = Calendar.getInstance()
                        calendar.set(year, monthOfYear, dayOfMonth)
                        val date = SimpleDateFormat("dd-MM-yyyy").format(calendar.time)
                        val dayName = mDays[calendar.get(Calendar.DAY_OF_WEEK) - 1]
                        tv_date.text = "$dayName, $date"
                        mDateString = SimpleDateFormat("dd-MM-yyyy").format(calendar.time)

                        if (calendar.time.toString() == Calendar.getInstance().time.toString() && tv_time.text.isNotBlank()) {
                            tv_time.text = ""
                            ll_saldo_waktu_container.visibility = View.GONE
                            open_challenge_next.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_dark_1))
                            open_challenge_next.enable(false)
                        }
                    }, mYear, mMonth, mDay)

            datePickerDialog.datePicker.minDate = Calendar.getInstance().timeInMillis
            datePickerDialog.show()
        }

        ll_time.setOnClickListener {
            if (mDateString?.isNotEmpty() == true) {
                val c = Calendar.getInstance()
                mHour = c.get(Calendar.HOUR_OF_DAY)
                mMinute = c.get(Calendar.MINUTE)

                val timePickerDialog = TimePickerDialog.newInstance({ view, hourOfDay, minute, second ->
                    var hour = hourOfDay.toString()
                    var mnt = minute.toString()
                    if (hourOfDay < 10) hour = "0" + hour
                    if (minute < 10) mnt = "0" + mnt
                    tv_time.text = "$hour.$mnt"
                    dateTimeDone()
                    mTimeString = "$hour:$mnt"
                }, mHour, mMinute, true)

                if (mDateString == SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().time)) {
                    timePickerDialog.setMinTime(mHour, mMinute, 0)
                }

                timePickerDialog.show(supportFragmentManager, "TimePickerDialog")
            } else {
                ToastUtil.show(this, "Pilih tanggal dahulu")
            }
        }

        et_pilih_saldo_waktu.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text!!.length > 0) {
                    saldoWaktuDone()
                } else {
                    saldoWaktuActive()
                }
            }

            override fun beforeTextChanged(textString: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(textString: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        open_challenge_next.setOnClickListener {
            val challenge = Challenge(
                    bid_kajian.text.toString(),
                    et_pernyataan.text.toString(),
                    mLink,
                    tv_date.text.toString(),
                    tv_time.text.toString(),
                    et_pilih_saldo_waktu.text.toString().toInt()
            )

            val intent = Intent(this, PromoteChallengeActivity::class.java)
            intent.putExtra("challenge", challenge)
            intent.putExtra("date", mDateString + " " + mTimeString)
            TODO("GANTI 'link' DENGAN TIPE STRING")
            intent.putExtra("link", oEmbedLink)
            startActivityForResult(intent, CreateChallengeActivity.OPEN_CHALLENGE)
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

        ll_saldo_waktu_container.visibility = View.VISIBLE
        ll_saldo_waktu.visibility = View.VISIBLE
        info_saldo_waktu.visibility = View.VISIBLE
        check_saldo_waktu.setImageResource(R.drawable.check_active)
    }

    fun saldoWaktuActive() {
        check_saldo_waktu.setImageResource(R.drawable.check_active)
        line_saldo_waktu.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_dark_1))

        open_challenge_next.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_dark_1))
        open_challenge_next.enable(false)
    }

    fun saldoWaktuDone() {
        check_saldo_waktu.setImageResource(R.drawable.check_done)
        line_saldo_waktu.setBackgroundColor(ContextCompat.getColor(this, R.color.orange_2))
        open_challenge_next.setBackgroundColor(ContextCompat.getColor(this, R.color.orange_2))
        open_challenge_next.enable(true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CreateChallengeActivity.OPEN_CHALLENGE && resultCode == Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    fun previewLink(url: String?) {
        if (url != null && url.length > 0) {
            ll_webview.visibility = View.VISIBLE
            ll_webview.previewTweet(url)
            link_pernyataan.visibility = View.GONE
        }
        link_close.setOnClickListener {
            ll_webview.visibility = View.GONE
            link_pernyataan.visibility = View.VISIBLE
            if (ll_webview.childCount > 1) {
                ll_webview.removeViewAt(1)
            }
        }
    }

    override fun onSuccessTweetPreview(oEmbedLink: OEmbedLink) {
        link_source.text = oEmbedLink.url
        previewLink(oEmbedLink.html)
        this.oEmbedLink = oEmbedLink
    }

    override fun showLoadingUrlPreview() {
        progress_bar_url_preview.visibleIf(true)
    }

    override fun dismissLoadingUrlPreview() {
        progress_bar_url_preview.visibleIf(false)
    }

    override fun showUrlPreview(urlItem: UrlItem) {
        link_source.text = urlItem.sourceUrl
        ll_webview.previewUrl(urlItem)
        ll_webview.visibility = View.VISIBLE
        link_pernyataan.visibility = View.GONE
        link_close.setOnClickListener {
            ll_webview.visibility = View.GONE
            link_pernyataan.visibility = View.VISIBLE
            if (ll_webview.childCount > 1) {
                ll_webview.removeViewAt(1)
            }
        }
    }

    override fun onErrorUrlPreview(t: Throwable) {
        Timber.e(t)
        ToastUtil.show(this, getString(R.string.link_tidak_valid))
    }

    fun back() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.close_challenge, null)
        val mDialogBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
        val mAlertDalog = mDialogBuilder.show()
        mDialogView.yes_button.setOnClickListener {
            mAlertDalog.dismiss()
            finish()
            setResult(Activity.RESULT_OK)
        }
        mDialogView.no_button.setOnClickListener {
            mAlertDalog.dismiss()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            back()
        }
        return true
    }

    override fun onBackPressed() {
        back()
    }
}
