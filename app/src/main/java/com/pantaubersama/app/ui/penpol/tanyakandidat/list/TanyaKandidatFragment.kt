package com.pantaubersama.app.ui.penpol.tanyakandidat.list

import android.content.Intent
import android.support.v4.app.Fragment
import android.view.View
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.ui.penpol.tanyakandidat.create.CreateTanyaKandidatActivity
import kotlinx.android.synthetic.main.fragment_tanya_kandidat.view.*

/**
 * A simple [Fragment] subclass.
 *
 */
class TanyaKandidatFragment : BaseFragment<BasePresenter<*>>() {

    override fun initPresenter(): BasePresenter<*>? {
        return null
    }

    override fun initView(view: View) {
        view.question_section.setOnClickListener {
            val intent = Intent(context, CreateTanyaKandidatActivity::class.java)
            startActivity(intent)
        }
    }

    override fun setLayout(): Int {
        return R.layout.fragment_tanya_kandidat
    }

    override fun showLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showError(throwable: Throwable) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        fun newInstance(): TanyaKandidatFragment {
            return TanyaKandidatFragment()
        }
    }
}