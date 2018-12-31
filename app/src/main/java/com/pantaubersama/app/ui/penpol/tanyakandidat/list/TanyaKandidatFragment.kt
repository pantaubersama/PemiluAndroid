package com.pantaubersama.app.ui.penpol.tanyakandidat.list

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseApp
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.base.listener.OnItemClickListener
import com.pantaubersama.app.ui.bannerinfo.BannerInfoActivity
import com.pantaubersama.app.data.interactors.TanyaKandidateInteractor
import com.pantaubersama.app.data.model.tanyakandidat.Pertanyaan
import com.pantaubersama.app.ui.penpol.tanyakandidat.create.CreateTanyaKandidatActivity
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.ShareUtil
import com.pantaubersama.app.utils.ToastUtil
import kotlinx.android.synthetic.main.fragment_tanya_kandidat.*
import kotlinx.android.synthetic.main.layout_banner_container.*
import kotlinx.android.synthetic.main.layout_common_recyclerview.*
import javax.inject.Inject

class TanyaKandidatFragment : BaseFragment<TanyaKandidatPresenter>(), TanyaKandidatView {
    @Inject
    lateinit var interactor: TanyaKandidateInteractor
    private var page = 1
    private var perPage = 10

    private lateinit var adapter: TanyaKandidatAdapter

    override fun initInjection() {
        (activity?.application as BaseApp).createActivityComponent(activity)?.inject(this)
    }

    override fun initPresenter(): TanyaKandidatPresenter? {
        return TanyaKandidatPresenter(interactor)
    }

    override fun initView(view: View) {
        question_section.setOnClickListener {
            val intent = Intent(context, CreateTanyaKandidatActivity::class.java)
            startActivity(intent)
        }
        setupBanner()
        setupTanyaKandidatList()
        presenter?.getTanyaKandidatList(page, perPage, "created", "desc", "user_verified_all")
    }

    private fun setupBanner() {
        presenter?.isBannerShown()
    }

    override fun showBanner() {
        layout_banner_tanya_kandidat.visibility = View.VISIBLE
        tv_banner_text.text = getString(R.string.tanya_kandidat_banner_text)
        iv_banner_image.setImageResource(R.drawable.ic_banner_tanya_kandidat)
        fl_banner.setOnClickListener {
            val intent = BannerInfoActivity.setIntent(context!!, PantauConstants.Extra.TYPE_TANYA_KANDIDAT)
            startActivityForResult(intent, PantauConstants.RequestCode.BANNER_TANYA_KANDIDAT)
        }
        iv_banner_close.setOnClickListener {
            layout_banner_tanya_kandidat.visibility = View.GONE
        }
    }

    override fun hideBanner() {
        layout_banner_tanya_kandidat.visibility = View.GONE
    }

    private fun setupTanyaKandidatList() {
        adapter = TanyaKandidatAdapter(context!!)
        adapter.listener = object : TanyaKandidatAdapter.AdapterListener {
            override fun onClickShare(item: Pertanyaan) {
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
        swipe_refresh?.setOnRefreshListener {
            swipe_refresh?.isRefreshing = false
            page = 1
            presenter?.getTanyaKandidatList(page, perPage, "created", "desc", "user_verified_all")
        }
    }

    override fun bindDataTanyaKandidat(pertanyaanList: MutableList<Pertanyaan>?) {
        recycler_view?.visibility = View.VISIBLE
        adapter.replaceData(pertanyaanList?.toList()!!)
    }

    override fun showEmptyDataAlert() {
        view_empty_state?.visibility = View.VISIBLE
    }

    override fun setLayout(): Int {
        return R.layout.fragment_tanya_kandidat
    }

    override fun showLoading() {
        view_empty_state?.visibility = View.GONE
        recycler_view?.visibility = View.INVISIBLE
        progress_bar?.visibility = View.VISIBLE
    }

    override fun dismissLoading() {
        progress_bar?.visibility = View.GONE
    }

    override fun showFailedGetDataAlert() {
        ToastUtil.show(context!!, "Gagal memuat daftar pertanyaan")
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PantauConstants.TanyaKandidat.CREATE_TANYA_KANDIDAT_REQUEST_CODE -> {
                    adapter.add((data?.getSerializableExtra(PantauConstants.TanyaKandidat.TANYA_KANDIDAT_DATA) as Pertanyaan), 0)
                    recycler_view.smoothScrollToPosition(0)
                }
                PantauConstants.RequestCode.BANNER_TANYA_KANDIDAT -> hideBanner()
            }
        }
    }
}