package com.pantaubersama.app.ui.profile.penpol

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.data.model.tanyakandidat.Pertanyaan
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.penpol.tanyakandidat.list.TanyaKandidatAdapter
import com.pantaubersama.app.utils.OnScrollListener
import com.pantaubersama.app.utils.ShareUtil
import kotlinx.android.synthetic.main.fragment_profile_tanya_kandidat.*

class ProfileTanyaKandidatFragment : BaseFragment<ProfileTanyaKandidatPresenter>(), ProfileTanyaKandidatView {

    override var presenter: ProfileTanyaKandidatPresenter = ProfileTanyaKandidatPresenter()

    private lateinit var adapter: TanyaKandidatAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var isDataEnd = false
    private var isLoading = false

    companion object {
        fun newInstance(): ProfileTanyaKandidatFragment {
            return ProfileTanyaKandidatFragment()
        }
    }

    override fun initInjection(activityComponent: ActivityComponent) {
    }

    override fun initView(view: View) {
        setupTanyaKandidatList()
        presenter.getTanyaKandidatList()
    }

    override fun setLayout(): Int {
        return R.layout.fragment_profile_tanya_kandidat
    }

    private fun setupTanyaKandidatList() {
        adapter = TanyaKandidatAdapter()
        adapter.listener = object : TanyaKandidatAdapter.AdapterListener {
            override fun onClickTanyaOption(item: Pertanyaan, position: Int) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onClickBanner(bannerInfo: BannerInfo) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onClickShare(item: Pertanyaan?) {
                ShareUtil.shareItem(context!!, item)
            }

            override fun onClickUpvote(id: String?, isLiked: Boolean, position: Int?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onClickDeleteItem(id: String?, position: Int?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onClickCopyUrl(id: String?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onClickLapor(id: String?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recycler_view?.layoutManager = layoutManager
        recycler_view?.adapter = adapter
        recycler_view.addOnScrollListener(object : OnScrollListener(layoutManager) {
            override fun loadMoreItem() {
                adapter.setLoading()
            }

            override fun isDataEnd(): Boolean {
                return isDataEnd
            }

            override fun isLoading(): Boolean {
                return isLoading
            }
        })
    }

    override fun bindDataTanyaKandidat(pertanyaanList: MutableList<Pertanyaan>?) {
        recycler_view?.visibility = View.VISIBLE
        adapter.setDatas(pertanyaanList!! as MutableList<ItemModel>)
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