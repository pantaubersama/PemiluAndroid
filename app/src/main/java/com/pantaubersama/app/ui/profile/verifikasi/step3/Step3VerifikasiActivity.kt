package com.pantaubersama.app.ui.profile.verifikasi.step3

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Point
import android.hardware.Camera
import android.os.Build
import android.os.Bundle
import android.view.Surface
import android.view.View
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.ui.profile.verifikasi.step4.Step4VerifikasiActivity
import com.pantaubersama.app.ui.widget.CameraPreview
import com.pantaubersama.app.utils.PantauConstants
import kotlinx.android.synthetic.main.activity_step3_verifikasi.*
import javax.inject.Inject
import android.webkit.MimeTypeMap
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.utils.ImageTools
import com.pantaubersama.app.utils.ToastUtil
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

class Step3VerifikasiActivity : BaseActivity<Step3VerifikasiPresenter>(), Step3VerifikasiView {

    @Inject
    override lateinit var presenter: Step3VerifikasiPresenter

    private var ktpSelfie: MultipartBody.Part? = null

    private var permission =
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    private var mCamera: Camera? = null
    private var mPreview: CameraPreview? = null
    private var cameraCallback: Camera.PictureCallback? = null
    private var isPreview = false
    private var isFrontCamera = false

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun fetchIntentExtra() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()

        if (isHaveStorageAndCameraPermission()) {
            setupCamera()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permission, PantauConstants.Camera.ASK_PERMISSIONS_REQUEST_CODE)
            }
        }
        next_button.setOnClickListener {
            presenter.submitSelfieKtp(ktpSelfie)
        }
        retake_button.setOnClickListener {
            image_preview_container.visibility = View.GONE
            restartActivity()
        }
    }

    private fun restartActivity() {
        finish()
        startActivity(intent)
    }

    private fun setupCamera() {
        if (checkCameraHardware(this@Step3VerifikasiActivity)) {
            try {
                mCamera = Camera.open()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            mCamera?.setDisplayOrientation(90)
            val params = mCamera?.parameters

            val sizeList = params?.supportedPictureSizes
            val display = windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)
            val height = size.y
            val chosenSize = getPictureSizeIndexForHeight(sizeList!!, height) // height from screen
            params.setPictureSize(sizeList[chosenSize].width, sizeList[chosenSize].height)

            mCamera?.parameters = params
            mPreview = mCamera?.let {
                CameraPreview(this, it)
            }
            mPreview?.also {
                camera_preview.addView(it)
            }
            cameraCallback = getCameraCallback()
            capture_button.setOnClickListener {
                capture_button.isEnabled = false
                mCamera?.takePicture(null, null, cameraCallback)
            }
            switch_camera.setOnClickListener {
                val camerasNumber = Camera.getNumberOfCameras()
                if (camerasNumber > 1) {
                    releaseCamera()
                    chooseCamera()
                }
            }
            close_camera_button.setOnClickListener {
                finish()
            }
        }
    }

    private fun chooseCamera() {
        if (isFrontCamera) {
            val cameraId = findBackFacingCamera()
            if (cameraId >= 0) {
                mCamera = Camera.open(cameraId)
                mCamera?.setDisplayOrientation(90)
                cameraCallback = getCameraCallback()
                mPreview?.refreshCamera(mCamera)
            }
        } else {
            val cameraId = findFrontFacingCamera()
            if (cameraId >= 0) {
                mCamera = Camera.open(cameraId)
                mCamera?.setDisplayOrientation(90)
                cameraCallback = getCameraCallback()
                mPreview?.refreshCamera(mCamera)
            }
        }
    }

    private fun getCameraCallback(): Camera.PictureCallback? {
        return Camera.PictureCallback { data, camera ->
            var bitmap = ImageTools.BitmapTools.toBitmap(data)
            var rotation = 0
            if (isFrontCamera) {
                when (windowManager.defaultDisplay.rotation) {
                    Surface.ROTATION_0 -> rotation = -90
                    Surface.ROTATION_90 -> rotation = 0
                    Surface.ROTATION_180 -> rotation = 90
                    Surface.ROTATION_270 -> rotation = 180
                }
            } else {
                when (windowManager.defaultDisplay.rotation) {
                    Surface.ROTATION_0 -> rotation = 90
                    Surface.ROTATION_90 -> rotation = 180
                    Surface.ROTATION_180 -> rotation = 270
                    Surface.ROTATION_270 -> rotation = 0
                }
            }
            bitmap = ImageTools.BitmapTools.rotate(bitmap, rotation)
            image_preview_container.visibility = View.VISIBLE
            image_preview.setImageBitmap(bitmap)
            isPreview = true

            val type: String
            val extension = MimeTypeMap.getFileExtensionFromUrl(ImageTools.getImageFile(bitmap).absolutePath)
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)!!

            val reqFile = RequestBody.create(MediaType.parse(type), ImageTools.getImageFile(bitmap))
            ktpSelfie = MultipartBody.Part.createFormData("ktp_selfie", ImageTools.getImageFile(bitmap).name, reqFile)
        }
    }

    private fun getPictureSizeIndexForHeight(sizeList: List<Camera.Size>, height: Int): Int {
        var chosenHeight = -1
        for (i in sizeList.indices) {
            if (sizeList[i].height < height) {
                chosenHeight = i - 1
                if (chosenHeight == -1)
                    chosenHeight = 0
                break
            }
        }
        return chosenHeight
    }

    private fun findFrontFacingCamera(): Int {
        var cameraId = -1
        val numberOfCameras = Camera.getNumberOfCameras()
        for (i in 0 until numberOfCameras) {
            val info = Camera.CameraInfo()
            Camera.getCameraInfo(i, info)
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i
                isFrontCamera = true
                break
            }
        }
        return cameraId

    }

    private fun findBackFacingCamera(): Int {
        var cameraId = -1
        val numberOfCameras = Camera.getNumberOfCameras()
        for (i in 0 until numberOfCameras) {
            val info = Camera.CameraInfo()
            Camera.getCameraInfo(i, info)
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i
                isFrontCamera = false
                break
            }
        }
        return cameraId
    }

    private fun releaseCamera() {
        if (mCamera != null) {
            mCamera?.stopPreview()
            mCamera?.setPreviewCallback(null)
            mCamera?.release()
            mCamera = null
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PantauConstants.Camera.ASK_PERMISSIONS_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            setupCamera()
        }
    }

    override fun onResume() {
        super.onResume()
        if (checkCameraHardware(this@Step3VerifikasiActivity)) {
            setupCamera()
        }
    }

    override fun setLayout(): Int {
        return R.layout.activity_step3_verifikasi
    }

    override fun showLoading() {
        progressDialog?.show()
    }

    override fun dismissLoading() {
        progressDialog?.dismiss()
    }

    private fun checkCameraHardware(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
    }

    override fun onDestroy() {
        releaseCamera()
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (isPreview) {
            restartActivity()
        } else {
            super.onBackPressed()
        }
    }

    fun endActivity() {
        finish()
    }

    override fun onSuccessSubmitSelfieKtp() {
        val intent = Intent(this@Step3VerifikasiActivity, Step4VerifikasiActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun showFailedSubmitSelfieKtpAlert() {
        ToastUtil.show(this@Step3VerifikasiActivity, "Gagal mengunggah gambar")
    }
}