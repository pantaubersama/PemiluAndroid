package com.pantaubersama.app.ui.debat

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.TextKeyListener
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver
import android.widget.* // ktlint-disable
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.firebase.messaging.FirebaseMessaging
import com.makeramen.roundedimageview.RoundedImageView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.debat.Challenge
import com.pantaubersama.app.data.model.debat.ChallengeConstants
import com.pantaubersama.app.data.model.debat.WordItem
import com.pantaubersama.app.data.model.debat.WordInputItem
import com.pantaubersama.app.data.remote.exception.ErrorException
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.debat.adapter.KomentarAdapter
import com.pantaubersama.app.ui.debat.adapter.WordsFighterAdapter
import com.pantaubersama.app.ui.debat.detail.DetailDebatDialogFragment
import com.pantaubersama.app.ui.widget.OptionDialogFragment
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_CHALLENGE_ITEM
import com.pantaubersama.app.utils.extensions.* // ktlint-disable
import com.pantaubersama.app.data.model.debat.ChallengeConstants.Role
import com.pantaubersama.app.utils.* // ktlint-disable
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_debat.*
import kotlinx.android.synthetic.main.layout_empty_state.*
import kotlinx.android.synthetic.main.layout_fail_state.*
import kotlinx.android.synthetic.main.layout_header_detail_debat.*
import kotlinx.android.synthetic.main.layout_komentar_debat.*
import kotlinx.android.synthetic.main.layout_loading_state.*
import kotlinx.android.synthetic.main.layout_status_debat.*
import kotlinx.android.synthetic.main.layout_toolbar_debat.*
import net.frakbot.jumpingbeans.JumpingBeans
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DebatActivity : BaseActivity<DebatPresenter>(), DebatView, WordsPNHandler.OnGotNewWordsListener {
    @Inject
    override lateinit var presenter: DebatPresenter

    override fun statusBarColor(): Int? = R.color.white
    override fun setLayout(): Int = R.layout.activity_debat

    private var jumpingBeans: JumpingBeans? = null

    private var isMainToolbarShown = true
    private var isAppbarExpanded = true
    private var isKeyboardShown = false
    private var mCoordinatorHeight = 0

    private lateinit var wordsFighterAdapter: WordsFighterAdapter
    private lateinit var komentarAdapter: KomentarAdapter

    private val myProfile by unSyncLazy { presenter.getMyProfile() }

    private lateinit var et_comment_main: EditText
    private lateinit var et_comment_in: EditText
    private lateinit var btn_comment_main: ImageView
    private lateinit var btn_comment_in: ImageView
    private lateinit var iv_avatar_comment_main: RoundedImageView
    private lateinit var iv_avatar_comment_in: RoundedImageView
    private lateinit var progress_bar_comment_main: ProgressBar
    private lateinit var progress_bar_comment_in: ProgressBar

    private var isWordsInputFocused = false
    private val optionDialog by unSyncLazy {
        OptionDialogFragment.newInstance(R.layout.layout_option_dialog_menguji)
    }

    private lateinit var challenge: Challenge
    private val isMyChallenge by unSyncLazy {
        presenter.getMyProfile().id in arrayOf(challenge.challenger.userId, challenge.opponent?.userId)
    }
    private val isDone by unSyncLazy { challenge.status == ChallengeConstants.Status.DONE }
    private val myRole by unSyncLazy {
        when (presenter.getMyProfile().id) {
            challenge.challenger.userId -> Role.CHALLENGER
            challenge.opponent?.userId -> Role.OPPONENT
            else -> Role.AUDIENCE
        }
    }
    private var isMyTurn: Boolean = false

    private val topicList by unSyncLazy {
        arrayOf(
            "android-fighter-${challenge.id}",
            "android-audience-${challenge.id}"
        )
    }

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    companion object {
        const val RUN_OUT_OF_TIME = "sudah habis"

        fun setIntent(context: Context, challenge: Challenge): Intent {
            val intent = Intent(context, DebatActivity::class.java)
            intent.putExtra(EXTRA_CHALLENGE_ITEM, challenge)
            return intent
        }
    }

    override fun fetchIntentExtra() {
        intent.getSerializableExtra(EXTRA_CHALLENGE_ITEM)?.let {
            challenge = it as Challenge
        }
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupLayoutBehaviour()
        setupHeader()
        setupArgumenList()
        setupKomentarList()
    }

    @SuppressLint("SetTextI18n")
    private fun setupHeader() {
        val challenger = challenge.challenger
        val opponent = challenge.opponent

        iv_avatar_challenger.loadUrl(challenger.avatar?.medium?.url, R.drawable.ic_avatar_placeholder)
        tv_name_challenger.text = challenger.fullName
        tv_username_challenger.text = "@${challenger.username}"

        iv_avatar_opponent.loadUrl(opponent?.avatar?.medium?.url, R.drawable.ic_avatar_placeholder)
        tv_name_opponent.text = opponent?.fullName
        tv_username_opponent.text = "@${opponent?.username}"

        tv_title.text = challenge.statement

        if (isMyChallenge && !isDone) {
            rl_sisa_waktu.visibleIf(true)
            tv_sisa_waktu.text = "menghitung.. menit"
        }

        if (isDone) {
            tv_toolbar_title.text = "Result"
            tv_toolbar_title.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            cl_clap_count.visibleIf(true)
            tv_clap_count_challenger_header.text = "${challenger.clapCount?.let { it } ?: "0"} total"
            tv_clap_count_opponent_header.text = "${opponent?.clapCount?.let { it } ?: "0"} total"
        }
    }

    private fun getList() {
        wordsFighterAdapter.setDataEnd(false)
        presenter.getWordsFighter(challenge.id, 1)
        presenter.getWordsAudience(challenge.id)
    }

    private fun setupArgumenList() {
        wordsFighterAdapter = WordsFighterAdapter(isMyChallenge)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = wordsFighterAdapter
        wordsFighterAdapter.listener = object : WordsFighterAdapter.AdapterListener {
            override fun onClickClap(words: WordItem) {
                presenter.putWordsClap(words.id)
            }

            override fun onWordsInputFocused(isFocused: Boolean) {
                if (isFocused) {
                    if (isAppbarExpanded) app_bar.setExpanded(false)
                    recycler_view.smoothScrollToPosition(0)
                    Completable.timer(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                        .subscribe { isWordsInputFocused = true }
                } else {
                    isWordsInputFocused = false
                }
            }

            override fun onPublish(words: String) {
                presenter.postWordsFighter(challenge.id, words)
            }
        }

        wordsFighterAdapter.addSupportLoadMore(recycler_view, 3) {
            wordsFighterAdapter.setLoading()
            presenter.getWordsFighter(challenge.id, it)
        }
    }

    private fun setupKomentarList() {
        komentarAdapter = KomentarAdapter()
        recycler_view_komentar.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recycler_view_komentar.adapter = komentarAdapter
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showWordsFighter(wordList: MutableList<WordItem>) {
        recycler_view.visibleIf(true)
        wordsFighterAdapter.setDatas(wordList)
        updateInputTurn()
        recycler_view.scrollToPosition(0)

        if (wordList.size < presenter.perPage) {
            wordsFighterAdapter.setDataEnd(true)
        }
    }

    override fun showMoreWordsFighter(wordList: MutableList<WordItem>) {
        wordsFighterAdapter.setLoaded()
        if (wordList.size < presenter.perPage) {
            wordsFighterAdapter.setDataEnd(true)
        }
        wordsFighterAdapter.addData(wordList)
    }

    override fun showLoadingWordsFighter() {
        val layoutParams = FrameLayout.LayoutParams(dip(200), dip(150))
        layoutParams.topMargin = dip(100)
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL
        lottie_loading.layoutParams = layoutParams
        lottie_loading.enableLottie(true, lottie_loading)
        view_empty_state.enableLottie(false, lottie_empty_state)
        view_fail_state.enableLottie(false, lottie_fail_state)
        recycler_view.visibleIf(false)
    }

    override fun dismissLoadingWordsFighter() {
        lottie_loading.enableLottie(false, lottie_loading)
    }

    override fun onEmptyWordsFighter() {
        if (!isMyChallenge || isDone) view_empty_state.enableLottie(true, lottie_empty_state) else recycler_view.visibleIf(true)
        if (view_empty_state.isVisible()) {
            val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 4f)
            layoutParams.marginStart = dip(70)
            layoutParams.marginEnd = dip(70)
            layoutParams.topMargin = dip(20)
            tv_empty_state.layoutParams = layoutParams
            tv_empty_state.text = "belum ada perdebatan"
        }
        updateInputTurn()
    }

    override fun onErrorGetWordsFighter(t: Throwable) {
        view_fail_state.enableLottie(true, lottie_fail_state)
        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 4f)
        layoutParams.marginStart = dip(70)
        layoutParams.marginEnd = dip(70)
        layoutParams.topMargin = dip(20)
        tv_fail_state.layoutParams = layoutParams
        tv_fail_state.text = t.message
    }

    override fun onErrorGetMoreWordsFighter(t: Throwable) {
        wordsFighterAdapter.setLoaded()
        if (t.message?.contains("expected :page") == true) wordsFighterAdapter.setDataEnd(true)
    }

    override fun showKomentar(komentarList: MutableList<WordItem>) {
        recycler_view_komentar.visibleIf(true)
        komentarAdapter.setDatas(komentarList)
        if (isMyChallenge || isDone) {
            val commentPreviewText = spannable {
                komentarList.last().author.fullName?.let { bold { textColor(color(R.color.black_2)) { +it } } }
                + "  "
                + komentarList.last().body
            }.toCharSequence()
            et_comment_main.setText(commentPreviewText)
            et_comment_main.inputType = InputType.TYPE_NULL
            et_comment_main.maxLines = 2
            et_comment_main.setTextColor(color(R.color.black_1))
            iv_avatar_comment_main.visibleIf(true)
            komentarList.last().author.avatar?.medium?.url?.let { iv_avatar_comment_main.loadUrl(it, R.drawable.ic_avatar_placeholder) }
                ?: iv_avatar_comment_main.setImageResource(R.drawable.ic_avatar_placeholder)
        }
        recycler_view_komentar.scrollToPosition(komentarAdapter.itemCount - 1)
    }

    override fun onEmptyWordsAudience() {
        if (isMyChallenge || isDone) {
            et_comment_main.setText((if (!isDone) "Belum" else "Tidak") + " ada komentar")
            et_comment_main.inputType = InputType.TYPE_NULL
            iv_avatar_comment_main.setImageDrawable(null)
            et_comment_main.setTextColor(color(R.color.gray))
        }

        tv_comment_in_state.text = (if (!isDone) "Belum" else "Tidak") + " ada komentar"
        tv_comment_in_state.visibleIf(true)
    }

    override fun onErrorGetWordsAudience(t: Throwable) {
        if (isMyChallenge || isDone) {
            et_comment_main.setText("Gagal memuat komentar")
            et_comment_main.inputType = InputType.TYPE_NULL
            iv_avatar_comment_main.setImageDrawable(null)
            et_comment_main.setTextColor(color(R.color.gray))
        }

        tv_comment_in_state.text = "Gagal memuat komentar"
        tv_comment_in_state.visibleIf(true)
    }

    override fun showLoadingKomentar() {
        if (isMyChallenge || isDone) {
            progress_bar_comment_main.visibleIf(true)
            et_comment_main.visibleIf(false, true)
            iv_avatar_comment_main.visibleIf(false, true)
        }

        progress_bar_comment_in.visibleIf(true)
        tv_comment_in_state.visibleIf(false)
        recycler_view_komentar.visibleIf(false)
    }

    override fun dismissLoadingKomentar() {
        if (isMyChallenge || isDone) {
            progress_bar_comment_main.visibleIf(false)
            et_comment_main.visibleIf(true)
        }

        progress_bar_comment_in.visibleIf(false)
    }

    private fun addKomentar(words: String) {
        presenter.postWordsAudience(challenge.id, words)
        layout_box_komentar_in.isEnabled = false
        et_comment_main.isEnabled = false
    }

    override fun onSuccessPostWordsAudience(wordItem: WordItem) {
        if (tv_comment_in_state.isVisible()) tv_comment_in_state.visibleIf(false)
        if (!recycler_view_komentar.isVisible()) recycler_view_komentar.visibleIf(true)
        komentarAdapter.addItem(wordItem)

        TextKeyListener.clear(et_comment_main.text)
        TextKeyListener.clear(et_comment_in.text)
        if (et_comment_main.isFocused) et_comment_main.clearFocus()
        if (et_comment_in.isFocused) et_comment_in.clearFocus()

        layout_box_komentar_in.isEnabled = true
        et_comment_main.isEnabled = true
    }

    override fun onFailedPostWordsAudience(t: Throwable) {
        layout_box_komentar_in.isEnabled = true
        et_comment_main.isEnabled = true
    }

    override fun onSuccessWordsClap() {
        // do nothing
    }

    override fun onErrorWordsClap(t: Throwable) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateInputTurn() {
        if (!isDone) {
//            TODO("UBAH BUKAN BERDASARKAN JUMLAH ITEM TAPI ITEM TERAKHIR")
            val turnActorRole =
                if (wordsFighterAdapter.itemCount != 0) {
                    when {
                        wordsFighterAdapter.get(0) is WordItem -> if ((wordsFighterAdapter.get(0) as WordItem).author.role == Role.CHALLENGER) Role.OPPONENT else Role.CHALLENGER
                        wordsFighterAdapter.get(1) != null -> if ((wordsFighterAdapter.get(1) as WordItem).author.role == Role.CHALLENGER) Role.OPPONENT else Role.CHALLENGER
                        else -> Role.CHALLENGER
                    }
                } else {
                    Role.CHALLENGER
                }

            isMyTurn = myRole == turnActorRole && wordsFighterAdapter.getMyTimeLeft(myRole) != null

            if (isMyChallenge) {
                if ((wordsFighterAdapter.itemCount == 0 || wordsFighterAdapter.get(0) !is WordInputItem)) {
                    showWordsInputBox()
                } else {
                    wordsFighterAdapter.clearInputMessage(isMyTurn)
                }
            }

            val turnActorName = when (turnActorRole) {
                myRole -> "Kamu"
                Role.CHALLENGER -> challenge.challenger.fullName
                Role.OPPONENT -> challenge.opponent?.fullName
                else -> throw ErrorException("unknown turnActorRole $turnActorRole")
            }

            tv_status_debat.text = "Giliran $turnActorName menulis argumen "
            enableJumpingDots(true)
        } else {
            enableJumpingDots(false)
            tv_status_debat.text = "Debat sudah selesai"
        }
    }

    override fun updateMyTimeLeft() {
        if (isMyChallenge && !isDone) {
            tv_sisa_waktu.text = wordsFighterAdapter.getMyTimeLeft(myRole)?.let { if (it > 0) "$it MENIT" else RUN_OUT_OF_TIME }
                ?: "${challenge.timeLimit} MENIT"
        }
        invalidateToolbar()
    }

    override fun onSuccessPostWordsFighter(wordItem: WordItem) {
        wordsFighterAdapter.clearInputMessage(false)
        wordsFighterAdapter.addItem(wordItem)
        updateInputTurn()
    }

    override fun onFailedPostWordsFighter(t: Throwable) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        wordsFighterAdapter.clearInputMessage(isMyTurn)
    }

    override fun gotNewArgumen(word: WordItem) {
        if (!recycler_view.isVisible()) recycler_view.visibleIf(true)
        if (view_empty_state.isVisible()) view_empty_state.enableLottie(false, lottie_empty_state)
        if (view_fail_state.isVisible()) view_fail_state.enableLottie(false, lottie_fail_state)
        wordsFighterAdapter.addItem(word)
        updateInputTurn()
    }

    override fun gotNewComment(word: WordItem) {
        if (tv_comment_in_state.isVisible()) tv_comment_in_state.visibleIf(false)
        if (!recycler_view_komentar.isVisible()) recycler_view_komentar.visibleIf(true)
        komentarAdapter.addItem(word)

        if (isMyChallenge || isDone) {
            val commentPreviewText = spannable {
                word.author.fullName?.let { bold { textColor(color(R.color.black_2)) { +it } } }
                + "  "
                + word.body
            }.toCharSequence()
            et_comment_main.setText(commentPreviewText)
            et_comment_main.inputType = InputType.TYPE_NULL
            et_comment_main.maxLines = 2
            et_comment_main.setTextColor(color(R.color.black_1))
            iv_avatar_comment_main.visibleIf(true)
            word.author.avatar?.medium?.url?.let { iv_avatar_comment_main.loadUrl(it, R.drawable.ic_avatar_placeholder) }
                ?: iv_avatar_comment_main.setImageResource(R.drawable.ic_avatar_placeholder)
        }
    }

    override fun showLoading() {
        showProgressDialog(getString(R.string.txt_mohon_tunggu))
    }

    override fun dismissLoading() {
        dismissProgressDialog()
    }

    private fun showWordsInputBox() {
        wordsFighterAdapter.addWordsInputItem(myRole, isMyTurn)
    }

    @SuppressLint("SetTextI18n")
    private fun setupLayoutBehaviour() {
        layout_box_komentar_main.apply {
            et_comment_main = findViewById(R.id.et_comment)
            btn_comment_main = findViewById(R.id.iv_btn_comment)
            iv_avatar_comment_main = findViewById(R.id.iv_avatar_me)
            progress_bar_comment_main = findViewById(R.id.progress_bar_comment)
        }
        layout_komentar_debat.apply {
            et_comment_in = findViewById(R.id.et_comment)
            btn_comment_in = findViewById(R.id.iv_btn_comment)
            iv_avatar_comment_in = findViewById(R.id.iv_avatar_me)
            progress_bar_comment_in = findViewById(R.id.progress_bar_comment_in)
        }

        lateinit var commentInTextWatcher: TextWatcher
        lateinit var commentMainTextWatcher: TextWatcher

        btn_back.setOnClickListener { onBackPressed() }

        tv_toolbar_title.setOnClickListener {
            if (!isAppbarExpanded) app_bar.setExpanded(true)
        }

        app_bar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (verticalOffset < -30) {
                if (isMainToolbarShown) {
                    isMainToolbarShown = false
                    val toolbarBackground = TransitionDrawable(arrayOf(toolbar_debat.background, ColorDrawable(ContextCompat.getColor(this, R.color.yellow))))
                    toolbar_debat.background = toolbarBackground.also { it.startTransition(150) }

                    if (isMyChallenge && !isDone) {
                        tv_toolbar_title.text = wordsFighterAdapter.getMyTimeLeft(myRole)?.let { if (it > 0) "$it menit tersisa" else "saldo waktu sudah habis" } ?: "${challenge.timeLimit} MENIT"
                        tv_toolbar_title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_saldo_waktu_white_24, 0, 0, 0)
                    }
                }
            } else if (verticalOffset == 0) {
                if (!isMainToolbarShown) {
                    isMainToolbarShown = true
                    val toolbarBackground = TransitionDrawable(arrayOf(toolbar_debat.background, ColorDrawable(ContextCompat.getColor(this, R.color.yellowAlpha))))
                    toolbar_debat.background = toolbarBackground.also {
                        it.isCrossFadeEnabled = true
                        it.reverseTransition(150)
                    }

                    if (isMyChallenge && !isDone) {
                        tv_toolbar_title.text = getString(R.string.live_now)
                        tv_toolbar_title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_debat_live, 0, 0, 0)
                    }
                }
            }
            adjustContentLayoutHeight(appBarLayout.bottom)
        })

        app_bar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            appBarLayout?.totalScrollRange?.let {
                isAppbarExpanded = Math.abs(verticalOffset) - it != 0
            }
        })

        btn_more_toolbar.setOnClickListener {
            optionDialog.setViewVisibility(R.id.delete_action, false)
            optionDialog.listener = View.OnClickListener {
                when (it.id) {
                    R.id.copy_link_action -> CopyUtil.copyChallenge(it.context, challenge)
                    R.id.share_action -> ShareUtil.shareItem(it.context, challenge)
                }
                optionDialog.dismiss()
            }
            optionDialog.show(supportFragmentManager, "dialog")
        }

        cl_btn_detail_debat.setOnClickListener {
            DetailDebatDialogFragment.show(supportFragmentManager, challenge)
        }

        fab_scroll_to_bottom.setOnClickListener {
            if (isAppbarExpanded) {
                app_bar.setExpanded(false)
            }
            recycler_view.smoothScrollToPosition(0)
            fab_scroll_to_bottom.hide()
        }

        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                showFAB(recyclerView.canScrollVertically(1))
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy < 0) { // scrolling up
                    Timber.d("onScrolled hideKeyboard() isWordInputFocused : $isWordsInputFocused")
                    Timber.d("onScrolled hideKeyboard() isKeyboardShown : $isKeyboardShown")
                    if (isKeyboardShown && isWordsInputFocused) {
                        hideKeyboard(this@DebatActivity)
                    }
                }
            }
        })

        // Wait for just before drawing the view to get its height.
        coordinator.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                coordinator.viewTreeObserver.removeOnPreDrawListener(this)
                mCoordinatorHeight = coordinator.height
                adjustContentLayoutHeight(app_bar.bottom)
                return false
            }
        })

        /**
         * Komentar Main Layout behaviour
         */
        layout_box_komentar_in.visibleIf(!isMyChallenge && !isDone)
        if (isMyChallenge || isDone) {
            et_comment_main.isFocusable = false
        } else {
            getString(R.string.tulis_komentar).apply {
                et_comment_main.hint = this
                et_comment_in.hint = this
            }
            iv_avatar_comment_main.loadUrl(myProfile.avatar?.medium?.url, R.drawable.ic_avatar_placeholder)
            iv_avatar_comment_in.loadUrl(myProfile.avatar?.medium?.url, R.drawable.ic_avatar_placeholder)
        }
        btn_comment_main.setOnClickListener {
            if (isKeyboardShown) {
                et_comment_main.text.apply {
                    if (toString().isNotEmpty()) {
                        addKomentar(toString())
                    }
                }
            } else {
                sliding_layout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
            }
        }

        /**
         * Komentar Sliding Layout behaviour
         */
        sliding_layout.apply {
            setFadeOnClickListener { panelState = SlidingUpPanelLayout.PanelState.COLLAPSED }
        }

        sliding_layout.addPanelSlideListener(object : SlidingUpPanelLayout.PanelSlideListener {
            override fun onPanelSlide(panel: View?, slideOffset: Float) {}

            override fun onPanelStateChanged(panel: View?, previousState: SlidingUpPanelLayout.PanelState?, newState: SlidingUpPanelLayout.PanelState?) {
                if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED && KeyboardVisibilityEvent.isKeyboardVisible(this@DebatActivity)) {
                    hideKeyboard(this@DebatActivity)
                }
            }
        })

        layout_komentar_debat.setOnClickListener(null)

        layout_box_komentar_main.post {
            adjustRvPadding()
        }

        btn_comment_main.setImageResource(R.drawable.ic_arrow_expand_more)

        KeyboardVisibilityEvent.setEventListener(this@DebatActivity) { isOpen ->
            isKeyboardShown = isOpen
            if (isOpen) {
                showFAB(false)
                if (isWordsInputFocused && layout_box_komentar_main.isVisible() || isMyChallenge) {
                    layout_box_komentar_main.visibleIf(false)
                    adjustRvPadding(true)
                } else {
                    btn_comment_main.setImageResource(R.drawable.ic_send)
                }
            } else {
                if (!layout_box_komentar_main.isVisible()) {
                    layout_box_komentar_main.visibleIf(true)
                    adjustRvPadding()
                }
                btn_comment_main.setImageResource(R.drawable.ic_arrow_expand_more)
                adjustContentLayoutHeight(app_bar.height)
            }
        }

        recycler_view_komentar.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (recyclerView.canScrollVertically(1)) {
                    tv_comment_below_available.animate().alpha(1f).duration = 200
                } else {
                    tv_comment_below_available.animate().alpha(0f).duration = 200
                }
            }
        })
        tv_comment_below_available.setOnClickListener {
            recycler_view_komentar.smoothScrollToPosition(komentarAdapter.itemCount - 1)
        }

        btn_comment_in.setOnClickListener {
            et_comment_in.text.apply {
                if (toString().isNotEmpty()) {
                    addKomentar(toString())
                }
            }
        }

        commentInTextWatcher = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(query: CharSequence?, p1: Int, p2: Int, p3: Int) {
                et_comment_main.removeTextChangedListener(commentMainTextWatcher)
                et_comment_main.setText(query)
                et_comment_main.addTextChangedListener(commentMainTextWatcher)
            }
        }

        commentMainTextWatcher = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(query: CharSequence?, p1: Int, p2: Int, p3: Int) {
                et_comment_in.removeTextChangedListener(commentInTextWatcher)
                et_comment_in.setText(query)
                et_comment_in.addTextChangedListener(commentInTextWatcher)
            }
        }

        et_comment_main.addTextChangedListener(commentMainTextWatcher)
        et_comment_in.addTextChangedListener(commentInTextWatcher)
    }

    private fun showFAB(showing: Boolean) {
        if (showing) {
            if (!fab_scroll_to_bottom.isShown && recycler_view.canScrollVertically(1) && !isKeyboardShown) {
                val fabLayoutParams = CoordinatorLayout.LayoutParams(dip(40), dip(40))
                fabLayoutParams.gravity = Gravity.BOTTOM or Gravity.END
                fabLayoutParams.setMargins(0, 0, dip(16), layout_box_komentar_main.height + dip(8))
                fab_scroll_to_bottom.layoutParams = fabLayoutParams
                fab_scroll_to_bottom.show()
            }
        } else {
            if (fab_scroll_to_bottom.isShown) fab_scroll_to_bottom.hide()
        }
    }

    private fun invalidateToolbar() {
        if (isMyChallenge && !isDone) {
            if (!isMainToolbarShown) {
                tv_toolbar_title.text = wordsFighterAdapter.getMyTimeLeft(myRole)?.let { if (it > 0) "$it menit tersisa" else "saldo waktu sudah habis" } ?: "${challenge.timeLimit} MENIT"
                tv_toolbar_title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_saldo_waktu_white_24, 0, 0, 0)
            }
        }
    }

    private fun adjustContentLayoutHeight(newHeight: Int) {
        if (!isKeyboardShown && recycler_view.isVisible()) recycler_view.setPadding(0, 0, 0, newHeight)
    }

    private fun adjustRvPadding(removePadding: Boolean = false) {
        if (recycler_view.isVisible()) {
            if (removePadding) {
                recycler_view.setPadding(0, 0, 0, 0)
            } else {
                recycler_view.setPadding(0, 0, 0, layout_box_komentar_main.height)
            }
        }
    }

    private fun enableJumpingDots(enable: Boolean = true) {
        if (enable && tv_status_debat.text.isNotEmpty() && !isDone) {
            jumpingBeans = JumpingBeans.with(tv_status_debat)
                .appendJumpingDots()
                .build()
        } else {
            jumpingBeans?.stopJumping()
        }
    }

    override fun onResume() {
        super.onResume()

        presenter.setOnGotNewWordsListener(this)

        if (!isDone) {
            enableJumpingDots()
        }

        topicList.forEach {
            FirebaseMessaging.getInstance().subscribeToTopic(it)
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        val msg = "FCM ERROR - Failed subscribing $it - ${task.exception}"
                        Log.e("FCM ERROR", msg)
                    }
                }
        }

        getList()
    }

    override fun onPause() {
        presenter.setOnGotNewWordsListener(null)

        if (!isDone) {
            enableJumpingDots(false)
        }

        topicList.forEach {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(it)
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        val msg = "FCM ERROR - Failed unsubscribing $it - ${task.exception}"
                        Log.e("FCM ERROR", msg)
                    }
                }
        }

        super.onPause()
    }

    override fun onBackPressed() {
        if (sliding_layout.panelState == SlidingUpPanelLayout.PanelState.EXPANDED || sliding_layout.panelState == SlidingUpPanelLayout.PanelState.ANCHORED) {
            sliding_layout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
        } else {
            super.onBackPressed()
        }
    }
}
