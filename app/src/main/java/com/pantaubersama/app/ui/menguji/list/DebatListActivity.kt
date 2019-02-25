package com.pantaubersama.app.ui.menguji.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.debat.DebatHeader
import com.pantaubersama.app.data.model.debat.DebatItem
import com.pantaubersama.app.ui.menguji.adapter.DebatListAdapter
import com.pantaubersama.app.utils.PantauConstants.Debat
import com.pantaubersama.app.utils.extensions.unSyncLazy
import kotlinx.android.synthetic.main.layout_common_recyclerview.*

class DebatListActivity : BaseActivity<DebatListPresenter>(), DebatListView {

    override var presenter: DebatListPresenter = DebatListPresenter()

    private val title by unSyncLazy { intent.getStringExtra(Debat.EXTRA_TITLE) }

    private val adapter by unSyncLazy { DebatListAdapter(supportFragmentManager) }

    override fun statusBarColor(): Int? = R.color.white

    override fun setLayout(): Int = R.layout.activity_debat_list

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, title, R.color.white, 4f)

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = adapter
        recycler_view.isNestedScrollingEnabled = true

        setupHeader()

        presenter.getDebatItems(title)
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

    override fun showDebatItems(list: List<DebatItem>) {
        adapter.addData(list)
    }

    override fun showLoading() {
    }

    override fun dismissLoading() {
    }

    companion object {
        fun start(context: Context, title: String) {
            context.startActivity(Intent(context, DebatListActivity::class.java)
                .putExtra(Debat.EXTRA_TITLE, title))
        }
    }
}
