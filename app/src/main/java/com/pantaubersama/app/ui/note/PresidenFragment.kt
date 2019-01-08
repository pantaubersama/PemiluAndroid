package com.pantaubersama.app.ui.note

import android.view.View
import com.pantaubersama.app.R
import com.pantaubersama.app.base.CommonFragment

class PresidenFragment : CommonFragment() {

    override fun initView(view: View) {
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        /*
        RecyclerView untuk menampilkan calon presiden
        style menggunakan StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        item layoutnya menggunakan item_pilihan.xml
         */
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
