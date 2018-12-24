package com.pantaubersama.app.ui.penpol.kuis.list

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.data.model.kuis.KuisListItem
import com.pantaubersama.app.data.model.kuis.KuisState
import com.pantaubersama.app.ui.penpol.kuis.ikutikuis.IkutiKuisActivity
import com.pantaubersama.app.ui.penpol.kuis.kuisinfo.KuisInfoActivity
import com.pantaubersama.app.utils.LineDividerItemDecoration
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.extensions.color
import com.pantaubersama.app.utils.extensions.dip
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.fragment_kuis.*
import kotlinx.android.synthetic.main.layout_banner_container.*
import kotlinx.android.synthetic.main.layout_common_recyclerview.*

class KuisFragment : BaseFragment<BasePresenter<*>>() {

    private lateinit var adapter: KuisListAdapter

    override fun setLayout(): Int = R.layout.fragment_kuis

    override fun initPresenter(): BasePresenter<*>? = null

    override fun initView(view: View) {
        tv_banner_text.text = "Ikuti quiz pilih jawaban tanpa kamu tahu itu jawaban siapa, " +
            "kemudian tunggu kejutannya dan lihat hasil dari quiz ini"

        read_more_action.setOnClickListener {
            val intent = Intent(context, KuisInfoActivity::class.java)
            startActivity(intent)
        }

        iv_banner_image.setImageResource(R.drawable.ic_banner_kuis)

        adapter = KuisListAdapter()
        adapter.listener = object : KuisListAdapter.AdapterListener{
            override fun onClick(item: KuisListItem.Item) {
                val intent = Intent(context, IkutiKuisActivity::class.java)
                intent.putExtra(PantauConstants.Quiz.QUIZ_DATA, item.id)
                startActivity(intent)
            }
        }
        recycler_view.layoutManager = LinearLayoutManager(requireContext())
        recycler_view.adapter = adapter
        recycler_view.addItemDecoration(LineDividerItemDecoration(color(R.color.gray_3), dip(1), dip(16)))

        iv_banner_close.setOnClickListener {
            content_container.removeView(banner_container)
        }

        swipe_refresh.setOnRefreshListener {
            swipe_refresh.isRefreshing = false
        }
        progress_bar.visibleIf(false)

        setupData()
    }

    private fun setupData() {
        adapter.data = listOf(
                KuisListItem.Result(70, "Jokowi - Makruf"),
                KuisListItem.Item(1, 1, 7, KuisState.NOT_TAKEN),
                KuisListItem.Item(2,2, 7, KuisState.COMPLETED),
                KuisListItem.Item(3,3, 7, KuisState.INCOMPLETE)
        )
    }

    override fun showLoading() {}
    override fun dismissLoading() {}
    override fun showError(throwable: Throwable) {}

    companion object {
        fun newInstance(): KuisFragment {
            return KuisFragment()
        }
    }
}
