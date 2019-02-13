package com.pantaubersama.app.ui.merayakan.perhitungan.list

import android.os.Bundle
import android.view.View
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseFragment
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.merayakan.perhitungan.create.datatps.DataTPSActivity
import com.pantaubersama.app.utils.PantauConstants
import kotlinx.android.synthetic.main.fragment_perhitungan.*
import javax.inject.Inject

class PerhitunganFragment : BaseFragment<PerhitunganPresenter>() {
    private lateinit var profile: Profile

    @Inject
    override lateinit var presenter: PerhitunganPresenter

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun showLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setLayout(): Int {
        return R.layout.fragment_perhitungan
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        fab_add.setOnClickListener {
            DataTPSActivity.start(requireContext(), PantauConstants.Merayakan.CREATE_PERHITUNGAN_REQUEST_CODE)
        }
    }

    companion object {
        fun newInstance(): PerhitunganFragment {
            return PerhitunganFragment()
        }

        val TAG: String = PerhitunganFragment::class.java.simpleName
    }
}
