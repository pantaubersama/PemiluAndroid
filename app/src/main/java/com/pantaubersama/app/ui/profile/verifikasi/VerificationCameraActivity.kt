package com.pantaubersama.app.ui.profile.verifikasi

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.pantaubersama.app.R
import com.pantaubersama.app.ui.widget.ImageChooserTools
import com.pantaubersama.app.utils.PantauConstants.RequestCode
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.checkPermission
import com.pantaubersama.app.utils.extensions.openAppSettings
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.activity_verification_camera.*
import okio.Okio
import java.io.File

class VerificationCameraActivity : AppCompatActivity() {

    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification_camera)

        val showSelfieFrame = intent.getBooleanExtra(EXTRA_SHOW_SELFIE_FRAME, false)
        frame_1.visibleIf(showSelfieFrame)
        frame_2.visibleIf(showSelfieFrame)
        frame_3.visibleIf(!showSelfieFrame)

        setupListeners()
    }

    private fun setupListeners() {
        switch_camera.setOnClickListener {
            camera.toggleFacing()
        }
        close_camera_button.setOnClickListener {
            finish()
        }
        capture_button.setOnClickListener {
            camera.captureImage { _, bytes ->
                try {
                    showPreview(storeTemporaryImage(bytes))
                } catch (error: Exception) {
                    Toast.makeText(this, "Terjadi error", Toast.LENGTH_SHORT).show()
                }
            }
        }
        choose_image.setOnClickListener {
            checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, RequestCode.RC_STORAGE,
                ::pickFromGallery)
        }
        retake_button.setOnClickListener {
            image_preview.setImageDrawable(null)
            image_preview_container.visibleIf(false)
            camera_overlay.visibleIf(true)
        }
        next_button.setOnClickListener {
            setResult(Activity.RESULT_OK, Intent().setData(imageUri))
            finish()
        }
    }

    private fun storeTemporaryImage(bytes: ByteArray): Uri {
        val file = File(cacheDir.path, "camera.jpg")

        with(Okio.buffer(Okio.sink(file))) {
            write(bytes)
            close()
        }
        return Uri.fromFile(file)
    }

    private fun showPreview(uri: Uri) {
        imageUri = uri
        image_preview.setImageURI(imageUri)
        image_preview_container.visibleIf(true)
        camera_overlay.visibleIf(false)
    }

    private fun pickFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(Intent.createChooser(intent, "Pilih"), RequestCode.RC_STORAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RequestCode.RC_STORAGE) {
                if (data != null) {
                    val file = ImageChooserTools.proccedImageFromStorage(data, this)
                    showPreview(Uri.fromFile(file))
                } else {
                    ToastUtil.show(this, getString(R.string.failed_load_image_alert))
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        camera.onStart()
    }

    override fun onResume() {
        super.onResume()
        camera.onResume()
    }

    override fun onPause() {
        camera.onPause()
        super.onPause()
    }

    override fun onStop() {
        camera.onStop()
        super.onStop()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        camera.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == RequestCode.RC_STORAGE) {
            if (grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
                pickFromGallery()
            } else if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // user selected never show again on permission popup
                ToastUtil.show(this, "Anda harus mengijinkan aplikasi mengakses storage")
                openAppSettings()
            }
        }
    }

    companion object {
        private const val EXTRA_SHOW_SELFIE_FRAME = "EXTRA_SHOW_SELFIE_FRAME"

        fun start(activity: Activity, showSelfieFrame: Boolean, requestCode: Int) {
            activity.startActivityForResult(Intent(activity, VerificationCameraActivity::class.java)
                .putExtra(EXTRA_SHOW_SELFIE_FRAME, showSelfieFrame),
                requestCode)
        }
    }
}
