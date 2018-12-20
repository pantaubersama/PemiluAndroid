package com.pantaubersama.app.ui.home.linimasa.pilpres

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.base.listener.OnItemClickListener
import com.pantaubersama.app.data.model.tweet.PilpresTweet
import com.pantaubersama.app.ui.home.linimasa.pilpres.adapter.PilpresAdapter
import com.pantaubersama.app.utils.ToastUtil
import kotlinx.android.synthetic.main.layout_common_recyclerview.view.*

class PilpresFragment : BaseFragment(), PilpresView {
    val TAG = PilpresFragment::class.java.simpleName

    private var presenter: PilpresPresenter? = null

    private lateinit var mView: View

    private lateinit var adapter: PilpresAdapter

    companion object {
//        val ARGS1 = "ARGS1"

        fun newInstance(): PilpresFragment {
            val fragment = PilpresFragment()
            val args = Bundle()
//            args.putString(ARGS1, var1)
            fragment.arguments = args
            return fragment
        }
    }

    override fun initView(view: View) {
        presenter = PilpresPresenter(this)
        presenter?.attach(this)
        this.mView = view
        setupRecyclerPilpres()
    }

    private fun setupRecyclerPilpres() {
        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        adapter = PilpresAdapter(context!!)
        mView.recycler_view.layoutManager = layoutManager
        mView.recycler_view.adapter = adapter
        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                ToastUtil.show(context!!, "clicked!")
            }
        })
        mView.swipe_refresh.setOnRefreshListener {
            mView.swipe_refresh.isRefreshing = false
            presenter?.getPilpresTweet()
        }
        presenter?.getPilpresTweet()
    }

    override fun showPilpresTweet(tweetList: List<PilpresTweet>) {
        if (tweetList.isEmpty()) {
            mView.view_empty_state.visibility = View.VISIBLE
        } else {
            mView.recycler_view.visibility = View.VISIBLE
            adapter.replaceData(tweetList)
        }
    }

    override fun setLayout(): Int {
        return R.layout.fragment_pilpres
    }

    override fun showLoading() {
        mView.view_empty_state.visibility = View.GONE
        mView.recycler_view.visibility = View.INVISIBLE
        mView.progress_bar.visibility = View.VISIBLE
    }

    override fun dismissLoading() {
        mView.recycler_view.visibility = View.GONE
        mView.progress_bar.visibility = View.GONE
    }

    override fun showError(throwable: Throwable) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}