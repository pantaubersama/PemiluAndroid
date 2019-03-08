package com.pantaubersama.app.ui.debat

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.text.method.TextKeyListener
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
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
import com.pantaubersama.app.data.model.debat.Komentar
import com.pantaubersama.app.data.model.debat.WordItem
import com.pantaubersama.app.data.model.debat.WordInputItem
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.data.remote.exception.ErrorException
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.debat.adapter.KomentarAdapter
import com.pantaubersama.app.ui.debat.adapter.WordsFighterAdapter
import com.pantaubersama.app.ui.debat.detail.DetailDebatDialogFragment
import com.pantaubersama.app.ui.widget.OptionDialogFragment
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_CHALLENGE_ITEM
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.* // ktlint-disable
import com.pantaubersama.app.utils.hideKeyboard
import com.pantaubersama.app.data.model.debat.ChallengeConstants.Role
import com.sothree.slidinguppanel.SlidingUpPanelLayout
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
import javax.inject.Inject

class DebatActivity : BaseActivity<DebatPresenter>(), DebatView {
    @Inject
    override lateinit var presenter: DebatPresenter

    override fun statusBarColor(): Int? = R.color.white
    override fun setLayout(): Int = R.layout.activity_debat

    private var jumpingBeans: JumpingBeans? = null

    private var isMainToolbarShown = true
    private var isAppbarExpanded = true
    private var isKeyboardShown = false

//    private lateinit var messageSortedAdapter: MessageSortedAdapter
    private lateinit var wordsFighterAdapter: WordsFighterAdapter
    private lateinit var komentarAdapter: KomentarAdapter

    private lateinit var myProfile: Profile

    private lateinit var et_comment_main: EditText
    private lateinit var et_comment_in: EditText
    private lateinit var btn_comment_main: ImageView
    private lateinit var btn_comment_in: ImageView
    private lateinit var iv_avatar_comment_main: RoundedImageView
    private lateinit var iv_avatar_comment_in: RoundedImageView

    private var isWordsInputFocused = false
    private val optionDialog by unSyncLazy {
        OptionDialogFragment.newInstance(R.layout.layout_option_dialog_menguji)
    }

    private lateinit var challenge: Challenge
    private val isMyChallenge by unSyncLazy {
        presenter.getMyProfile().id in arrayOf(challenge.challenger.userId, challenge.opponent?.userId)
    }
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
        myProfile = presenter.getMyProfile()

        iv_avatar_comment_main.loadUrl(myProfile.avatar?.medium?.url, R.color.gray_3)
        iv_avatar_comment_in.loadUrl(myProfile.avatar?.medium?.url, R.color.gray_3)

        setupDebatList()
        setupKomentarList()

        getList()
    }

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
    }

    private fun addKomentar(komentar: Komentar) {
        komentarAdapter.addItem(komentar)
    }

    private fun getList() {
        presenter.getWordsFighter(challenge.id)
//        presenter.getKomentar()
    }

    private fun setupDebatList() {
        wordsFighterAdapter = WordsFighterAdapter()
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = wordsFighterAdapter
        wordsFighterAdapter.listener = object : WordsFighterAdapter.AdapterListener {
            override fun onClickClap() {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onWordsInputFocused(isFocused: Boolean) {
                isWordsInputFocused = isFocused
                if (isFocused && isAppbarExpanded) {
                    app_bar.setExpanded(false)
                    recycler_view.smoothScrollToPosition(0)
                }
            }

            override fun onPublish(words: String) {
                presenter.postWordsFighter(challenge.id, words)
            }
        }

        swipe_refresh.setOnRefreshListener {
            getList()
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
//        recycler_view.scrollToPosition(wordsFighterAdapter.itemCount - 1)

        Handler().postDelayed({
            showFAB(true)
        }, 500)
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
        if (swipe_refresh.isRefreshing) swipe_refresh.isRefreshing = false
    }

    override fun onEmptyWordsFighter() {
        if (!isMyChallenge) view_empty_state.enableLottie(true, lottie_empty_state)
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

    override fun showKomentar(komentarList: MutableList<Komentar>) {
        komentarAdapter.setDatas(komentarList)
    }

    override fun updateInputTurn() {
        val turnActorRole = if (!wordsFighterAdapter.itemCount.isOdd()) {
            if (isMyChallenge) {
                Role.CHALLENGER
            } else Role.OPPONENT
        } else {
            if (isMyChallenge) {
                Role.OPPONENT
            } else Role.CHALLENGER
        }
        val turnActorName = when (turnActorRole) {
            myRole -> "Kamu"
            Role.CHALLENGER -> challenge.challenger.fullName
            Role.OPPONENT -> challenge.opponent?.fullName
            else -> throw ErrorException("unknown turnActorRole $turnActorRole")
        }

        isMyTurn = myRole == turnActorRole

        if (isMyChallenge && wordsFighterAdapter.get(0) !is WordInputItem) showWordsInputBox()

        tv_status_debat.text = "Giliran $turnActorName menulis argumen "
        enableJumpingDots(true)
    }

    override fun onSuccessPostWordsFighter(wordItem: WordItem) {
        wordsFighterAdapter.clearInputMessage(false)
        wordsFighterAdapter.addItem(wordItem, 1)
        updateInputTurn()
    }

    override fun onFailedPostWordsFighter(wordItem: WordItem) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLoading() {
        showProgressDialog(getString(R.string.txt_mohon_tunggu))
    }

    override fun dismissLoading() {
        dismissProgressDialog()
    }

    override fun showLoadingKomentar() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoadingKomentar() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun showWordsInputBox() {
        Timber.d("showWordsInputBox isMyTurn = $isMyTurn")
        wordsFighterAdapter.addWordsInputItem(myRole, isMyTurn)
    }

    @SuppressLint("SetTextI18n")
    private fun setupLayoutBehaviour() {
        et_comment_main = layout_box_komentar_main.findViewById(R.id.et_comment)
        et_comment_in = layout_komentar_debat.findViewById(R.id.et_comment)
        btn_comment_main = layout_box_komentar_main.findViewById(R.id.iv_btn_comment)
        btn_comment_in = layout_komentar_debat.findViewById(R.id.iv_btn_comment)
        iv_avatar_comment_main = layout_box_komentar_main.findViewById(R.id.iv_avatar_me)
        iv_avatar_comment_in = layout_komentar_debat.findViewById(R.id.iv_avatar_me)

        lateinit var commentInTextWatcher: TextWatcher
        lateinit var commentMainTextWatcher: TextWatcher

        btn_back.setOnClickListener { onBackPressed() }

        app_bar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            if (verticalOffset < -30) {
                if (isMainToolbarShown) {
                    isMainToolbarShown = false
                    val toolbarBackground = TransitionDrawable(arrayOf(toolbar_debat.background, ColorDrawable(ContextCompat.getColor(this, R.color.yellow))))
                    toolbar_debat.background = toolbarBackground.also { it.startTransition(150) }

                    tv_toolbar_title.text = "${tv_sisa_waktu.text.toString().toLowerCase()} tersisa"
                    tv_toolbar_title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_saldo_waktu_white_24, 0, 0, 0)
                }
            } else if (verticalOffset == 0) {
                if (!isMainToolbarShown) {
                    isMainToolbarShown = true
                    val toolbarBackground = TransitionDrawable(arrayOf(toolbar_debat.background, ColorDrawable(ContextCompat.getColor(this, R.color.yellowAlpha))))
                    toolbar_debat.background = toolbarBackground.also {
                        it.isCrossFadeEnabled = true
                        it.reverseTransition(150)
                    }

                    tv_toolbar_title.text = getString(R.string.live_now)
                    tv_toolbar_title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_debat_live, 0, 0, 0)
                }
            }
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
                    R.id.copy_link_action -> ToastUtil.show(it.context, "Salin Tautan")
                    R.id.share_action -> ToastUtil.show(it.context, "Bagikan")
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
        })

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
                if (isWordsInputFocused && layout_box_komentar_main.isVisible()) {
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
            }
        }

        btn_comment_main.setOnClickListener {
            if (isKeyboardShown) {
                et_comment_main.text.apply {
                    if (this.toString().isNotEmpty()) {
                        val komentar = Komentar(System.currentTimeMillis().toString(), this.toString(), "Baru saja", presenter.getMyProfile())
                        addKomentar(komentar)
                        TextKeyListener.clear(this)
                        et_comment_main.clearFocus()
                    }
                }
            } else {
                sliding_layout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
            }
        }

        btn_comment_in.setOnClickListener {
            et_comment_in.text.apply {
                if (this.toString().isNotEmpty()) {
                    val komentar = Komentar(System.currentTimeMillis().toString(), this.toString(), "Baru saja", presenter.getMyProfile())
                    addKomentar(komentar)
                    TextKeyListener.clear(this)
                    et_comment_in.clearFocus()
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

    private fun adjustRvPadding(removePadding: Boolean = false) {
        if (removePadding) {
            recycler_view.setPadding(0, 0, 0, 0)
        } else {
            recycler_view.setPadding(0, 0, 0, layout_box_komentar_main.height)
        }
    }

    private fun enableJumpingDots(enable: Boolean = true) {
        if (enable && tv_status_debat.text.isNotEmpty()) {
            jumpingBeans = JumpingBeans.with(tv_status_debat)
                .appendJumpingDots()
                .build()
        } else {
            jumpingBeans?.stopJumping()
        }
    }

    override fun onResume() {
        super.onResume()
        enableJumpingDots()

        topicList.forEach {
            FirebaseMessaging.getInstance().subscribeToTopic(it)
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        val msg = "FCM ERROR - Failed subscribing $it - ${task.exception}"
                        Log.e("FCM ERROR", msg)
                    }
                }
        }
    }

    override fun onPause() {
        enableJumpingDots(false)

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
