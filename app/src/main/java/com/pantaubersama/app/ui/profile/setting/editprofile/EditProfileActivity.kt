package com.pantaubersama.app.ui.profile.setting.editprofile

import android.text.method.KeyListener
import android.widget.EditText
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : BaseActivity<EditProfilePresenter>(), EditProfileView {

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun fetchIntentExtra() {
        // ok
    }

    override fun initPresenter(): EditProfilePresenter? {
        return EditProfilePresenter()
    }

    override fun setupUI() {
        setupToolbar(true, getString(R.string.title_edit_profile), R.color.white, 4f)
        setEditextDisable(edit_profile_nama)
        onClickAction()
    }

    override fun setLayout(): Int {
        return R.layout.activity_edit_profile
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
