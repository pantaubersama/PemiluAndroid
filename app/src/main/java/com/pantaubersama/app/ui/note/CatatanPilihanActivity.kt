package com.pantaubersama.app.ui.note

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.partai.PoliticalParty
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.note.partai.PartaiFragment
import com.pantaubersama.app.ui.note.presiden.PresidenFragment
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.activity_catatan_pilihanku.*
import kotlinx.android.synthetic.main.catatan_tab_item.*
import javax.inject.Inject

class CatatanPilihanActivity : BaseActivity<CatatanPilihanPresenter>(), CatatanPilihanView {
    var selectedTab: String = ""
    var slectedPaslon = 0
    lateinit var selectedPartai: PoliticalParty

    @Inject
    override lateinit var presenter: CatatanPilihanPresenter

    private lateinit var adapter: CatatanTabAdapter

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    val presidenFragment = PresidenFragment.newInstance()
    val partaiFragment = PartaiFragment.newInstance()

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(false, getString(R.string.title_catatan_pilihanku), R.color.white, 4f)
        setupRecyclerview()
        setupViewPager()
        if (savedInstanceState != null) {
            adapter.setSelected(savedInstanceState.getInt("tab_selected"))
        } else {
            adapter.setSelected(0)
        }
        catatan_pilihanku_ok.setOnClickListener {
            presenter.submitCatatanku(slectedPaslon, selectedPartai)
        }
    }

    private fun setupRecyclerview() {
        recycler_view.layoutManager =
                LinearLayoutManager(this@CatatanPilihanActivity, LinearLayoutManager.HORIZONTAL, false)
        adapter = CatatanTabAdapter()
        recycler_view.adapter = adapter
    }

    inner class CatatanTabAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private val tabs: MutableList<String> = ArrayList()

        init {
            tabs.add("Pilpres")
            tabs.add("Partai")
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return CatatanTabViewHolder(parent.inflate(R.layout.catatan_tab_item))
        }

        override fun getItemCount(): Int {
            return tabs.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as CatatanTabViewHolder).bind(tabs[position], position)
        }

        fun setSelected(i: Int) {
            selectedTab = tabs[i]
        }

        inner class CatatanTabViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
            fun bind(data: String, position: Int) {
                tab_text.text = data
                if (data == selectedTab) {
                    item.setBackgroundResource(R.drawable.rounded_outline_red)
                } else {
                    item.setBackgroundResource(R.drawable.rounded_gray_dark_1)
                }
                itemView.setOnClickListener {
                    selectedTab = data
                    notifyDataSetChanged()
                    setPage(position)
                }
            }
        }
    }

    private fun setPage(position: Int) {
        catatan_pilihanku_container.currentItem = position
    }

    private fun setupViewPager() {
        catatan_pilihanku_container.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return when (position) {
                    0 -> {
                        presidenFragment
                    }
                    1 -> partaiFragment
                    else -> Fragment()
                }
            }

            override fun getCount(): Int {
                return adapter.itemCount
            }
        }
    }

    override fun setLayout(): Int {
        return R.layout.activity_catatan_pilihanku
    }

    override fun showLoading() {
        showProgressDialog("Menyimpan pilihan")
    }

    override fun dismissLoading() {
        dismissProgressDialog()
    }

    override fun showSuccessSubmitCatatanAlert() {
        ToastUtil.show(this@CatatanPilihanActivity, "Pilihan tersimpan")
    }

    override fun finishActivity() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun showFailedSubmitCatatanAlert() {
        ToastUtil.show(this@CatatanPilihanActivity, "Gagal menyimpan pilihan")
    }

    fun setSelectedPaslon(paslon: Int) {
        this.slectedPaslon = paslon
    }

    fun setSelectedParty(partai: PoliticalParty) {
        this.selectedPartai = partai
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("tab_selected", catatan_pilihanku_container.currentItem)
    }
}
