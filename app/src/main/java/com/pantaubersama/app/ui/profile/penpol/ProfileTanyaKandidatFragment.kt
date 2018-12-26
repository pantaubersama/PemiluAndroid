package com.pantaubersama.app.ui.profile.penpol

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.base.listener.OnItemClickListener
import com.pantaubersama.app.data.model.tanyakandidat.TanyaKandidat
import com.pantaubersama.app.ui.penpol.tanyakandidat.list.TanyaKandidatAdapter
import com.pantaubersama.app.utils.ShareUtil
import com.pantaubersama.app.utils.ToastUtil
import kotlinx.android.synthetic.main.fragment_profile_tanya_kandidat.*

class ProfileTanyaKandidatFragment : BaseFragment<ProfileTanyaKandidatPresenter>(), ProfileTanyaKandidatView {
    private lateinit var adapter: TanyaKandidatAdapter

    companion object {
        fun newInstance(): ProfileTanyaKandidatFragment {
            return ProfileTanyaKandidatFragment()
        }
    }

    override fun initPresenter(): ProfileTanyaKandidatPresenter? {
        return ProfileTanyaKandidatPresenter()
    }

    override fun initView(view: View) {
        setupTanyaKandidatList()
        presenter?.getTanyaKandidatList()
    }

    override fun setLayout(): Int {
        return R.layout.fragment_profile_tanya_kandidat
    }

    private fun setupTanyaKandidatList() {
        adapter = TanyaKandidatAdapter(context!!)
        adapter.listener = object : TanyaKandidatAdapter.AdapterListener {
            override fun onClickShare(item: TanyaKandidat) {
                ShareUtil(context!!, item)
            }
        }
        recycler_view?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recycler_view?.adapter = adapter
        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                ToastUtil.show(context!!, "clicked!")
            }
        })
    }

    override fun bindDataTanyaKandidat(tanyaKandidatList: MutableList<TanyaKandidat>?) {
        recycler_view?.visibility = View.VISIBLE
        adapter.replaceData(tanyaKandidatList?.toList()!!)
    }

    override fun showEmptyDataAlert() {
        view_empty_state?.visibility = View.VISIBLE
    }

    override fun showLoading() {
        view_empty_state?.visibility = View.GONE
        recycler_view?.visibility = View.INVISIBLE
        progress_bar?.visibility = View.VISIBLE
    }

    override fun dismissLoading() {
        progress_bar?.visibility = View.GONE
    }
}