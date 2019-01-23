package com.pantaubersama.app.ui.categorydialog

import android.view.View
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseDialogFragment
import com.pantaubersama.app.di.component.ActivityComponent
import javax.inject.Inject

/**
 * @author edityomurti on 23/01/2019 10:50
 */
class CategoryListDialog : BaseDialogFragment<CategoryListDialogPresenter>(), CategoryListDialogView {
    @Inject override lateinit var presenter: CategoryListDialogPresenter

    override fun setLayout(): Int = R.layout.layout_dialog_list

    override fun initInjection(activityComponent: ActivityComponent) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initView(view: View) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun showLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}