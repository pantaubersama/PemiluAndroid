package com.pantaubersama.app.ui.profile.setting.editprofile

import android.app.Activity
import android.text.method.KeyListener
import android.view.View
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.base.BaseApp
import com.pantaubersama.app.data.interactors.ProfileInteractor
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.loadUrl
import kotlinx.android.synthetic.main.activity_edit_profile.*
import javax.inject.Inject

class EditProfileActivity : BaseActivity<EditProfilePresenter>(), EditProfileView {

    @Inject
    lateinit var profileInteractor: ProfileInteractor

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun fetchIntentExtra() {
        // ok
    }

    override fun initInjection() {
        (application as BaseApp).createActivityComponent(this)?.inject(this)
    }

    override fun initPresenter(): EditProfilePresenter? {
        return EditProfilePresenter(profileInteractor)
    }

    override fun setupUI() {
        setupToolbar(true, getString(R.string.title_edit_profile), R.color.white, 4f)
        onClickAction()
        swipe_refresh.setOnRefreshListener {
            presenter?.refreshUserData()
        }
    }

    override fun showFailedGetUserDataAlert() {
        ToastUtil.show(this@EditProfileActivity, "Gagal memuat data profil")
    }

    override fun setLayout(): Int {
        return R.layout.activity_edit_profile
    }

    override fun onResume() {
        super.onResume()
        presenter?.getUserData()
    }

    override fun onSuccessLoadUser(profile: Profile) {
        swipe_refresh.isRefreshing = false
        edit_profile_avatar.loadUrl(profile.avatar.medium?.url, R.drawable.ic_avatar_placeholder)
        edit_profile_nama.setText(profile.name)
        edit_profile_username.setText("@%s".format(profile.username))
        edit_profile_lokasi.setText(profile.location)
        edit_profile_deskripsi.setText(profile.about)
        edit_profile_pendidikan.setText(profile.education)
        edit_profile_pekerjaan.setText(profile.occupation)
    }

    override fun showLoading() {
        progress_bar.visibility = View.VISIBLE
    }

    override fun dismissLoading() {
        progress_bar.visibility = View.GONE
    }

    private fun onClickAction() {
        edit_profile_change_profile.setOnClickListener {
            // change profile
        }
        edit_profile_submit.setOnClickListener {
            presenter?.saveEditedUserData(
                edit_profile_nama.text.toString(),
                edit_profile_username.text.toString().substring(1),
                edit_profile_lokasi.text.toString(),
                edit_profile_deskripsi.text.toString(),
                edit_profile_pendidikan.text.toString(),
                edit_profile_pekerjaan.text.toString()
            )
        }
    }

    override fun showProfileUpdatedAlert() {
        ToastUtil.show(this@EditProfileActivity, "Berhasil memperbarui profil")
    }

    override fun showFailedUpdateProfileAlert() {
        ToastUtil.show(this@EditProfileActivity, "Gagal memperbarui profil")
    }

    override fun disableView() {
        edit_profile_submit.isEnabled = false
        edit_profile_submit.setBackgroundColor(ContextCompat.getColor(this@EditProfileActivity, R.color.gray_dark_1))
        for (i in 0 until edit_profile_container.childCount) {
            val child = edit_profile_container.getChildAt(i)
            child.isEnabled = false
        }
    }

    override fun finishThisScetion() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun enableView() {
        edit_profile_submit.isEnabled = true
        edit_profile_submit.setBackgroundColor(ContextCompat.getColor(this@EditProfileActivity, R.color.colorPrimary))
        for (i in 0 until edit_profile_container.childCount) {
            val child = edit_profile_container.getChildAt(i)
            child.isEnabled = true
        }
    }

    //    fun setEditextDisable(editext: EditText) {
//        editext.tag = editext.keyListener
//        editext.keyListener = null
//    }
//
//    fun setEditextEnable(editext: EditText) {
//        editext.setKeyListener(editext.getTag() as KeyListener)
//    }
}
