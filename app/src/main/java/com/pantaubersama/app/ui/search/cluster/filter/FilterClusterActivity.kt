package com.pantaubersama.app.ui.search.cluster.filter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.cluster.Category
import com.pantaubersama.app.di.component.ActivityComponent
import kotlinx.android.synthetic.main.activity_filter_cluster.*
import javax.inject.Inject

class FilterClusterActivity : BaseActivity<FilterClusterPresenter>(), FilterClusterView {
    @Inject override lateinit var presenter: FilterClusterPresenter

    override fun statusBarColor(): Int? = R.color.white
    override fun setLayout(): Int = R.layout.activity_filter_cluster

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, getString(R.string.txt_filter_cluster), R.color.white, 4f)
        presenter.getFilter()

        rl_cluster_category_container.setOnClickListener {
//            ClusterListDial
        }
    }

    override fun showLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showSelectedFilter(categoryItem: Category?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSuccessSetFilter() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
