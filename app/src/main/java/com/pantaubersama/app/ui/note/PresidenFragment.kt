package com.pantaubersama.app.ui.note

import android.view.View
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.pantaubersama.app.R
import com.pantaubersama.app.base.CommonFragment
import com.pantaubersama.app.data.model.capres.PaslonData
import kotlinx.android.synthetic.main.fragment_presiden.view.*

class PresidenFragment : CommonFragment() {
    private lateinit var layoutManager: StaggeredGridLayoutManager
    private lateinit var paslonAdapter: PaslonAdapter

    override fun initView(view: View) {
        setupRecyclerView(view)
        setData()
    }

    private fun setData() {
        val paslons: MutableList<PaslonData> = ArrayList()
        paslons.add(
            PaslonData(
                1,
                "Jokowi - Ma'ruf",
                R.drawable.ava_calon_1,
                "Joko Widodo",
                "Ma'ruf Amin")
        )
        paslons.add(
            PaslonData(
                2,
                "Prabowo - Sandi",
                R.drawable.ava_calon_2,
                "Prabowo Subiyanto",
                "Sandiaga Salahudin Uno")
        )
        paslons.add(
            PaslonData(
                3,
                "Belum menentukan pilihan")
        )
        paslonAdapter.setDatas(paslons)
    }

    private fun setupRecyclerView(view: View) {
        layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        paslonAdapter = PaslonAdapter()
        paslonAdapter.listener = object : PaslonAdapter.Listener {
            override fun onSelectItem(paslonData: PaslonData) {
                view.selected_paslon.text = paslonData.paslonName
            }
        }
        view.presiden_recycler_view_calon.layoutManager = layoutManager
        view.presiden_recycler_view_calon.adapter = paslonAdapter
    }

    override fun setLayout(): Int {
        return R.layout.fragment_presiden
    }

    companion object {
        fun newInstance(): PresidenFragment {
            return PresidenFragment()
        }
    }
}
