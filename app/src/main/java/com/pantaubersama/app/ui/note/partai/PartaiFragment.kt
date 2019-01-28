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
    private lateinit var noParty: PoliticalParty
    private var savedInstanceState: Bundle? = null

    @Inject
    override lateinit var presenter: PartaiPresenter

    override fun initView(view: View, savedInstanceState: Bundle?) {
        this.savedInstanceState = savedInstanceState
        setupRecyclerView()
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
            for (partai in partaiAdapter.getListData() as MutableList<PoliticalParty>) {
                if (partai == profile.politicalParty) {
                    partaiAdapter.setSelectedData(partai)
                }
            }
        } else {
            partaiAdapter.setSelectedData(noParty)
        }
    }

    override fun showPartai(politicalParties: List<PoliticalParty>) {
        val parties: MutableList<PoliticalParty> = ArrayList()
        for (party in politicalParties) {
            parties.add(party)
        }
        noParty = PoliticalParty(
            "",
            null,
            "Belum menentukan pilihan",
            parties.size + 1
        )
        parties.add(noParty)
        partaiAdapter.setDatas(parties)
        setSelectedParty()
    }

    private fun setSelectedParty() {
        if (savedInstanceState != null) {
            for (partai in partaiAdapter.getListData() as MutableList<PoliticalParty>) {
                if (partai == savedInstanceState?.getSerializable("selected_partai") as PoliticalParty) {
                    partaiAdapter.setSelectedData(partai)
                }
            }
        } else {
            presenter.getUserProfile()
        }
    }

    private fun setupRecyclerView() {
        layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        partaiAdapter = PartaiAdapter()
        partaiAdapter.listener = object : PartaiAdapter.Listener {
            override fun onSelectItem(politicalParty: PoliticalParty) {
                partai_selected.text = politicalParty.name
                (activity as CatatanPilihanActivity).setSelectedParty(politicalParty)
            }
        }
        partai_rv_view.layoutManager = layoutManager
        partai_rv_view.adapter = partaiAdapter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        partaiAdapter.selectedItem?.let { outState.putSerializable("selected_partai", it) }
    }

    companion object {
        fun newInstance(): PartaiFragment {
            return PartaiFragment()
        }
    }
}
