package com.pantaubersama.app.ui.debat

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.TextKeyListener
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.makeramen.roundedimageview.RoundedImageView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.debat.Komentar
import com.pantaubersama.app.data.model.debat.MessageItem
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.debat.adapter.KomentarAdapter
import com.pantaubersama.app.ui.debat.adapter.MessageSortedAdapter
import com.pantaubersama.app.ui.widget.OptionDialog
import com.pantaubersama.app.utils.extensions.dip
import com.pantaubersama.app.utils.extensions.isVisible
import com.pantaubersama.app.utils.extensions.loadUrl
import com.pantaubersama.app.utils.extensions.visibleIf
import com.pantaubersama.app.utils.hideKeyboard
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.activity_debat.*
import kotlinx.android.synthetic.main.layout_header_detail_debat.*
import kotlinx.android.synthetic.main.layout_komentar_debat.*
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

    private lateinit var jumpingBeans: JumpingBeans

    private lateinit var messageSortedAdapter: MessageSortedAdapter
    private lateinit var komentarAdapter: KomentarAdapter

    private lateinit var myProfile: Profile

    private lateinit var et_comment_main: EditText
    private lateinit var et_comment_in: EditText
    private lateinit var btn_comment_main: ImageView
    private lateinit var btn_comment_in: ImageView
    private lateinit var iv_avatar_comment_main: RoundedImageView
    private lateinit var iv_avatar_comment_in: RoundedImageView

    private var isMessageInputFocused = false

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupLayoutBehaviour()
        myProfile = presenter.getMyProfile()

        iv_avatar_comment_main.loadUrl(myProfile.avatar?.medium?.url, R.color.gray_3)
        iv_avatar_comment_in.loadUrl(myProfile.avatar?.medium?.url, R.color.gray_3)

        setupDebatList()
        setupKomentarList()

        getList()
    }

    private fun addKomentar(komentar: Komentar) {
        komentarAdapter.addItem(komentar)
    }

    private fun getList() {
        presenter.getMessage()
        presenter.getKomentar()
    }

    private fun setupDebatList() {
        messageSortedAdapter = MessageSortedAdapter()
        recycler_view.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, true)
        recycler_view.adapter = messageSortedAdapter
        messageSortedAdapter.listener = object : MessageSortedAdapter.AdapterListener {
            override fun onClickClap() {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onMessageInputFocused(isFocused: Boolean) {
                isMessageInputFocused = isFocused
            }

            override fun onPublish(content: String) {
                presenter.postMessage(content)
            }
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

    override fun showMessage(messageList: MutableList<MessageItem>) {
        messageList.forEach {
            Timber.d("messageList : $it")
        }

        messageSortedAdapter.setDatas(messageList)
        for (i in 0 until messageSortedAdapter.getDatas()?.size()!!) {
            Timber.d("messageList getDatas $i : ${messageSortedAdapter.getData(i)}")
        }
    }

    override fun showKomentar(komentarList: MutableList<Komentar>) {
        komentarAdapter.setDatas(komentarList)
    }

    override fun onSuccessPostMessage(messageItem: MessageItem) {
        Timber.d("messageList getDatas before MESSAGE_INPUT_LEFT == : ${messageSortedAdapter.getData(messageSortedAdapter.itemCount - 1)}")
        messageSortedAdapter.updateItem(MessageItem("msg-input-left", "", Profile(), false, 0, 0, MessageItem.Type.INPUT_LEFT_SIDE), messageSortedAdapter.itemCount - 1)
//        Timber.d("messageList getDatas MESSAGE_INPUT_LEFT == : $MESSAGE_INPUT_LEFT")
        Timber.d("messageList getDatas after MESSAGE_INPUT_LEFT == : ${messageSortedAdapter.getData(messageSortedAdapter.itemCount - 1)}")
        messageSortedAdapter.addItem(messageItem)
        Timber.d("messageList getDatas onSuccessPostMessage ===")
        for (i in 0 until messageSortedAdapter.getDatas()?.size()!!) {
            Timber.d("messageList getDatas $i : ${messageSortedAdapter.getData(i)}")
        }
    }

    override fun onFailedPostMessage(messageItem: MessageItem) {
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

    override fun onBackPressed() {
        if (sliding_layout.panelState == SlidingUpPanelLayout.PanelState.EXPANDED || sliding_layout.panelState == SlidingUpPanelLayout.PanelState.ANCHORED) {
            sliding_layout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
        } else {
            super.onBackPressed()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupLayoutBehaviour() {
        var isMainToolbarShown = true
        var isKeyboardShown = false
        var isAppbarExpanded = true

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
                    Timber.d("OffsetChangedTransition - startTransition")
                    toolbar_debat.background = toolbarBackground.also { it.startTransition(150) }

                    tv_toolbar_title.text = "${tv_sisa_waktu.text.toString().toLowerCase()} tersisa"
                    tv_toolbar_title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_saldo_waktu_white_24, 0, 0, 0)
                }
            } else if (verticalOffset == 0) {
                if (!isMainToolbarShown) {
                    isMainToolbarShown = true
                    val toolbarBackground = TransitionDrawable(arrayOf(toolbar_debat.background, ColorDrawable(ContextCompat.getColor(this, R.color.yellowAlpha))))
                    Timber.d("OffsetChangedTransition - reverseTransition")
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
            val dialog = OptionDialog(this, R.layout.layout_option_dialog_common)
            dialog.removeItem(R.id.delete_tanya_kandidat_item_action)
            dialog.removeItem(R.id.report_tanya_kandidat_action)
            dialog.show()
        }

        cl_btn_detail_debat.setOnClickListener {
            DetailDebatDialogFragment.show(supportFragmentManager)
        }

        fab_scroll_to_bottom.setOnClickListener {
            if (isAppbarExpanded) {
                app_bar.setExpanded(false)
            }
            recycler_view.smoothScrollToPosition(messageSortedAdapter.itemCount - 1)
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
            showFAB(true)
        }

        btn_comment_main.setImageResource(R.drawable.ic_arrow_expand_more)

        KeyboardVisibilityEvent.setEventListener(this@DebatActivity) { isOpen ->
            isKeyboardShown = isOpen
            if (isOpen) {
                showFAB(false)
                if (isMessageInputFocused && layout_box_komentar_main.isVisible()) {
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
            if (!fab_scroll_to_bottom.isShown) {
                val fabLayoutParams = CoordinatorLayout.LayoutParams(dip(40), dip(40))
                fabLayoutParams.gravity = Gravity.BOTTOM or Gravity.END
                fabLayoutParams.setMargins(0, 0, dip(16), layout_box_komentar_main.height + dip(8))
                fab_scroll_to_bottom.layoutParams = fabLayoutParams
                fab_scroll_to_bottom.show()
            }
        } else {
            fab_scroll_to_bottom.hide()
        }
    }

    private fun adjustRvPadding(removePadding: Boolean = false) {
        if (removePadding) {
            recycler_view.setPadding(0, 0, 0, 0)
        } else {
            recycler_view.setPadding(0, 0, 0, layout_box_komentar_main.height)
        }
    }

    override fun onResume() {
        super.onResume()

        jumpingBeans = JumpingBeans.with(tv_status_debat)
            .appendJumpingDots()
            .build()
    }

    override fun onPause() {
        jumpingBeans.stopJumping()
        super.onPause()
    }
}
