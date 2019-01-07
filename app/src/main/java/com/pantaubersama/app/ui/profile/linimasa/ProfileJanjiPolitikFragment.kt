package com.pantaubersama.app.ui.profile.linimasa

import android.view.View
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.data.model.janjipolitik.JanjiPolitik
import com.pantaubersama.app.di.component.ActivityComponent
import kotlinx.android.synthetic.main.layout_common_recyclerview.*
import timber.log.Timber

class ProfileJanjiPolitikFragment : BaseFragment<ProfileJanjiPolitikPresenter>(), ProfileJanjiPolitikView {

    companion object {
        fun newInstance(): ProfileJanjiPolitikFragment {
            return ProfileJanjiPolitikFragment()
        }
    }

    override var presenter: ProfileJanjiPolitikPresenter = ProfileJanjiPolitikPresenter()

    override fun initInjection(activityComponent: ActivityComponent) {

    }

    override fun initView(view: View) {
        setupJanPolList()
        presenter?.getJanjiPolitikList()
    }

    private fun setupJanPolList() {
//        adapter = JanjiPolitikAdapterDEL(context!!)
//        adapter.listener = object : JanjiPolitikAdapterDEL.AdapterListener {
//            override fun onClickContent(item: JanjiPolitik) {
//                startActivity(DetailJanjiPolitikActivity.setIntent(context!!, item.id!!))
//            }
//        }
//        recycler_view?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
//        recycler_view?.adapter = adapter
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
//            adapter.replaceData(janpolList)
        }
    }
}