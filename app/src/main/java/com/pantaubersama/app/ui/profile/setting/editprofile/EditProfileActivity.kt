package com.pantaubersama.app.ui.profile.setting.editprofile

import android.text.method.KeyListener
import android.widget.EditText
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.base.BaseApp
import com.pantaubersama.app.data.interactors.ProfileInteractor
import com.pantaubersama.app.data.model.user.Profile
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
        setEditextDisable(edit_profile_nama)
        onClickAction()
    }

    override fun setLayout(): Int {
        return R.layout.activity_edit_profile
    }

    override fun onResume() {
        super.onResume()
        presenter?.updateUser()
    }

    override fun onSuccessLoadUser(profile: Profile) {
        edit_profile_avatar.loadUrl(profile.avatar.medium?.url, R.drawable.ic_avatar_placeholder)
    }

    override fun showLoading() {
        // Show Loading
    }

    override fun dismissLoading() {
        // Hide Loading
    }

    private fun onClickAction() {
        edit_profile_avatar.setOnClickListener {
            // edit avatar
        }
        edit_profile_change_profile.setOnClickListener {
            // change profile
        }
        edit_profile_submit.setOnClickListener {
            // submit hasil edit
        }
        edit_profile_nama.setOnClickListener {
            setEditextEnable(edit_profile_nama)
        }
        edit_profile_username.setOnClickListener {
            // ok
        }
        edit_profile_lokasi.setOnClickListener {
            // ok
        }
        edit_profile_deskripsi.setOnClickListener {
            // ok
        }
        edit_profile_pendidikan.setOnClickListener {
            // ok
        }
        edit_profile_pekerjaan.setOnClickListener {
            // ok
        }
    }

    fun setEditextDisable(editext: EditText) {
        editext.tag = editext.keyListener
        editext.keyListener = null
    }

    fun setEditextEnable(editext: EditText) {
        editext.setKeyListener(editext.getTag() as KeyListener)
    }
}
