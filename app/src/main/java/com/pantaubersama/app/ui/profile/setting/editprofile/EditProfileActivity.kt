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
        setEditextDisable(edit_profile_username)
        setEditextDisable(edit_profile_lokasi)
        setEditextDisable(edit_profile_deskripsi)
        setEditextDisable(edit_profile_pendidikan)
        setEditextDisable(edit_profile_pekerjaan)
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
            setEditextEnable(edit_profile_username)
        }
        edit_profile_lokasi.setOnClickListener {
            setEditextEnable(edit_profile_lokasi)
        }
        edit_profile_deskripsi.setOnClickListener {
            setEditextEnable(edit_profile_deskripsi)
        }
        edit_profile_pendidikan.setOnClickListener {
            setEditextEnable(edit_profile_pendidikan)
        }
        edit_profile_pekerjaan.setOnClickListener {
            setEditextEnable(edit_profile_pekerjaan)
        }
    }

    // belum optimal
    fun setEditextDisable(editext: EditText) {
        editext.tag = editext.keyListener
        editext.keyListener = null
    }

    fun setEditextEnable(editext: EditText) {
        editext.setKeyListener(editext.getTag() as KeyListener)
    }
}
