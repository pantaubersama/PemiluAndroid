package com.pantaubersama.app.ui.linimasa.janjipolitik.create

import android.Manifest
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.ui.widget.ImageChooserDialog
import com.pantaubersama.app.utils.PantauConstants.Permission.GET_IMAGE_PERMISSION
import com.pantaubersama.app.utils.PantauConstants.RequestCode.RC_ASK_PERMISSIONS
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.activity_create_janji_politik.*
import kotlinx.android.synthetic.main.layout_editor_option.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest
import javax.inject.Inject

class CreateJanjiPolitikActivity : BaseActivity<CreateJanjiPolitikPresenter>(), CreateJanjiPolitikView {

    @Inject
    override lateinit var presenter: CreateJanjiPolitikPresenter

    private var isLoading = false

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        setupToolbar(true, getString(R.string.txt_tab_janji_politik), R.color.white, 4f)
        et_create_janpol_content.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(query: CharSequence?, p1: Int, p2: Int, p3: Int) {
                tv_current_length.text = query?.length.toString()
            }
        })
        et_create_janpol_content.setOnFocusChangeListener { _, hasFocus -> ll_layout_editor_option.visibleIf(hasFocus) }
        setupEditor()
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
            ImageChooserDialog(this).show()
        } else {
            EasyPermissions.requestPermissions(
                PermissionRequest.Builder(this, RC_ASK_PERMISSIONS, *GET_IMAGE_PERMISSION)
                    .setRationale("Mohon izinkan aplikasi mengakses perangkat anda")
                    .setPositiveButtonText("OK")
                    .setNegativeButtonText("Cancel")
                    .build()
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_done, menu)
        menu?.findItem(R.id.done_action)?.isVisible = !isLoading
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.done_action -> {
                et_create_janpol_content.clearFocus()
//                presenter.
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}
