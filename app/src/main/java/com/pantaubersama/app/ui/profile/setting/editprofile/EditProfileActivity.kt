package com.pantaubersama.app.ui.profile.setting.editprofile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.webkit.MimeTypeMap
import com.jakewharton.rxbinding2.widget.RxTextView
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.home.HomeActivity
import com.pantaubersama.app.ui.profile.connect.ConnectActivity
import com.pantaubersama.app.ui.widget.ImageChooserTools
import com.pantaubersama.app.utils.ImageUtil
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.PantauConstants.Permission.GET_IMAGE_PERMISSION
import com.pantaubersama.app.utils.PantauConstants.RequestCode.RC_ASK_PERMISSIONS
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.enable
import com.pantaubersama.app.utils.extensions.loadUrl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_edit_profile.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class EditProfileActivity : BaseActivity<EditProfilePresenter>(), EditProfileView {
    @Inject
    override lateinit var presenter: EditProfilePresenter
    private var isProfileCompletion = false
    private var isUsernameComplete = false

    private lateinit var imageFile: File

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun fetchIntentExtra() {
        isProfileCompletion = intent.getBooleanExtra(PantauConstants.PROFILE_COMPLETION, isProfileCompletion)
    }

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        val pageTitle: String
        if (isProfileCompletion) {
            pageTitle = getString(R.string.title_buat_profilmu)
            submit_button.text = getString(R.string.next_action)
        } else {
            pageTitle = getString(R.string.title_edit_profile)
        }
        setupToolbar(true, pageTitle, R.color.white, 4f)
        onClickAction()
        swipe_refresh.setOnRefreshListener {
            presenter.refreshUserData()
        }
        setupUsername()
    }

    private fun setupUsername() {
        RxTextView.textChanges(edit_profile_username)
            .filter { it.isNotEmpty() }
            .debounce(1000, TimeUnit.MILLISECONDS)
            .map { it.toString() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                presenter.usernameCheck(it)
            }
            .subscribe()
    }

    override fun showFailedGetUserDataAlert() {
        ToastUtil.show(this@EditProfileActivity, getString(R.string.failed_load_profile_alert))
    }

    override fun setLayout(): Int {
        return R.layout.activity_edit_profile
    }

    override fun onResume() {
        super.onResume()
        presenter.getUserData()
    }

    override fun onSuccessLoadUser(profile: Profile) {
        swipe_refresh.isRefreshing = false
        edit_profile_avatar.loadUrl(profile.avatar?.medium?.url, R.drawable.ic_avatar_placeholder)
        edit_profile_nama.setText(profile.fullName)
        profile.username?.let {
            edit_profile_username.setText(it)
        }
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
            showImageChooserDialog()
        }
        edit_profile_submit.setOnClickListener {
            if (isProfileCompletion) {
                if (isUsernameComplete) {
                    saveData()
                } else {
                    setUsernameAlert("Mohon lengkapi username")
                }
            } else {
                saveData()
            }
        }
    }

    private fun saveData() {
        presenter.saveEditedUserData(
            edit_profile_nama.text.toString(),
            edit_profile_username.text.toString(),
            edit_profile_lokasi.text.toString(),
            edit_profile_deskripsi.text.toString(),
            edit_profile_pendidikan.text.toString(),
            edit_profile_pekerjaan.text.toString()
        )
    }

    override fun showProfileUpdatedAlert() {
        ToastUtil.show(this@EditProfileActivity, "Berhasil memperbarui profil")
    }

    override fun showFailedUpdateProfileAlert() {
        ToastUtil.show(this@EditProfileActivity, "Gagal memperbarui profil")
    }

    override fun disableView() {
        edit_profile_submit.enable(false)
        edit_profile_container.enable(false)
    }

    override fun onSuccessUpdateProfile() {
        if (isProfileCompletion) {
            val intent = Intent(this@EditProfileActivity, ConnectActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } else {
            finishThisScetion()
        }
    }

    fun finishThisScetion() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun enableView() {
        edit_profile_submit.enable(true)
        edit_profile_container.enable(true)
    }

    private fun showImageChooserDialog() {
        if (isHaveStorageAndCameraPermission()) {
            showIntentChooser()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(GET_IMAGE_PERMISSION, RC_ASK_PERMISSIONS)
            }
        }
    }

    private fun isHaveStorageAndCameraPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val storagePermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            val cameraPermission = checkSelfPermission(Manifest.permission.CAMERA)
            val writeExternalPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            !(storagePermission != PackageManager.PERMISSION_GRANTED || cameraPermission != PackageManager.PERMISSION_GRANTED || writeExternalPermission != PackageManager.PERMISSION_GRANTED)
        } else {
            true
        }
    }

    private fun showIntentChooser() {
        ImageChooserTools.showDialog(this@EditProfileActivity, object : ImageChooserTools.ImageChooserListener {
            override fun onClickCamera(file: File) {
                imageFile = file
            }
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RC_ASK_PERMISSIONS && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showIntentChooser()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && resultCode != Activity.RESULT_CANCELED) {
            if (requestCode == PantauConstants.RequestCode.RC_CAMERA) {
                showProgressDialog("Memperbarui avatar")
                try {
                    ImageUtil.compressImage(this, imageFile, 2, object : ImageUtil.CompressorListener {
                        override fun onSuccess(file: File) {
                            updateAvatar(file)
                        }

                        override fun onFailed(throwable: Throwable) {
                            showError(throwable)
                            dismissProgressDialog()
                        }
                    })
                } catch (e: Exception) {
                    e.printStackTrace()
                    dismissProgressDialog()
                    ToastUtil.show(this@EditProfileActivity, getString(R.string.failed_load_image_alert))
                }
            } else if (requestCode == PantauConstants.RequestCode.RC_STORAGE) {
                if (data != null) {
                    updateAvatar(ImageChooserTools.proccedImageFromStorage(data, this@EditProfileActivity))
                } else {
                    ToastUtil.show(this@EditProfileActivity, getString(R.string.failed_load_image_alert))
                }
            }
        }
    }

    private fun updateAvatar(file: File? = null) {
        if (file != null) {
            val type: String
            val extension = MimeTypeMap.getFileExtensionFromUrl(file.absolutePath)
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)!!
            val reqFile = RequestBody.create(MediaType.parse(type), file)
            val avatar = MultipartBody.Part.createFormData("avatar", file.name, reqFile)
            presenter.uploadAvatar(avatar)
        } else {
            dismissProgressDialog()
            showError(Throwable("Terjadi kesalahan pada gambar"))
        }
    }

    override fun showUploadPictureLoading() {
        showProgressDialog("Mengunggah avatar")
    }

    override fun dismissUploadPictureLoading() {
        dismissProgressDialog()
    }

    override fun refreshProfile() {
        presenter.refreshUserData()
    }

    override fun showSuccessUpdateAvatarAlert() {
        dismissProgressDialog()
        ToastUtil.show(this@EditProfileActivity, getString(R.string.success_update_avatar_alert))
    }

    override fun showFailedUpdateAvatarAlert() {
        dismissProgressDialog()
        ToastUtil.show(this@EditProfileActivity, getString(R.string.failed_update_avatar_alert))
    }

    override fun onUsernameAvailable() {
        username_check.visibility = View.VISIBLE
        edit_profile_username.error = null
        isUsernameComplete = true
    }

    override fun onUsernameUnAvailable() {
        setUsernameAlert("Username sudah digunakan")
    }

    private fun setUsernameAlert(alert: String) {
        username_check.visibility = View.GONE
        edit_profile_username.error = alert
    }

    override fun onBackPressed() {
        if (isProfileCompletion) {
            val intent = Intent(this@EditProfileActivity, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return true
    }
}