package com.pantaubersama.app.ui.debat

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.debat.Komentar
import com.pantaubersama.app.data.model.debat.Message
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.debat.adapter.KomentarAdapter
import com.pantaubersama.app.ui.debat.adapter.MessageAdapter
import com.pantaubersama.app.utils.hideKeyboard
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.activity_debat.*
import kotlinx.android.synthetic.main.layout_header_detail_debat.*
import kotlinx.android.synthetic.main.layout_komentar_debat.*
import kotlinx.android.synthetic.main.layout_status_debat.*
import kotlinx.android.synthetic.main.layout_toolbar_debat.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import timber.log.Timber
import javax.inject.Inject

class DebatActivity : BaseActivity<DebatPresenter>(), DebatView {
    @Inject
    override lateinit var presenter: DebatPresenter

    override fun statusBarColor(): Int? = R.color.white
    override fun setLayout(): Int = R.layout.activity_debat

    private lateinit var messageAdapter: MessageAdapter
    private lateinit var komentarAdapter: KomentarAdapter

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        btn_back.setOnClickListener { onBackPressed() }
        setupLayoutBehaviour()

        cl_btn_detail_debat.setOnClickListener {
            expandable_detail_debat.toggle(true)
            if (expandable_detail_debat.isExpanded) {
                iv_detail_debat_arrow.animate().rotation(180f).start()
            } else {
                iv_detail_debat_arrow.animate().rotation(0f).start()
            }
        }
        setupDebatList()
        setupKomentarList()

        getList()
    }

    private fun getList() {
        presenter.getMessage()
        presenter.getKomentar()
    }

    private fun setupDebatList() {
        messageAdapter = MessageAdapter()
        recycler_view.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recycler_view.adapter = messageAdapter
        messageAdapter.listener = object : MessageAdapter.AdapterListener {
            override fun onClickClap() {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

    override fun showMessage(messageList: MutableList<Message>) {
        messageList.forEach {
            Timber.d("messageList : $it")
        }

        messageAdapter.setDatas(messageList)
        for (i in 0 until messageAdapter.getDatas()?.size()!!) {
            Timber.d("messageList getDatas $i : ${messageAdapter.getData(i)}")
        }
    }

    override fun showKomentar(komentarList: MutableList<Komentar>) {
        komentarAdapter.setDatas(komentarList)
//        for (i in 0 until komentarList.size) {
//
//        }
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

        val et_comment_main = layout_box_komentar_main.findViewById<EditText>(R.id.et_comment)
        val et_comment_in = layout_komentar_debat.findViewById<EditText>(R.id.et_comment)
        val btn_comment_main = layout_box_komentar_main.findViewById<ImageView>(R.id.iv_btn_comment)
        val btn_comment_in = layout_komentar_debat.findViewById<ImageView>(R.id.iv_btn_comment)

        lateinit var commentInTextWatcher: TextWatcher
        lateinit var commentMainTextWatcher: TextWatcher

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

        sliding_layout.apply {
            setFadeOnClickListener { panelState = SlidingUpPanelLayout.PanelState.COLLAPSED }
        }

        sliding_layout.addPanelSlideListener(object : SlidingUpPanelLayout.PanelSlideListener{
            override fun onPanelSlide(panel: View?, slideOffset: Float) {}

            override fun onPanelStateChanged(panel: View?, previousState: SlidingUpPanelLayout.PanelState?, newState: SlidingUpPanelLayout.PanelState?) {
                if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED && KeyboardVisibilityEvent.isKeyboardVisible(this@DebatActivity)) {
                    hideKeyboard(this@DebatActivity)
                }
            }
        })

        layout_komentar_debat.setOnClickListener(null)

        layout_box_komentar_main.post {
            recycler_view.setPadding(0,0,0, layout_box_komentar_main.height)
        }

        KeyboardVisibilityEvent.setEventListener(this@DebatActivity) { isOpen ->
            isKeyboardShown = isOpen
            if (isOpen) {
                btn_comment_main.setImageResource(R.drawable.ic_debat_open)
            } else {
                btn_comment_main.setImageResource(R.drawable.ic_outline_debat_done)
            }
        }

        btn_comment_main.setOnClickListener {
            if (isKeyboardShown) {

            } else {
                sliding_layout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
            }
        }

        btn_comment_in.setOnClickListener {  }

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
}
