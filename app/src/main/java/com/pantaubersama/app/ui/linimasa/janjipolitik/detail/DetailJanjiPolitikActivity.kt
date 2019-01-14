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
import com.pantaubersama.app.ui.home.HomeActivity
import com.pantaubersama.app.ui.widget.DeleteConfimationDialog
import com.pantaubersama.app.ui.widget.OptionDialog
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_ITEM_POSITION
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_JANPOL_ID
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_JANPOL_ITEM
import com.pantaubersama.app.utils.PantauConstants.ResultCode.RESULT_DELETE_ITEM_JANPOL
import com.pantaubersama.app.utils.ShareUtil
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.enableLottie
import com.pantaubersama.app.utils.extensions.loadUrl
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.activity_detail_janji_politik.*
import kotlinx.android.synthetic.main.item_cluster.*
import kotlinx.android.synthetic.main.layout_empty_state.*
import kotlinx.android.synthetic.main.layout_fail_state.*
import kotlinx.android.synthetic.main.layout_loading_state.*
import javax.inject.Inject

class DetailJanjiPolitikActivity : BaseActivity<DetailJanjiPolitikPresenter>(), DetailJanjiPolitikView {

    @Inject override lateinit var presenter: DetailJanjiPolitikPresenter

    private var janpolId: String? = null
    private var janpolItem: JanjiPolitik? = null
    private var creator: Creator? = null
    private var cluster: ClusterItem? = null
    private var itemPosition: Int? = null

    companion object {
        fun setIntent(context: Context, janjiPolitik: JanjiPolitik, itemPosition: Int): Intent {
            val intent = Intent(context, DetailJanjiPolitikActivity::class.java)
            intent.putExtra(EXTRA_JANPOL_ITEM, janjiPolitik)
            intent.putExtra(EXTRA_ITEM_POSITION, itemPosition)
            return intent
        }

        fun setIntent(context: Context, idJanpol: String): Intent {
            val intent = Intent(context, DetailJanjiPolitikActivity::class.java)
            intent.putExtra(EXTRA_JANPOL_ID, idJanpol)
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
        if (intent.getSerializableExtra(EXTRA_JANPOL_ITEM) != null) {
            this.janpolItem = intent.getSerializableExtra(EXTRA_JANPOL_ITEM) as JanjiPolitik
            this.itemPosition = intent.getIntExtra(EXTRA_ITEM_POSITION, -1)
        } else {
            this.janpolId = intent.getStringExtra(EXTRA_JANPOL_ID)
        }
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        janpolItem?.let {
            onBindData(it)
        } ?: run {
            janpolId?.let {
                presenter.getJanpol(it)
            }
        }

        btn_close.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBindData(janjiPolitik: JanjiPolitik) {
        janpolItem = janjiPolitik
        scrollview_detail_janpol.visibleIf(true)
        tv_detail_janpol_title.text = janpolItem?.title
        tv_detail_janpol_content.text = janpolItem?.body
        if (janpolItem?.image?.url != null) {
            iv_detail_janpol_image.visibleIf(true)
            iv_detail_janpol_image.loadUrl(janpolItem?.image?.url)
        } else {
            iv_detail_janpol_image.visibleIf(false)
        }

        creator = janpolItem?.creator
        creator?.let {
            iv_creator_avatar.loadUrl(it.avatar?.medium?.url, R.drawable.ic_avatar_placeholder)
            tv_creator_name.text = it.fullName
            tv_creator_description.text = it.about
        }
        if (creator?.cluster != null) {
            ll_cluster_container.visibleIf(true)
            cluster = creator?.cluster
            cluster?.let {
                tv_cluster_name.text = it.name
                tv_cluster_member_count.text = it.memberCount.toString() + getString(R.string.anggota)
                iv_avatar_cluster.loadUrl(it.image?.url, R.drawable.ic_avatar_placeholder)
            }
        } else {
            ll_cluster_container.visibleIf(false)
        }

        tv_posted_time.text = janpolItem?.createdAtInWord?.id

        iv_share_button.setOnClickListener { onClickShare() }
        iv_options_button.setOnClickListener { onClickOption() }
    }

    override fun showLoading() {
        scrollview_detail_janpol.visibleIf(false)
        lottie_loading.enableLottie(true, lottie_loading)
        view_empty_state.enableLottie(false, lottie_empty_state)
        view_fail_state.enableLottie(false, lottie_fail_state)
    }

    override fun dismissLoading() {
        lottie_loading.enableLottie(false, lottie_loading)
    }

    override fun onSuccessDeleteItem() {
        val intent = Intent()
        if (itemPosition != null) {
            intent.putExtra(EXTRA_ITEM_POSITION, itemPosition?.let { it })
            setResult(RESULT_DELETE_ITEM_JANPOL, intent)
        }
        finish()
    }

    override fun onFailedDeleteItem(throwable: Throwable) {
        dismissProgressDialog()
    }

    override fun onDataNotFound() {
        view_empty_state.enableLottie(true, lottie_empty_state)
        tv_empty_state.text = getString(R.string.janji_politik_tidak_ditemukan)
    }

    override fun onFailedGetData(throwable: Throwable) {
        view_fail_state.enableLottie(true, lottie_fail_state)
        tv_fail_state.text = throwable.message
    }

    override fun setLayout(): Int {
        return R.layout.activity_detail_janji_politik
    }

    private fun onClickOption() {
        val myProfile = presenter.getMyProfile()
        val dialog = OptionDialog(this, R.layout.layout_option_dialog_tanya_kandidat)
        if (myProfile.cluster != null &&
            janpolItem?.creator?.cluster != null &&
            janpolItem?.creator?.cluster?.id?.equals(myProfile.cluster?.id)!! &&
            janpolItem?.creator?.id.equals(myProfile.id) &&
            myProfile.cluster?.isEligible!!) {
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
                        onClickCopyUrl(janpolItem?.id!!)
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
                        val deleteDialog = DeleteConfimationDialog(this@DetailJanjiPolitikActivity, getString(R.string.txt_delete_item_ini), 0, janpolItem?.id!!)
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

    override fun onBackPressed() {
        if (this.isTaskRoot) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        } else {
            finish()
        }
    }
}
