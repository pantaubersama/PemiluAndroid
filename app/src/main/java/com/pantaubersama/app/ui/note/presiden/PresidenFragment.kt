package com.pantaubersama.app.ui.note.presiden

import android.view.View
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.data.model.capres.PaslonData
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.di.component.ActivityComponent
import kotlinx.android.synthetic.main.fragment_presiden.view.*
import javax.inject.Inject

class PresidenFragment : BaseFragment<PaslonPresenter>(), PaslonView {
    private lateinit var layoutManager: StaggeredGridLayoutManager
    private lateinit var paslonAdapter: PaslonAdapter

    @Inject override lateinit var presenter: PaslonPresenter

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun initView(view: View) {
        setupRecyclerView(view)
        setData()
        presenter.getUserProfile()
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
                view.selected_paslon.text = paslonData.paslonName
            }
        }
        view.presiden_recycler_view_calon.layoutManager = layoutManager
        view.presiden_recycler_view_calon.adapter = paslonAdapter
    }

    override fun setLayout(): Int {
        return R.layout.fragment_presiden
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
        paslonAdapter.setSelectedData(paslonAdapter.get(profile.votePreference-1) as PaslonData)
    }
}
