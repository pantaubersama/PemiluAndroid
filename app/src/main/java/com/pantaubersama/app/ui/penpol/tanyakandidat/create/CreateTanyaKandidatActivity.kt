package com.pantaubersama.app.ui.penpol.tanyakandidat.create

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding2.widget.RxTextView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.ItemModel
import com.pantaubersama.app.data.model.tanyakandidat.Pertanyaan
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.penpol.tanyakandidat.detail.DetailTanyaKandidatActivity
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.loadUrl
import com.pantaubersama.app.utils.extensions.visibleIf
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_create_tanya_kandidat.*
import kotlinx.android.synthetic.main.layout_loading_state.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CreateTanyaKandidatActivity : BaseActivity<CreateTanyaKandidatPresenter>(), CreateTanyaKandidatView {

    @Inject
    override lateinit var presenter: CreateTanyaKandidatPresenter

    private var isLoading = false
    private lateinit var adapter: QuestionsAvailableAdapter
    private lateinit var disposables: CompositeDisposable

    override fun statusBarColor(): Int {
        return 0
    }

    override fun fetchIntentExtra() {
//        TODO("not implemented") //To change body of createdAt functions use File | Settings | File Templates.
    }

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, getString(R.string.create_question), R.color.white, 4f)
        presenter.getUserData()
        if (savedInstanceState?.getString("question") != null) {
            question.setText(savedInstanceState.getString("question"))
        }
        question?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
//                TODO("not implemented") //To change body of createdAt functions use File | Settings | File Templates.
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                TODO("not implemented") //To change body of createdAt functions use File | Settings | File Templates.
            }

            override fun onTextChanged(string: CharSequence?, p1: Int, p2: Int, p3: Int) {
                question_length.text = string?.length.toString()
            }
        })
        setupAvailableQuestions()
        disposables = CompositeDisposable()
        RxTextView.textChanges(question)
            .debounce(1000, TimeUnit.MILLISECONDS)
            .map { it.toString() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.isNotEmpty()) {
                    adapter.setDataEnd(false)
                    available_questions_container.visibility = View.VISIBLE
                    presenter.getAvailableQuestions(it)
                } else {
                    available_questions_container.visibility = View.GONE
                }
            }.addTo(disposables)
    }

    private fun setupAvailableQuestions() {
        adapter = QuestionsAvailableAdapter()
        adapter.listener = object : QuestionsAvailableAdapter.Listener {
            override fun onClickQuestion(item: Pertanyaan) {
                val intent = DetailTanyaKandidatActivity.setIntent(this@CreateTanyaKandidatActivity, item.id)
                startActivity(intent)
            }
        }
        available_questions.layoutManager = LinearLayoutManager(this@CreateTanyaKandidatActivity)
        available_questions.adapter = adapter
    }

    override fun bindAvailableQuestions(questions: MutableList<Pertanyaan>) {
        available_questions.visibleIf(true)
        adapter.setDatas(questions as MutableList<ItemModel>)
    }

    override fun showQustionAvailableLoading() {
        available_questions.visibleIf(false)
        empty_alert.visibility = View.GONE
        failed_alert.visibility = View.GONE
        lottie_loading.visibleIf(true)
    }

    override fun hideQustionAvailableLoading() {
        available_questions.visibleIf(false)
        lottie_loading.visibleIf(false)
    }

    override fun showFailedGetAvailableQuestions() {
        available_questions.visibleIf(false)
        failed_alert.visibility = View.VISIBLE
    }

    override fun showEmptyAvailableQuestionAlert() {
        empty_alert.visibility = View.VISIBLE
    }

    override fun setLayout(): Int {
        return R.layout.activity_create_tanya_kandidat
    }

    override fun showLoading() {
        page_loading.visibility = View.VISIBLE
    }

    override fun dismissLoading() {
        page_loading.visibility = View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_done, menu)
        menu?.findItem(R.id.done_action)?.isVisible = !isLoading
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.done_action -> {
                presenter.submitQuestion(question.text.toString())
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun bindProfileData(profile: Profile?) {
        iv_user_avatar.loadUrl(profile?.avatar?.thumbnail?.url, R.drawable.ic_avatar_placeholder)
    }

    override fun showEmptyQuestionAlert() {
        question.error = getString(R.string.empty_question_alert)
    }

    override fun showSuccessCreateTanyaKandidatAlert() {
        ToastUtil.show(this@CreateTanyaKandidatActivity, "Berhasil mengirim pertanyaan")
    }

    override fun finishActivity(question: Pertanyaan?) {
        val intent = Intent()
        intent.putExtra(PantauConstants.TanyaKandidat.TANYA_KANDIDAT_DATA, question)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun showFailedCreateTanyaKandidatAlert() {
        ToastUtil.show(this@CreateTanyaKandidatActivity, "Gagal mengirim pertanyaan")
        isLoading = false
        invalidateOptionsMenu()
    }

    override fun hideActions() {
        isLoading = true
        invalidateOptionsMenu()
    }

    override fun onBackPressed() {
        if (available_questions_container.visibility == View.VISIBLE) {
            available_questions_container.visibility = View.GONE
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        outPersistentState?.putString("question", question.text.toString())
        super.onSaveInstanceState(outState, outPersistentState)
    }
}
