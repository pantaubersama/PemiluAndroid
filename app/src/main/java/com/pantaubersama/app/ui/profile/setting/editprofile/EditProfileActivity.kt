package com.pantaubersama.app.ui.profile.setting.editprofile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Surface
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.utils.ImageTools
import com.pantaubersama.app.utils.PantauConstants
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.enable
import com.pantaubersama.app.utils.extensions.loadUrl
import kotlinx.android.synthetic.main.activity_edit_profile.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class EditProfileActivity : BaseActivity<EditProfilePresenter>(), EditProfileView {
    private var permission = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    @Inject
    override lateinit var presenter: EditProfilePresenter

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun fetchIntentExtra() {
        // ok
    }

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, getString(R.string.title_edit_profile), R.color.white, 4f)
        onClickAction()
        swipe_refresh.setOnRefreshListener {
            presenter.refreshUserData()
        }
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
            showImageChooserDialog()
        }
        edit_profile_submit.setOnClickListener {
            presenter.saveEditedUserData(
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
        edit_profile_submit.enable(false)
        edit_profile_container.enable(false)
    }

    override fun finishThisScetion() {
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
                requestPermissions(permission, PantauConstants.ASK_PERMISSIONS_REQUEST_CODE)
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
        val items = arrayOf<CharSequence>("Camera", "Gallery",
            "Batal")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Ambil gambar dari")
        builder.setItems(items) { dialog, item ->
            if (item == 0) {
                openCamera()
            } else if (item == 1) {
                openGallery()
            } else if (item == 2) {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, PantauConstants.Profile.CAMERA_REQUEST_CODE)
    }

    private fun openGallery() {
        val intentGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(Intent.createChooser(intentGallery, "Pilih"), PantauConstants.Profile.STORAGE_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PantauConstants.ASK_PERMISSIONS_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showIntentChooser()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PantauConstants.Profile.CAMERA_REQUEST_CODE) {
                onCaptureImageResult(data)
            } else if (requestCode == PantauConstants.Profile.STORAGE_REQUEST_CODE) {
                onSelectFromGalleryResult(data)
            }
        }
    }

    private fun onCaptureImageResult(data: Intent?) {
        var rotation = 0
        when (windowManager.defaultDisplay.rotation) {
            Surface.ROTATION_0 -> rotation = 90
            Surface.ROTATION_90 -> rotation = 180
            Surface.ROTATION_180 -> rotation = 270
            Surface.ROTATION_270 -> rotation = 0
        }
        val bitmap = data?.extras?.get("data") as Bitmap
        val rotatedBitmap = ImageTools.BitmapTools.rotate(bitmap, rotation)
        updateAvatar(ImageTools.getImageFile(rotatedBitmap))
    }

    private fun updateAvatar(file: File) {
        val type: String
        val extension = MimeTypeMap.getFileExtensionFromUrl(file.absolutePath)
        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)!!
        val reqFile = RequestBody.create(MediaType.parse(type), file)
        val avatar = MultipartBody.Part.createFormData("avatar", file.getName(), reqFile)
        presenter.uploadAvatar(avatar)
    }

    private fun rotateImage(source: Bitmap, angle: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle)
        val bm = Bitmap.createBitmap(source, 0, 0, source.width, source.height,
            matrix, true) as Bitmap
        return bm
    }

    private fun getRealPathFromURI(contentUri: Uri?): String {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = managedQuery(contentUri, proj, null, null, null)
        val column_index = cursor
            ?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        return cursor?.getString(column_index!!)!!
    }

    private fun onSelectFromGalleryResult(data: Intent?) {
        if (data == null) {
            // Display an error
            Toast.makeText(this@EditProfileActivity, getString(R.string.failed_load_image_alert), Toast.LENGTH_SHORT).show()
            return
        }
        val selectedImage = data.data
        val projection = arrayOf(MediaStore.MediaColumns.DATA)
        val cursor = managedQuery(selectedImage, projection, null, null, null)
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        cursor.moveToFirst()
        val path = cursor.getString(column_index)
        val file = File(path)
        updateAvatar(file)
    }

    override fun refreshProfile() {
        presenter.refreshUserData()
    }

    override fun showSuccessUpdateAvatarAlert() {
        ToastUtil.show(this@EditProfileActivity, getString(R.string.success_update_avatar_alert))
    }

    override fun showFailedUpdateAvatarAlert() {
        ToastUtil.show(this@EditProfileActivity, getString(R.string.failed_update_avatar_alert))
    }
}