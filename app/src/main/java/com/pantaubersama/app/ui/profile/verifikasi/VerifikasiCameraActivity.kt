package com.pantaubersama.app.ui.profile.verifikasi

import android.Manifest.permission
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.pantaubersama.app.R
import com.pantaubersama.app.ui.widget.ImageChooserTools
import com.pantaubersama.app.utils.GlideApp
import com.pantaubersama.app.utils.PantauConstants.RequestCode
import com.pantaubersama.app.utils.ToastUtil
import com.pantaubersama.app.utils.extensions.checkPermission
import com.pantaubersama.app.utils.extensions.openAppSettings
import com.pantaubersama.app.utils.extensions.visibleIf
import kotlinx.android.synthetic.main.activity_verification_camera.*
import okio.Okio
import java.io.File

class VerifikasiCameraActivity : AppCompatActivity() {

    private var imageUri: Uri? = null

    private var alertDialog: AlertDialog? = null

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
            checkPermission(permission.READ_EXTERNAL_STORAGE, RequestCode.RC_STORAGE, ::pickFromGallery)
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
        image_preview_container.visibleIf(true)
        camera_overlay.visibleIf(false)
        GlideApp.with(this)
            .load(uri)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(image_preview)
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
        checkPermission(permission.CAMERA, RequestCode.RC_CAMERA, camera::onStart)
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
        alertDialog?.dismiss()
        super.onStop()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val isCameraRequest = requestCode == RequestCode.RC_CAMERA
        val alertMessage = getString(
            if (isCameraRequest) R.string.izinkan_akses_kamera
            else R.string.izinkan_akses_penyimpanan
        )

        handlePermissionsResult(permissions.firstOrNull(), grantResults.firstOrNull(), alertMessage) {
            if (isCameraRequest) camera.onStart() else pickFromGallery()
        }
    }

    private inline fun handlePermissionsResult(permission: String?, grantResult: Int?,
                                               alertMessage: String, onGranted: () -> Unit) {
        if (permission == null || grantResult == null) return

        if (grantResult == PackageManager.PERMISSION_GRANTED) {
            onGranted()
        } else if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            // user selected never show again on permission popup,
            // so user have to grant permission manually through app setting
            alertDialog = AlertDialog.Builder(this)
                .setTitle(R.string.permintaan_akses)
                .setMessage(alertMessage)
                .setPositiveButton(R.string.buka_pengaturan) { _, _ -> openAppSettings() }
                .setNegativeButton(R.string.batal, null)
                .show()
        }
    }

    companion object {
        private const val EXTRA_SHOW_SELFIE_FRAME = "EXTRA_SHOW_SELFIE_FRAME"

        fun start(activity: Activity, showSelfieFrame: Boolean, requestCode: Int) {
            activity.startActivityForResult(Intent(activity, VerifikasiCameraActivity::class.java)
                .putExtra(EXTRA_SHOW_SELFIE_FRAME, showSelfieFrame),
                requestCode)
        }
    }
}
