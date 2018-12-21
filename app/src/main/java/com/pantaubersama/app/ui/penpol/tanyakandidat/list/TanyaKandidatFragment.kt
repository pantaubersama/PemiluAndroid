package com.pantaubersama.app.ui.penpol.tanyakandidat.list

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseApp
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.base.listener.OnItemClickListener
import com.pantaubersama.app.data.model.tanyakandidat.TanyaKandidat
import com.pantaubersama.app.ui.penpol.tanyakandidat.create.CreateTanyaKandidatActivity
import com.pantaubersama.app.utils.ToastUtil
import kotlinx.android.synthetic.main.fragment_tanya_kandidat.view.*
import kotlinx.android.synthetic.main.layout_common_recyclerview.view.*

class TanyaKandidatFragment : BaseFragment<TanyaKandidatPresenter>(), TanyaKandidatView {

    private lateinit var adapter: TanyaKandidatAdapter
    private var mView: View? = null

    override fun initInjection() {
        (activity?.application as BaseApp).createActivityComponent(activity)?.inject(this)
    }

    override fun initPresenter(): TanyaKandidatPresenter? {
        return TanyaKandidatPresenter()
    }

    override fun initView(view: View) {
        mView = view
        view.question_section.setOnClickListener {
            val intent = Intent(context, CreateTanyaKandidatActivity::class.java)
            startActivity(intent)
        }
        setupTanyaKandidatList()
        presenter?.attach(this) // temporary
        presenter?.getTanyaKandidatList()
    }

    private fun setupTanyaKandidatList() {
        adapter = TanyaKandidatAdapter(context!!)
        mView?.recycler_view?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mView?.recycler_view?.adapter = adapter
        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                ToastUtil.show(context!!, "clicked!")
            }
        })
        mView?.swipe_refresh?.setOnRefreshListener {
            mView?.swipe_refresh?.isRefreshing = false
            presenter?.getTanyaKandidatList()
        }
    }

    override fun bindDataTanyaKandidat(tanyaKandidatList: MutableList<TanyaKandidat>?) {
        mView?.recycler_view?.visibility = View.VISIBLE
        adapter.replaceData(tanyaKandidatList?.toList()!!)
    }

    override fun showEmptyDataAlert() {
        mView?.view_empty_state?.visibility = View.VISIBLE
    }

    override fun setLayout(): Int {
        return R.layout.fragment_tanya_kandidat
    }

    override fun showLoading() {
        mView?.view_empty_state?.visibility = View.GONE
        mView?.recycler_view?.visibility = View.INVISIBLE
        mView?.progress_bar?.visibility = View.VISIBLE
    }

    override fun dismissLoading() {
        mView?.progress_bar?.visibility = View.GONE
    }

    companion object {
        fun newInstance(): TanyaKandidatFragment {
            return TanyaKandidatFragment()
        }
    }

    override fun onDestroy() {
        (activity?.application as BaseApp).releaseActivityComponent()
        super.onDestroy()
    }
}