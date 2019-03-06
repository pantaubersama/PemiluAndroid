package com.pantaubersama.app.ui.wordstadium.challenge.direct

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.CompoundButton
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.data.model.wordstadium.BidangKajian
import com.pantaubersama.app.data.model.wordstadium.Challenge
import com.pantaubersama.app.data.model.wordstadium.LawanDebat
import com.pantaubersama.app.data.model.wordstadium.OEmbedLink
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.wordstadium.InfoDialog
import com.pantaubersama.app.ui.wordstadium.challenge.CreateChallengeActivity
import com.pantaubersama.app.ui.wordstadium.challenge.open.BidangKajianDialog
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.enable
import com.pantaubersama.app.utils.extensions.loadUrl
import kotlinx.android.synthetic.main.activity_direct_challenge.*
import kotlinx.android.synthetic.main.close_challenge.view.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class DirectChallengeActivity : BaseActivity<DirectChallengePresenter>(), DirectChallengeView {

    var mYear: Int = 0
    var mMonth: Int = 0
    var mDay: Int = 0
    var mHour: Int = 0
    var mMinute: Int = 0
    var mDateString: String? = null
    var mTimeString: String? = null
    var mLink: String = ""
    var oEmbedLink: OEmbedLink? = null
    var lawanId: String? = null
    var lawanUserName: String? = null
    var lawanName: String? = null
    var lawanAvatar: String? = null

    val SYMBOLIC_USER = 1
    val TWITTER_USER = 2
    var userType = SYMBOLIC_USER

    private lateinit var adapter: LawanDebatAdapter

    @Inject
    override lateinit var presenter: DirectChallengePresenter

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun setLayout(): Int {
        return R.layout.activity_direct_challenge
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, getString(R.string.title_direct_challenge), R.color.white, 4f)
        presenter.getUserProfile()
        actionClick()
        bidangKajianActive()

        link_webview.webViewClient = MyWebViewClient()
    }

    override fun showLoading() {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showProfile(profile: Profile) {
        direct_challenge_avatar.loadUrl(profile.avatar?.medium?.url, R.drawable.ic_avatar_placeholder)
        direct_challenge_name.text = profile.fullName
        direct_challenge_username.text = profile.username
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
        info_lawan_debat.setOnClickListener {
            val infoDialog = InfoDialog.newInstance(R.layout.info_lawan_debat)
            infoDialog.show(supportFragmentManager, "info-lawan_debat")
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
            val alertDialog = AlertDialog.Builder(this@DirectChallengeActivity)
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
                presenter.getConvertLink(url)
            }
        }

        radio_symbolic.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(cb: CompoundButton?, isChecked: Boolean) {
                if (isChecked) {
                    radio_twitter.isChecked = false
                    radio_symbolic.isChecked = true
                    et_username_twitter.isEnabled = false
                    et_username_twitter.setText("")
                    et_username_symbolic.isEnabled = true
                    ll_user_symbolic.visibility = View.GONE
                    ll_form_symbolic.visibility = View.VISIBLE
                    ll_user_twitter.visibility = View.GONE
                    ll_form_twitter.visibility = View.VISIBLE
                    userType = SYMBOLIC_USER
                }
            }
        })

        radio_twitter.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(cb: CompoundButton?, isChecked: Boolean) {
                if (isChecked) {
                    radio_twitter.isChecked = true
                    radio_symbolic.isChecked = false
                    et_username_symbolic.isEnabled = false
                    et_username_symbolic.setText("")
                    et_username_twitter.isEnabled = true
                    ll_user_symbolic.visibility = View.GONE
                    ll_form_symbolic.visibility = View.VISIBLE
                    ll_user_twitter.visibility = View.GONE
                    ll_form_twitter.visibility = View.VISIBLE
                    userType = TWITTER_USER
                }
            }
        })

        setupPeopleList()

        et_username_symbolic.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text!!.length > 0) {
                    presenter.searchPerson(text.toString(), 1, 5)
                } else {
                    bidangKajianActive()
                    bidangKajianDone()
                    pernyataanDone()
                    ll_date_time.visibility = View.GONE
                    ll_saldo_waktu_container.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(textString: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(textString: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        et_username_twitter.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text!!.length > 0) {
                    presenter.searchLawanDebat(text.toString(), 1, 5)
                } else {
                    bidangKajianActive()
                    bidangKajianDone()
                    pernyataanDone()
                    ll_date_time.visibility = View.GONE
                    ll_saldo_waktu_container.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(textString: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(textString: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        ll_date.setOnClickListener {
            val c = Calendar.getInstance()
            mYear = c.get(Calendar.YEAR)
            mMonth = c.get(Calendar.MONTH)
            mDay = c.get(Calendar.DAY_OF_MONTH)

            val mDays = arrayOf("Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu")

            val datePickerDialog = DatePickerDialog(this,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        var calendar = Calendar.getInstance()
                        calendar.set(year, monthOfYear, dayOfMonth)
                        val date = SimpleDateFormat("dd-MM-yyyy").format(calendar.time)
                        val dayName = mDays[calendar.get(Calendar.DAY_OF_WEEK) - 1]
                        tv_date.text = "$dayName, $date"
                        mDateString = SimpleDateFormat("dd-MM-yyyy").format(calendar.time)
                    }, mYear, mMonth, mDay)

            datePickerDialog.show()
        }

        ll_time.setOnClickListener {
            val c = Calendar.getInstance()
            mHour = c.get(Calendar.HOUR_OF_DAY)
            mMinute = c.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(this,
                    TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                        tv_time.text = "$hourOfDay.$minute"
                        dateTimeDone()
                        mTimeString = "$hourOfDay:$minute"
                    }, mHour, mMinute, false)

            timePickerDialog.show()
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

        direct_challenge_next.setOnClickListener {
            val challenge = Challenge(
                    bid_kajian.text.toString(),
                    et_pernyataan.text.toString(),
                    mLink,
                    tv_date.text.toString(),
                    tv_time.text.toString(),
                    et_pilih_saldo_waktu.text.toString().toInt(),
                    lawanId,
                    lawanUserName,
                    lawanName,
                    lawanAvatar
            )

            val intent = Intent(this, PreviewChallengeActivity::class.java)
            intent.putExtra("challenge", challenge)
            intent.putExtra("date", mDateString + " " + mTimeString)
            intent.putExtra("link", oEmbedLink)
            startActivityForResult(intent, CreateChallengeActivity.DIRECT_CHALLENGE)
        }
    }

    fun bidangKajianActive() {
        direct_challenge_next.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_dark_1))
        direct_challenge_next.enable(false)

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

        ll_lawan_debat_container.visibility = View.VISIBLE
        ll_lawan_debat.visibility = View.GONE
        info_lawan_debat.visibility = View.GONE
    }

    fun pernyataanDone() {
        check_pernyataan.setImageResource(R.drawable.check_done)
        line_pernyataan.setBackgroundColor(ContextCompat.getColor(this, R.color.orange_2))

        ll_lawan_debat.visibility = View.VISIBLE
        info_lawan_debat.visibility = View.VISIBLE
        check_lawan_debat.setImageResource(R.drawable.check_active)

        ll_date_time_continer.visibility = View.VISIBLE
        ll_date_time.visibility = View.GONE
        info_date_time.visibility = View.GONE
    }

    fun lawanDebatDone() {
        check_lawan_debat.setImageResource(R.drawable.check_done)
        line_lawan_debat.setBackgroundColor(ContextCompat.getColor(this, R.color.orange_2))

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

    fun saldoWaktuActive() {
        check_saldo_waktu.setImageResource(R.drawable.check_active)
        line_saldo_waktu.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_dark_1))

        direct_challenge_next.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_dark_1))
        direct_challenge_next.enable(false)
    }

    fun saldoWaktuDone() {
        check_saldo_waktu.setImageResource(R.drawable.check_done)
        line_saldo_waktu.setBackgroundColor(ContextCompat.getColor(this, R.color.orange_2))

        direct_challenge_next.setBackgroundColor(ContextCompat.getColor(this, R.color.orange_2))
        direct_challenge_next.enable(true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CreateChallengeActivity.DIRECT_CHALLENGE && resultCode == Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    fun previewLink(url: String?) {
        if (url != null && url.length > 0) {
            ll_webview.visibility = View.VISIBLE
            link_webview.settings.loadsImagesAutomatically = true
            link_webview.settings.javaScriptEnabled = true
            link_webview.getSettings().setAppCacheEnabled(true)
            link_webview.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
            link_webview.loadDataWithBaseURL("https://twitter.com", url.toString(), "text/html", "utf-8", "")
            link_pernyataan.visibility = View.GONE
        }
        link_close.setOnClickListener {
            ll_webview.visibility = View.GONE
            link_pernyataan.visibility = View.VISIBLE
        }
    }

    class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            view?.loadUrl(request?.url.toString())
            return true
        }
    }

    override fun onSuccessConvertLink(oEmbedLink: OEmbedLink) {
        link_source.text = oEmbedLink.url
        previewLink(oEmbedLink.html)
        this.oEmbedLink = oEmbedLink
    }

    override fun bindData(users: MutableList<LawanDebat>) {
        recycler_search_user.visibility = View.VISIBLE
        adapter.setDatas(users)
        if (users.size > 0)
            if (userType == SYMBOLIC_USER) {
                ll_lawan_debat_twitter.visibility = View.GONE
            } else {
                ll_lawan_debat_twitter.visibility = View.VISIBLE
            }
    }

    override fun showEmptyData() {
        recycler_search_user.visibility = View.GONE
    }

    override fun showFailedGetDataAlert() {
        ToastUtil.show(this@DirectChallengeActivity, "Gagal memuat data")
    }

    private fun setupPeopleList() {
        recycler_search_user.layoutManager = LinearLayoutManager(this@DirectChallengeActivity)
        adapter = LawanDebatAdapter()
        adapter.listener = object : LawanDebatAdapter.Listener {
            override fun onClickItem(lawanDebat: LawanDebat) {
                lawanDebatDone()
                recycler_search_user.visibility = View.GONE
                ll_lawan_debat_twitter.visibility = View.VISIBLE
                if (userType == TWITTER_USER) {
                    ll_form_twitter.visibility = View.GONE
                    ll_user_twitter.visibility = View.VISIBLE
                    ava_lawan_twitter.loadUrl(lawanDebat.profileImageUrl)
                    name_lawan_twitter.text = lawanDebat.name
                    username_lawan_twitter.text = lawanDebat.screenName
                    lawanId = lawanDebat.id
                    lawanUserName = lawanDebat.screenName
                    lawanName = lawanDebat.name
                    lawanAvatar = lawanDebat.profileImageUrl
                } else {
                    ll_form_symbolic.visibility = View.GONE
                    ll_user_symbolic.visibility = View.VISIBLE
                    ava_lawan_symbolic.loadUrl(lawanDebat.avatar.url)
                    name_lawan_symbolic.text = lawanDebat.fullName
                    username_lawan_symbolic.text = lawanDebat.username
                    lawanId = lawanDebat.id
                    lawanUserName = lawanDebat.username
                    lawanName = lawanDebat.fullName
                    lawanAvatar = lawanDebat.avatar.url
                }
            }
        }
        recycler_search_user.adapter = adapter
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
