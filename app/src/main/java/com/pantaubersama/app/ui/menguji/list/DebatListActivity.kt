package com.pantaubersama.app.ui.menguji.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.debat.Challenge
import com.pantaubersama.app.data.model.debat.DebatHeader
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.menguji.adapter.DebatListAdapter
import com.pantaubersama.app.utils.PantauConstants.Debat
import com.pantaubersama.app.utils.extensions.enableLottie
import com.pantaubersama.app.utils.extensions.unSyncLazy
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.layout_common_recyclerview.*
import kotlinx.android.synthetic.main.layout_empty_state.*
import kotlinx.android.synthetic.main.layout_fail_state.*
import kotlinx.android.synthetic.main.layout_loading_state.*
import javax.inject.Inject

class DebatListActivity : BaseActivity<DebatListPresenter>(), DebatListView {

    @Inject
    override lateinit var presenter: DebatListPresenter

    private val title by unSyncLazy { intent.getStringExtra(Debat.EXTRA_TITLE) }

    private val adapter by unSyncLazy { DebatListAdapter(supportFragmentManager) }

    override fun statusBarColor(): Int? = R.color.white

    override fun setLayout(): Int = R.layout.activity_debat_list

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, title, R.color.white, 4f)

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = adapter
        recycler_view.isNestedScrollingEnabled = true

        swipe_refresh.setOnRefreshListener {
            swipe_refresh.isRefreshing = false
            presenter.getChallenges(title)
        }

        setupHeader()

        presenter.getChallenges(title)
    }

    private fun setupHeader() {
        val text = when (title) {
            Debat.Title.PUBLIK_LIVE_NOW -> "Ini daftar debat yang sedang berlangsung.\nYuk, pantau bersama!"
            Debat.Title.PUBLIK_COMING_SOON -> "Jangan lewatkan daftar debat yang akan segera berlangsung.\nCatat jadwalnya, yaa."
            Debat.Title.PUBLIK_DONE -> "Berikan komentar dan appresiasi pada debat-debat yang sudah selesai.\nDaftarnya ada di bawah ini:"
            Debat.Title.PUBLIK_CHALLENGE -> "Daftar Open Challenge yang bisa diikuti. Pilih debat mana yang kamu ingin ambil tantangannya. Be truthful and gentle! ;)"
            Debat.Title.PERSONAL_CHALLENGE_IN_PROGRESS -> "Daftar tantangan yang perlu respon dan perlu konfirmasi ditampilkan semua disini. Jangan sampai terlewatkan, yaa."
            Debat.Title.PERSONAL_COMING_SOON -> "Jangan lewatkan daftar debat yang akan segera berlangsung.\nCatat jadwalnya, yaa."
            Debat.Title.PERSONAL_DONE -> "Berikan komentar dan appresiasi pada debat-debat yang sudah selesai.\nDaftarnya ada di bawah ini:"
            Debat.Title.PERSONAL_CHALLENGE -> "Daftar tantangan yang kamu buat, yang kamu ikuti dan tantangan dari orang lain buat kamu ditampilkan semua di sini."
            else -> ""
        }
        adapter.addItem(DebatHeader(text), 0)
    }

    override fun showChallenge(list: List<Challenge>) {
        view_empty_state.enableLottie(list.isEmpty(), lottie_empty_state)
        recycler_view.visibleIf(list.isNotEmpty())
        adapter.setDatas(list)
        setupHeader()
    }

    override fun showLoading() {
        lottie_loading.enableLottie(true, lottie_loading)
        view_empty_state.enableLottie(false, lottie_empty_state)
        view_fail_state.enableLottie(false, lottie_fail_state)
        recycler_view.visibleIf(false)
    }

    override fun dismissLoading() {
        recycler_view.visibleIf(true)
        lottie_loading.enableLottie(false, lottie_loading)
    }

    override fun showError(throwable: Throwable) {
        super.showError(throwable)
        recycler_view.visibleIf(false)
        view_fail_state.enableLottie(true, lottie_fail_state)
    }

    companion object {
        fun start(context: Context, title: String) {
            context.startActivity(Intent(context, DebatListActivity::class.java)
                .putExtra(Debat.EXTRA_TITLE, title))
        }
    }
}
