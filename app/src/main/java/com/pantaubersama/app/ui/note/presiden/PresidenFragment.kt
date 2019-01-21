package com.pantaubersama.app.ui.note.presiden

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.data.model.capres.PaslonData
import com.pantaubersama.app.data.model.kuis.KuisUserResult
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.color
import com.pantaubersama.app.utils.extensions.loadUrl
import com.pantaubersama.app.utils.extensions.visibleIf
import com.pantaubersama.app.utils.spannable
import kotlinx.android.synthetic.main.fragment_presiden.*
import javax.inject.Inject

class PresidenFragment : BaseFragment<PaslonPresenter>(), PaslonView {
    private lateinit var layoutManager: StaggeredGridLayoutManager
    private lateinit var paslonAdapter: PaslonAdapter

    @Inject
    override lateinit var presenter: PaslonPresenter
    var listener: Listener? = null

    override fun setLayout(): Int {
        return R.layout.fragment_presiden
    }

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun initView(view: View) {
        setupRecyclerView(view)
        setData()
        presenter.getUserProfile()
        presenter.getMyTendency()
    }

    private fun setData() {
        val paslons: MutableList<PaslonData> = ArrayList()
        paslons.add(
            PaslonData(
                1,
                "Jokowi - Ma'ruf",
                R.drawable.ava_calon_1,
                "Joko Widodo",
                "Ma'ruf Amin")
        )
        paslons.add(
            PaslonData(
                2,
                "Prabowo - Sandi",
                R.drawable.ava_calon_2,
                "Prabowo Subiyanto",
                "Sandiaga Salahudin Uno")
        )
        paslons.add(
            PaslonData(
                3,
                "Belum menentukan pilihan")
        )
        paslonAdapter.setDatas(paslons)
    }

    private fun setupRecyclerView(view: View) {
        layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        paslonAdapter = PaslonAdapter()
        paslonAdapter.listener = object : PaslonAdapter.Listener {
            override fun onSelectItem(paslonData: PaslonData) {
                selected_paslon.text = paslonData.paslonName
                listener?.onPaslonSelect(paslonData)
            }
        }
        presiden_recycler_view_calon.layoutManager = layoutManager
        presiden_recycler_view_calon.adapter = paslonAdapter
    }

    companion object {
        fun newInstance(): PresidenFragment {
            return PresidenFragment()
        }
    }

    override fun showLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun bindUserProfile(profile: Profile) {
        if (profile.votePreference != 0) {
            paslonAdapter.setSelectedData(paslonAdapter.get(profile.votePreference - 1) as PaslonData)
        }
    }

    override fun showFailedGetMyTendencyAlert() {
        ToastUtil.show(requireContext(), "Gagal memuat kecenderungan")
    }

    @SuppressLint("SetTextI18n")
    override fun bindMyTendency(tendency: KuisUserResult, name: String) {
        ll_kuis_result.visibleIf(tendency.meta.finished != 0)
        presiden_total_kuis.text = spannable {
            +"Total Kecenderungan ${tendency.meta.finished} Dari ${tendency.meta.total} Kuis,\n"
            textColor(color(R.color.black_3)) { +name }
            +" lebih suka jawaban dari Paslon no ${tendency.team.id}"
        }.toCharSequence()
        paslon_avatar.loadUrl(tendency.team.avatar)
        total_tendency.text = "%.2f%%".format(tendency.percentage)
        paslon_name.text = tendency.team.title
    }

    interface Listener {
        fun onPaslonSelect(paslonData: PaslonData)
    }
}
