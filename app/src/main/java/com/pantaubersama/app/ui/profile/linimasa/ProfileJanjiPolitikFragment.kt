package com.pantaubersama.app.ui.profile.linimasa

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.data.model.janjipolitik.JanjiPolitik
import com.pantaubersama.app.ui.linimasa.janjipolitik.adapter.JanjiPolitikAdapterDEL
import com.pantaubersama.app.ui.linimasa.janjipolitik.detail.DetailJanjiPolitikActivity
import kotlinx.android.synthetic.main.layout_common_recyclerview.*
import timber.log.Timber

class ProfileJanjiPolitikFragment : BaseFragment<ProfileJanjiPolitikPresenter>(), ProfileJanjiPolitikView {

    private lateinit var adapter: JanjiPolitikAdapterDEL

    companion object {
        fun newInstance(): ProfileJanjiPolitikFragment {
            return ProfileJanjiPolitikFragment()
        }
    }

    override fun initPresenter(): ProfileJanjiPolitikPresenter? {
        return ProfileJanjiPolitikPresenter()
    }

    override fun initView(view: View) {
        setupJanPolList()
        presenter?.getJanjiPolitikList()
    }

    private fun setupJanPolList() {
        adapter = JanjiPolitikAdapterDEL(context!!)
        adapter.listener = object : JanjiPolitikAdapterDEL.AdapterListener {
            override fun onClickContent(item: JanjiPolitik) {
                startActivity(DetailJanjiPolitikActivity.setIntent(context!!, item.id!!))
            }
        }
        recycler_view?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recycler_view?.adapter = adapter
    }

    override fun setLayout(): Int {
        return R.layout.fragment_profile_janji_politik
    }

    override fun showLoading() {
        view_empty_state.visibility = View.GONE
        recycler_view.visibility = View.INVISIBLE
//        progress_bar.visibility = View.VISIBLE
    }

    override fun dismissLoading() {
        recycler_view.visibility = View.GONE
//        progress_bar.visibility = View.GONE
    }

    override fun bindDataJanjiPolitik(janpolList: MutableList<JanjiPolitik>?) {
        Timber.d("janpol data size : ${janpolList?.size}")
        if (janpolList!!.isEmpty()) {
            view_empty_state.visibility = View.VISIBLE
        } else {
            recycler_view.visibility = View.VISIBLE
            adapter.replaceData(janpolList)
        }
    }
}