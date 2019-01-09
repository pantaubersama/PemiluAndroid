package com.pantaubersama.app.ui.linimasa.janjipolitik.detail

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.cluster.ClusterItem
import com.pantaubersama.app.data.model.janjipolitik.Creator
import com.pantaubersama.app.data.model.janjipolitik.JanjiPolitik
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.widget.DeleteConfimationDialog
import com.pantaubersama.app.ui.widget.OptionDialog
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_ITEM_POSITION
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_JANPOL_ITEM
import com.pantaubersama.app.utils.PantauConstants.ResultCode.RESULT_DELETE_ITEM_JANPOL
import com.pantaubersama.app.utils.ShareUtil
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.loadUrl
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.activity_detail_janji_politik.*
import kotlinx.android.synthetic.main.item_cluster.*
import javax.inject.Inject

class DetailJanjiPolitikActivity : BaseActivity<DetailJanjiPolitikPresenter>(), DetailJanjiPolitikView {

    @Inject override lateinit var presenter: DetailJanjiPolitikPresenter

    private lateinit var janpolItem: JanjiPolitik
    private lateinit var creator: Creator
    private lateinit var cluster: ClusterItem
    private var itemPosition: Int? = null

    companion object {
        fun setIntent(context: Context, janjiPolitik: JanjiPolitik, itemPosition: Int): Intent {
            val intent = Intent(context, DetailJanjiPolitikActivity::class.java)
            intent.putExtra(EXTRA_JANPOL_ITEM, janjiPolitik)
            intent.putExtra(EXTRA_ITEM_POSITION, itemPosition)
            return intent
        }
    }

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun statusBarColor(): Int? {
        return R.color.white
    }

    override fun fetchIntentExtra() {
        this.janpolItem = intent.getSerializableExtra(EXTRA_JANPOL_ITEM) as JanjiPolitik
        this.itemPosition = intent.getIntExtra(EXTRA_ITEM_POSITION, -1)
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        tv_detail_janpol_title.text = janpolItem.title
        tv_detail_janpol_content.text = janpolItem.body
        if (janpolItem.image?.url != null) {
            iv_detail_janpol_image.visibleIf(true)
            iv_detail_janpol_image.loadUrl(janpolItem.image?.url)
        } else {
            iv_detail_janpol_image.visibleIf(false)
        }

        creator = janpolItem.creator!!
        iv_creator_avatar.loadUrl(creator.avatar?.medium?.url, R.drawable.ic_avatar_placeholder)
        tv_creator_name.text = creator.fullName
        tv_creator_description.text = creator.about

        cluster = creator.cluster!!
        tv_cluster_name.text = cluster.name
        tv_cluster_member_count.text = cluster.memberCount.toString() + getString(R.string.anggota)
        iv_avatar_cluster.loadUrl(cluster.image?.url, R.drawable.ic_avatar_placeholder)

//        tv_posted_time.text =

        iv_share_button.setOnClickListener { onClickShare() }
        iv_options_button.setOnClickListener { onClickOption() }

        btn_close.setOnClickListener {
            finish()
        }
    }

    override fun showLoading() {
        showProgressDialog(getString(R.string.menghapus_janji_politik))
    }

    override fun dismissLoading() {
        dismissProgressDialog()
    }

    override fun onSuccessDeleteItem() {
        val intent = Intent()
        intent.putExtra(EXTRA_ITEM_POSITION, itemPosition)
        setResult(RESULT_DELETE_ITEM_JANPOL, intent)
        finish()
    }

    override fun onFailedDeleteItem(throwable: Throwable) {
        // do nothing, already handled by presenter
    }

    override fun setLayout(): Int {
        return R.layout.activity_detail_janji_politik
    }

    private fun onClickOption() {
        val dialog = OptionDialog(this, R.layout.layout_option_dialog_tanya_kandidat)
        if (janpolItem.creator?.id.equals(presenter.getUserId())) {
//                    dialog.removeItem(R.id.report_tanya_kandidat_action)
        } else {
            dialog.removeItem(R.id.delete_tanya_kandidat_item_action)
        }
        dialog.removeItem(R.id.report_tanya_kandidat_action) // dihilangkan sementara karena belum ada endpoint @edityo 08/01/19
        dialog.show()
        dialog.listener = object : OptionDialog.DialogListener {
            override fun onClick(viewId: Int) {
                when (viewId) {
                    R.id.copy_url_tanya_kandidat_action -> {
                        onClickCopyUrl(janpolItem.id!!)
                        dialog.dismiss()
                    }
                    R.id.share_tanya_kandidat_action -> {
                        onClickShare()
                        dialog.dismiss()
                    }
                    R.id.report_tanya_kandidat_action -> {
//                        onClickLapor(janpolItem.id!!)
                        dialog.dismiss()
                    }
                    R.id.delete_tanya_kandidat_item_action -> {
                        val deleteDialog = DeleteConfimationDialog(this@DetailJanjiPolitikActivity, getString(R.string.txt_delete_item_ini), 0, janpolItem.id!!)
                        deleteDialog.show()
                        deleteDialog.listener = object : DeleteConfimationDialog.DialogListener {
                            override fun onClickDeleteItem(id: String, position: Int) {
                                showProgressDialog(getString(R.string.menghapus_janji_politik))
                                presenter.deleteJanjiPolitik(id)
                            }
                        }
                        dialog.dismiss()
                    }
                }
            }
        }
    }

    private fun onClickCopyUrl(id: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(PantauConstants.LABEL_COPY, "janpol id : $id")
        clipboard.primaryClip = clip
        ToastUtil.show(this, "janji politik telah disalin")
    }

    private fun onClickShare() {
        ShareUtil.shareItem(this, janpolItem)
    }
}
