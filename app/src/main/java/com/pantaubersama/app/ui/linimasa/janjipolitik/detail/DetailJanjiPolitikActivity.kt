package com.pantaubersama.app.ui.linimasa.janjipolitik.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.cluster.ClusterItem
import com.pantaubersama.app.data.model.janjipolitik.Creator
import com.pantaubersama.app.data.model.janjipolitik.JanjiPolitik
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.clusterdetail.ClusterDetailActivity
import com.pantaubersama.app.ui.home.HomeActivity
import com.pantaubersama.app.ui.profile.ProfileActivity
import com.pantaubersama.app.ui.widget.DeleteConfimationDialog
import com.pantaubersama.app.ui.widget.OptionDialog
import com.pantaubersama.app.utils.CopyUtil
import com.pantaubersama.app.utils.FacebookEventLogger
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_ITEM_POSITION
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_JANPOL_ID
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_JANPOL_ITEM
import com.pantaubersama.app.utils.PantauConstants.ResultCode.RESULT_DELETE_ITEM_JANPOL
import com.pantaubersama.app.utils.ShareUtil
import com.pantaubersama.app.utils.extensions.enableLottie
import com.pantaubersama.app.utils.extensions.loadUrl
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.activity_detail_janji_politik.*
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
    private lateinit var profile: Profile

    override fun setLayout(): Int = R.layout.activity_detail_janji_politik

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
        presenter.getProfile()
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

    override fun bindProfile(profile: Profile) {
        this.profile = profile
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
            iv_creator_avatar.setOnClickListener {
                openProfile(janjiPolitik)
            }
            tv_creator_name.text = it.fullName
            tv_creator_name.setOnClickListener {
                openProfile(janjiPolitik)
            }
            tv_creator_description.text = it.about
            tv_creator_description.setOnClickListener {
                openProfile(janjiPolitik)
            }
        }
        if (creator?.cluster != null) {
            ll_cluster_container.visibleIf(true)
            cluster = creator?.cluster
            if (cluster?.id != null) {
                cluster?.let {
                    tv_cluster_name.text = it.name
                    tv_cluster_member_count.text = it.memberCount.toString() + getString(R.string.anggota)
                    iv_avatar_cluster.loadUrl(it.image?.url, R.drawable.ic_avatar_placeholder)
                }
                ll_cluster_container.setOnClickListener {
                    cluster?.id?.let { it1 -> ClusterDetailActivity.start(this@DetailJanjiPolitikActivity, it1) }
                }
            } else {
                ll_cluster_container.visibility = View.GONE
            }
        } else {
            ll_cluster_container.visibleIf(false)
        }

        tv_posted_time.text = janpolItem?.createdAtInWord?.id

        iv_share_button.setOnClickListener { onClickShare() }
        iv_options_button.setOnClickListener { onClickOption() }
        janjiPolitik.title?.let {
            janjiPolitik.id?.let { it1 ->
                FacebookEventLogger.logViewedContentEvent(
                    this@DetailJanjiPolitikActivity,
                    PantauConstants.JANPOL,
                    it,
                    it1
                )
            }
        }
    }

    private fun openProfile(janjiPolitik: JanjiPolitik) {
        val intent = Intent(this@DetailJanjiPolitikActivity, ProfileActivity::class.java)
        if (profile.id != janjiPolitik.creator?.id) {
            intent.putExtra(PantauConstants.Profile.USER_ID, janjiPolitik.creator?.id)
        }
        startActivityForResult(intent, PantauConstants.Profile.PROFILE_REQUEST_CODE)
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
        itemPosition?.let {
            intent.putExtra(EXTRA_ITEM_POSITION, it)
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

    private fun onClickOption() {
        val dialog = OptionDialog(this, R.layout.layout_option_dialog_common)
        if (profile.cluster != null &&
            janpolItem?.creator?.cluster != null &&
            janpolItem?.creator?.cluster?.id?.equals(profile.cluster?.id) == true &&
            janpolItem?.creator?.id.equals(profile.id) &&
            profile.cluster?.isEligible!!) {
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
                        val deleteDialog = DeleteConfimationDialog(
                            this@DetailJanjiPolitikActivity,
                            getString(R.string.txt_delete_item_ini),
                            0, janpolItem?.id!!,
                            object : DeleteConfimationDialog.DialogListener {
                                override fun onClickDeleteItem(id: String, position: Int) {
                                    showProgressDialog(getString(R.string.menghapus_janji_politik))
                                    presenter.deleteJanjiPolitik(id)
                                }
                            })
                        deleteDialog.show()
                        dialog.dismiss()
                    }
                }
            }
        }
    }

    private fun onClickCopyUrl(id: String) {
        CopyUtil.copyJanpol(this, id)
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
