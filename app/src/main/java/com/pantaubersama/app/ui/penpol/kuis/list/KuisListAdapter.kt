package com.pantaubersama.app.ui.penpol.kuis.list

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseRecyclerAdapter
import com.pantaubersama.app.base.viewholder.LoadingViewHolder
import com.pantaubersama.app.data.model.bannerinfo.BannerInfo
import com.pantaubersama.app.data.model.kuis.KuisItem
import com.pantaubersama.app.data.model.kuis.KuisUserResult
import com.pantaubersama.app.data.model.kuis.KuisState
import com.pantaubersama.app.ui.penpol.kuis.result.KuisUserResultActivity
import com.pantaubersama.app.utils.PantauConstants.ItemModel.TYPE_BANNER
import com.pantaubersama.app.utils.PantauConstants.ItemModel.TYPE_KUIS_ITEM
import com.pantaubersama.app.utils.PantauConstants.ItemModel.TYPE_KUIS_RESULT
import com.pantaubersama.app.utils.PantauConstants.ItemModel.TYPE_LOADING
import com.pantaubersama.app.utils.extensions.inflate
import com.pantaubersama.app.utils.extensions.loadUrl
import com.pantaubersama.app.utils.extensions.setBackgroundTint
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_banner_container.*
import kotlinx.android.synthetic.main.item_kuis.*
import kotlinx.android.synthetic.main.item_kuis_result.*

class KuisListAdapter : BaseRecyclerAdapter() {

    var listener: KuisListAdapter.AdapterListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_BANNER -> BannerViewHolder(parent.inflate(R.layout.item_banner_container))
            TYPE_KUIS_RESULT -> ResultViewHolder(parent.inflate(R.layout.item_kuis_result))
            TYPE_KUIS_ITEM -> ItemViewHolder(parent.inflate(R.layout.item_kuis))
            TYPE_LOADING -> LoadingViewHolder(parent.inflate(R.layout.item_loading))
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? BannerViewHolder)?.bind(data[position] as BannerInfo)
        (holder as? ResultViewHolder)?.bind(data[position] as KuisUserResult)
        (holder as? ItemViewHolder)?.bind(data[position] as KuisItem)
    }

    inner class ItemViewHolder(override val containerView: View)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(item: KuisItem) {
            val (buttonText, buttonColor) = when (item.state) {
                KuisState.NOT_PARTICIPATING -> "IKUTI" to R.color.orange
                KuisState.FINISHED -> "HASIL" to R.color.colorAccent
                KuisState.IN_PROGRESS -> "LANJUT" to R.color.red_2
            }
            iv_kuis_image.loadUrl(item.image.url)
            tv_kuis_title.text = item.title
            tv_kuis_count.text = "${item.kuisQuestionsCount} Pertanyaan"
            btn_kuis_open.text = "$buttonText >>"
            btn_kuis_open.setBackgroundTint(buttonColor)
            btn_kuis_open.setOnClickListener { listener?.onClickOpenKuis(item) }
            btn_share.setOnClickListener { listener?.onClickShare(item) }
        }
    }

    class ResultViewHolder(override val containerView: View)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(result: KuisUserResult) {
            presiden_total_kuis.text = result.meta.run { "$finished dari $total Kuis" }
            tv_kuis_result.text = "%.2f%% (%s)".format(result.percentage, result.team.title)
            iv_kuis_result.loadUrl(result.team.avatar)
            btn_share_result.setOnClickListener {
                val intent = Intent(containerView.context, KuisUserResultActivity::class.java)
                containerView.context.startActivity(intent)
            }
        }
    }

    inner class BannerViewHolder(
        override val containerView: View?
    ) : RecyclerView.ViewHolder(containerView!!), LayoutContainer {
        fun bind(item: BannerInfo) {
            tv_banner_text.text = item.body
            iv_banner_image.loadUrl(item.headerImage?.url)
            rl_banner_container.setOnClickListener { listener?.onClickBanner(item) }
            iv_banner_close.setOnClickListener { removeBanner() }
        }
    }

    fun removeBanner() {
        if (data[0] is BannerInfo) {
            data.removeAt(0)
            notifyItemRemoved(0)
        }
    }

    interface AdapterListener {
        fun onClickBanner(item: BannerInfo)
        fun onClickOpenKuis(item: KuisItem)
        fun onClickShare(item: KuisItem)
    }
}