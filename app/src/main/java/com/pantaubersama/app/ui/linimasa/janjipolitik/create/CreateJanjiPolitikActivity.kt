package com.pantaubersama.app.ui.linimasa.janjipolitik.create

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.webkit.MimeTypeMap
import com.bumptech.glide.Glide
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.data.model.janjipolitik.JanjiPolitik
import com.pantaubersama.app.data.model.user.Profile
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.widget.DeleteConfimationDialog
import com.pantaubersama.app.ui.widget.ImageChooserTools
import com.pantaubersama.app.utils.PantauConstants.Extra.EXTRA_JANPOL_ITEM
import com.pantaubersama.app.utils.PantauConstants.Permission.GET_IMAGE_PERMISSION
import com.pantaubersama.app.utils.PantauConstants.RequestCode.RC_ASK_PERMISSIONS
import com.pantaubersama.app.utils.PantauConstants.RequestCode.RC_CAMERA
import com.pantaubersama.app.utils.PantauConstants.RequestCode.RC_STORAGE
import com.pantaubersama.app.utils.extensions.loadUrl
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.activity_create_janji_politik.*
import kotlinx.android.synthetic.main.layout_editor_option.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest
import java.io.File
import javax.inject.Inject

class CreateJanjiPolitikActivity : BaseActivity<CreateJanjiPolitikPresenter>(), CreateJanjiPolitikView {

    @Inject
    override lateinit var presenter: CreateJanjiPolitikPresenter

    private var isLoading = false

    private var title = ""
    private var body = ""
    private var imageFile: File? = null

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, getString(R.string.txt_tab_janji_politik), R.color.white, 4f)

        presenter.getProfile()

        et_create_janpol_title.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
//                TODO("not implemented") //To change body of createdAt functions use File | Settings | File Templates.
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                TODO("not implemented") //To change body of createdAt functions use File | Settings | File Templates.
            }

            override fun onTextChanged(query: CharSequence?, p1: Int, p2: Int, p3: Int) {
                tv_current_length.text = query?.length.toString()
            }
        })
        et_create_janpol_content.setOnFocusChangeListener { _, hasFocus -> ll_layout_editor_option.visibleIf(hasFocus && imageFile == null) }
        setupEditor()
    }

    override fun onSuccessGetProfile(profile: Profile) {
        iv_user_avatar.loadUrl(profile.avatar.medium?.url, R.drawable.ic_avatar_placeholder)
        tv_user_name.text = profile.name
    }

    override fun onSuccessCreateJanpol(janjiPolitik: JanjiPolitik) {
        val intent = Intent()
        intent.putExtra(EXTRA_JANPOL_ITEM, janjiPolitik)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onFailedCreateJanpol() {
//        TODO("not implemented") //To change body of createdAt functions use File | Settings | File Templates.
    }

    private fun setupEditor() {
        iv_editor_capital.setOnClickListener {}
        iv_editor_bold.setOnClickListener {}
        iv_editor_italic.setOnClickListener {}
        iv_editor_numbering.setOnClickListener {}
        iv_editor_bullet.setOnClickListener {}
        iv_editor_line.setOnClickListener {}
        iv_editor_image.setOnClickListener { showIntentChooser() }
    }

    override fun setLayout(): Int {
        return R.layout.activity_create_janji_politik
    }

    override fun showLoading() {
        isLoading = true
        page_loading.visibleIf(true)
        page_content.visibleIf(false)
        invalidateOptionsMenu()
        et_create_janpol_content.clearFocus()
    }

    override fun dismissLoading() {
        isLoading = false
        page_loading.visibleIf(false)
        page_content.visibleIf(true)
        invalidateOptionsMenu()
    }

    @AfterPermissionGranted(RC_ASK_PERMISSIONS)
    private fun showIntentChooser() {
        if (EasyPermissions.hasPermissions(this, *GET_IMAGE_PERMISSION)) {
            ImageChooserTools.showDialog(this@CreateJanjiPolitikActivity)
        } else {
            EasyPermissions.requestPermissions(
                PermissionRequest.Builder(this, RC_ASK_PERMISSIONS, *GET_IMAGE_PERMISSION)
                    .setRationale(getString(R.string.izinkan_akses_kamera_dan_galeri))
                    .setPositiveButtonText(getString(R.string.ok))
                    .setNegativeButtonText(getString(R.string.batal))
                    .build()
            )
        }
    }

    private fun createPost() {
        if (isValidInput()) {
            val title = RequestBody.create(MediaType.parse("text/plain"), this.title)
            val body = RequestBody.create(MediaType.parse("text/plain"), this.body)

            if (imageFile != null) {
                val type: String
                val extension = MimeTypeMap.getFileExtensionFromUrl(imageFile?.absolutePath)
                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)!!
                val reqFile = RequestBody.create(MediaType.parse(type), imageFile!!)
                val image = MultipartBody.Part.createFormData("image", imageFile?.name, reqFile)
                presenter.createJanpol(title, body, image)
            } else {
                presenter.createJanpol(title, body, null)
            }
        }
    }

    private fun isValidInput(): Boolean {
        title = et_create_janpol_title.text.toString().trim()
        body = et_create_janpol_content.text.toString().trim()

        var isValid = true
        if (title.isEmpty()) {
            isValid = false
            et_create_janpol_title.error = getString(R.string.wajib_diisi)
        } else {
            et_create_janpol_title.error = null
        }
        if (body.isEmpty()) {
            isValid = false
            et_create_janpol_content.error = getString(R.string.wajib_diisi)
        } else {
            et_create_janpol_content.error = null
        }

        return isValid
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_done, menu)
        menu?.findItem(R.id.done_action)?.isVisible = !isLoading
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.done_action -> createPost()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RC_CAMERA -> onCaptureImageResult(data)
                RC_STORAGE -> onSelectFromGalleryResult(data)
            }
        }
    }

    private fun onCaptureImageResult(data: Intent?) {
        if (data != null) {
            setImage(ImageChooserTools.proccedImageFromCamera(data))
        } else {
            showError(Throwable(getString(R.string.failed_load_image_alert)))
        }
    }

    private fun onSelectFromGalleryResult(data: Intent?) {
        if (data != null) {
            setImage(ImageChooserTools.proccedImageFromStorage(data, this@CreateJanjiPolitikActivity))
        } else {
            showError(Throwable(getString(R.string.failed_load_image_alert)))
        }
    }

    private fun setImage(file: File?) {
        this.imageFile = file
        iv_create_janpol_image.visibleIf(file != null)
        ll_layout_editor_option.visibleIf(file == null)
        if (file != null) {
            Glide.with(this).load(file).into(iv_create_janpol_image)
            iv_create_janpol_image.setOnClickListener {
                val dialog = DeleteConfimationDialog(this, "Hapus gambar", 0, "")
                dialog.show()
                dialog.listener = object : DeleteConfimationDialog.DialogListener {
                    override fun onClickDeleteItem(id: String, position: Int) {
                        setImage(null)
                    }
                }
            }
        }
    }
}
