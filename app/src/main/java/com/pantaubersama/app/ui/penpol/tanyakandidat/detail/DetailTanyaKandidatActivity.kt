package com.pantaubersama.app.ui.penpol.tanyakandidat.detail

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.tanyakandidat.Pertanyaan
import com.pantaubersama.app.data.model.user.EMPTY_PROFILE
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.home.HomeActivity
import com.pantaubersama.app.ui.profile.ProfileActivity
import com.pantaubersama.app.ui.widget.DeleteConfimationDialog
import com.pantaubersama.app.ui.widget.OptionDialog
import com.pantaubersama.app.utils.* // ktlint-disable
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_ITEM_POSITION
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_QUESTION_ITEM
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_QUESTION_ID
import com.pantaubersama.app.utils.PantauConstants.ResultCode.RESULT_DELETE_ITEM_QUESTION
import com.pantaubersama.app.utils.PantauConstants.ResultCode.RESULT_ITEM_CHANGED_QUESTION
import com.pantaubersama.app.utils.extensions.enableLottie
import com.pantaubersama.app.utils.extensions.loadUrl
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.activity_detail_tanya_kandidat.*
import kotlinx.android.synthetic.main.item_tanya_kandidat.*
import kotlinx.android.synthetic.main.layout_action_post.*
import kotlinx.android.synthetic.main.layout_fail_state.*
import kotlinx.android.synthetic.main.layout_loading_state.*
import javax.inject.Inject

class DetailTanyaKandidatActivity : BaseActivity<DetailTanyaKandidatPresenter>(), DetailTanyaKandidatView {
    @Inject override lateinit var presenter: DetailTanyaKandidatPresenter
    val animator = ValueAnimator.ofFloat(0.0f, 1.0f).setDuration(1000)
    private var questionId: String? = null
    private var question: Pertanyaan? = null
    private var itemPosition: Int? = null
    private lateinit var profile: Profile

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun statusBarColor(): Int? = R.color.white

    override fun setLayout(): Int = R.layout.activity_detail_tanya_kandidat

    companion object {
        fun setIntent(context: Context, question: Pertanyaan, itemPosition: Int): Intent {
            val intent = Intent(context, DetailTanyaKandidatActivity::class.java)
            intent.putExtra(EXTRA_QUESTION_ITEM, question)
            intent.putExtra(EXTRA_ITEM_POSITION, itemPosition)
            return intent
        }

        fun setIntent(context: Context, questionId: String): Intent {
            val intent = Intent(context, DetailTanyaKandidatActivity::class.java)
            intent.putExtra(EXTRA_QUESTION_ID, questionId)
            return intent
        }
    }

    override fun fetchIntentExtra() {
        intent.getStringExtra(EXTRA_QUESTION_ID)?.let { this.questionId = it }
        intent.getSerializableExtra(EXTRA_QUESTION_ITEM)?.let { this.question = it as Pertanyaan }
        intent.getIntExtra(EXTRA_ITEM_POSITION, -1).let { this.itemPosition = it }
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        presenter.getProfile()
        if (question != null) {
            question?.let { bindData(it) }
        } else {
            questionId?.let { presenter.getData(it) }
        }
        btn_close.setOnClickListener { onBackPressed() }
    }

    override fun bindProfile(profile: Profile) {
        this.profile = profile
    }

    override fun showLoading() {
        lottie_loading.enableLottie(false, lottie_loading)
        layout_item_tanya_kandidat.visibleIf(false)
        view_fail_state.enableLottie(false, lottie_fail_state)
    }

    override fun dismissLoading() {
        lottie_loading.enableLottie(false, lottie_loading)
    }

    override fun bindData(question: Pertanyaan) {
        this.question = question
        iv_user_avatar.loadUrl(question.user.avatar?.url, R.drawable.ic_avatar_placeholder)
        tv_user_name.text = question.user.fullName
        tv_user_bio.text = question.user.about
        user_container.setOnClickListener {
            val intent = Intent(this@DetailTanyaKandidatActivity, ProfileActivity::class.java)
            if (profile?.id != question.user.id) {
                intent.putExtra(PantauConstants.Profile.USER_ID, question.user.id)
            }
            startActivityForResult(intent, PantauConstants.Profile.PROFILE_REQUEST_CODE)
        }
        question_time.text = question.createdAtInWord?.id
        upvote_count_text.text = question.likeCount.toString()
        user_question.text = question.body
        iv_options_button.setOnClickListener { onClickOption() }
        iv_share_button.setOnClickListener { ShareUtil.shareItem(this, question) }
        question.isliked.let {
            if (it) {
                upvote_animation.progress = 1.0f
            } else {
                upvote_animation.progress = 0.0f
            }
        }
        upvote_container.setOnClickListener {
            if (!animator.isRunning) {
                setUpvoted()
            }
        }

        layout_item_tanya_kandidat.visibleIf(true)
        FacebookEventLogger.logViewedContentEvent(
            this@DetailTanyaKandidatActivity,
            PantauConstants.TanyaKandidat.NAME,
            question.body, question.id
        )
    }

    private fun setUpvoted() {
        val upVoted = question?.isliked
        question?.isliked = !question?.isliked!!
        if (!upVoted!!) {
            question?.likeCount = question?.likeCount?.plus(1)!!
            animator.addUpdateListener { animation -> upvote_animation.progress = animation.animatedValue as Float
                upvote_count_text.text = question?.likeCount.toString()
            }
            animator.start()
        } else {
            question?.likeCount = question?.likeCount?.minus(1)!!
            upvote_count_text.text = question?.likeCount.toString()
            upvote_animation.progress = 0.0f
        }

        if (!upVoted) {
            presenter.upvoteQuestion(question?.id!!, upVoted)
        } else {
            presenter.unvoteQuestion(question?.id!!, upVoted)
        }
        setItemChangedResult()
    }

    private fun onClickOption() {
        val dialog = OptionDialog(this, R.layout.layout_option_dialog_common)
        if (question?.user?.id.equals(profile.id)) {
            dialog.removeItem(R.id.report_tanya_kandidat_action)
        } else {
            dialog.removeItem(R.id.delete_tanya_kandidat_item_action)
        }
        if (profile == EMPTY_PROFILE) {
            dialog.removeItem(R.id.report_tanya_kandidat_action)
        }
        dialog.show()
        dialog.listener = object : OptionDialog.DialogListener {
            override fun onClick(viewId: Int) {
                when (viewId) {
                    R.id.copy_url_tanya_kandidat_action -> {
                        onClickCopyUrl()
                        dialog.dismiss()
                    }
                    R.id.share_tanya_kandidat_action -> {
                        onClickShare()
                        dialog.dismiss()
                    }
                    R.id.report_tanya_kandidat_action -> {
                        onClickLapor()
                        dialog.dismiss()
                    }
                    R.id.delete_tanya_kandidat_item_action -> {
                        val deleteDialog = DeleteConfimationDialog(
                            this@DetailTanyaKandidatActivity, getString(R.string.txt_delete_item_ini),
                            listener = object : DeleteConfimationDialog.DialogListener {
                                override fun onClickDeleteItem(id: String, position: Int) {
                                    onClickDeleteItem()
                                }
                            })
                        deleteDialog.show()

                        dialog.dismiss()
                    }
                }
            }
        }
    }

    override fun onFailedGetData(throwable: Throwable) {
        view_fail_state.enableLottie(true, lottie_fail_state)
    }

    override fun onItemUpVoted() {
        setItemChangedResult()
        question?.body?.let {
            question?.id?.let { it1 ->
                FacebookEventLogger.logRatedEvent(
                    this@DetailTanyaKandidatActivity,
                    PantauConstants.TanyaKandidat.NAME,
                    it,
                    it1,
                    1,
                    1.0
                )
            }
        }
    }

    override fun onFailedUpVoteItem(liked: Boolean) {
        question?.isliked = liked
        question?.isliked?.let {
            if (it) {
                upvote_animation.progress = 1.0f
            } else {
                upvote_animation.progress = 0.0f
            }
        }
        setItemChangedResult()
    }

    override fun showItemReportedAlert() {
        ToastUtil.show(this, getString(R.string.berhasil_melaporkan_pertanyaan))
    }

    override fun showFailedReportItem() {
        ToastUtil.show(this, getString(R.string.gagal_melaporkan_pertanyaan))
    }

    override fun showFailedDeleteItemAlert() {
        dismissProgressDialog()
        ToastUtil.show(this, getString(R.string.gagal_menghapus_pertanyaan))
    }

    override fun onItemDeleted() {
        dismissProgressDialog()
        itemPosition?.let {
            intent.putExtra(EXTRA_ITEM_POSITION, it)
            setResult(RESULT_DELETE_ITEM_QUESTION, intent)
        }
//        val animator = ValueAnimator.ofFloat(0.0f, 1.0f).setDuration(1000)
//        if (!question?.isliked!!) {
//            val loveCount = question?.likeCount!! + 1
//            question?.likeCount = loveCount
//            animator.addUpdateListener { animation -> upvote_animation.progress = animation.animatedValue as Float
//                upvote_count_text.text = question?.likeCount.toString()
//            }
//            animator.start()
//        } else {
//            val upVoteCount = question?.likeCount!! - 1
//            question?.likeCount = upVoteCount
//            upvote_count_text.text = question?.likeCount.toString()
//            upvote_animation.progress = 0.0f
//        }
        onBackPressed()
    }

    override fun onClickCopyUrl() {
        question?.id?.let { CopyUtil.copyTanyaKandidat(this, it) }
    }

    override fun onClickShare() {
        ShareUtil.shareItem(this, question)
    }

    override fun onClickLapor() {
        question?.id?.let { presenter.reportQuestion(it) }
    }

    override fun onClickDeleteItem() {
        showProgressDialog(getString(R.string.txt_delete_item_ini))
        question?.id?.let { presenter.deleteItem(it) }
    }

    private fun setItemChangedResult() {
        val intent = Intent()
        intent.putExtra(EXTRA_ITEM_POSITION, itemPosition)
        intent.putExtra(EXTRA_QUESTION_ITEM, question)
        setResult(RESULT_ITEM_CHANGED_QUESTION, intent)
    }

    override fun onBackPressed() {
        if (this.isTaskRoot) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        super.onBackPressed()
    }
}
