package com.pantaubersama.app.ui.note.partai

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.data.model.partai.PoliticalParty
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.note.CatatanPilihanActivity
import kotlinx.android.synthetic.main.fragment_partai.*
import javax.inject.Inject

class PartaiFragment : BaseFragment<PartaiPresenter>(), PartaiView {

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var partaiAdapter: PartaiAdapter

    @Inject
    override lateinit var presenter: PartaiPresenter

    override fun initView(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        if (savedInstanceState != null) {
            partaiAdapter.setSelectedData(PoliticalParty(savedInstanceState.getString("selected_partai"),
                    null, null, null))
        } else {
            presenter.getUserProfile()
        }
        presenter.getPartai(1, 20)
    }

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setLayout(): Int {
        return R.layout.fragment_partai
    }

    override fun showLoading() {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
        //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun bindUserProfile(profile: Profile) {
        if (profile.politicalParty != null) {
            profile.politicalParty?.id?.let { PoliticalParty(it, null, null, null) }?.let { partaiAdapter.setSelectedData(it) }
        }
    }

    override fun showPartai(politicalParties: List<PoliticalParty>) {
        val parties: MutableList<PoliticalParty> = ArrayList()
        for (party in politicalParties) {
            parties.add(party)
        }
        val totalNumber = parties.size
        parties.add(PoliticalParty(
                "",
                null,
                "Belum menentukan pilihan",
                totalNumber + 1
        ))
        partaiAdapter.setDatas(parties)
    }

    private fun setupRecyclerView() {
        layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        partaiAdapter = PartaiAdapter()
        partaiAdapter.listener = object : PartaiAdapter.Listener {
            override fun onSelectItem(politicalParty: PoliticalParty) {
                partai_selected.text = politicalParty.name
                (activity as CatatanPilihanActivity).setSelectedParty(politicalParty.id)
            }
        }
        partai_rv_view.layoutManager = layoutManager
        partai_rv_view.adapter = partaiAdapter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        partaiAdapter.selectedItem?.id?.let { outState.putString("selected_partai", it) }
    }

    companion object {
        fun newInstance(): PartaiFragment {
            return PartaiFragment()
        }
    }
}
