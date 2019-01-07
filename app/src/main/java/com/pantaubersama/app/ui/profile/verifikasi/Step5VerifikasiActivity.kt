package com.pantaubersama.app.ui.profile.verifikasi

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Point
import android.hardware.Camera
import android.os.Build
import android.os.Bundle
import android.view.Surface
import android.view.View
import com.pantaubersama.app.CommonActivity
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.ui.widget.CameraPreview
import com.pantaubersama.app.utils.PantauConstants
import kotlinx.android.synthetic.main.activity_step5_verifikasi.*

class Step5VerifikasiActivity : CommonActivity() {
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

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun fetchIntentExtra() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
            val intent = Intent(this@Step5VerifikasiActivity, Step6VerifikasiActivity::class.java)
            startActivity(intent)
            finish()
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
        if (checkCameraHardware(this@Step5VerifikasiActivity)) {
            mCamera = getCameraInstance()
            mCamera?.setDisplayOrientation(90)
            val params = mCamera?.parameters

            val sizeList = params?.supportedPictureSizes
            val display = windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)
            val height = size.y
            val chosenSize = getPictureSizeIndexForHeight(sizeList!!, height) // height from screen
            params.setPictureSize(sizeList.get(chosenSize).width, sizeList[chosenSize].height)

            mCamera?.parameters = params
            mPreview = mCamera?.let {
                CameraPreview(this, it)
            }
            mPreview?.also {
                camera_preview.addView(it)
            }
            cameraCallback = Camera.PictureCallback { data, camera ->
                var rotation = 0
                when (display.rotation) {
                    Surface.ROTATION_0 -> rotation = -90
                    Surface.ROTATION_90 -> rotation = 0
                    Surface.ROTATION_180 -> rotation = 90
                    Surface.ROTATION_270 -> rotation = 180
                }

                var bitmap = BitmapTools.toBitmap(data)
                bitmap = BitmapTools.rotate(bitmap, rotation)
                image_preview_container.visibility = View.VISIBLE
                image_preview.setImageBitmap(bitmap)
                isPreview = true
            }
            capture_button.setOnClickListener {
                capture_button.isEnabled = false
                mCamera?.takePicture(null, null, cameraCallback)
            }
            close_camera_button.setOnClickListener {
                finish()
            }
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

    object BitmapTools {
        fun toBitmap(data: ByteArray): Bitmap {
            return BitmapFactory.decodeByteArray(data, 0, data.size)
        }

        fun rotate(bitmap: Bitmap, angle: Int): Bitmap {
            val mat = Matrix()
            mat.postRotate(angle.toFloat())
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, mat, true)
        }
    }

    private fun findFrontFacingCamera(): Int {
        var cameraId = -1
        // Search for the front facing camera
        val numberOfCameras = Camera.getNumberOfCameras()
        for (i in 0 until numberOfCameras) {
            val info = Camera.CameraInfo()
            Camera.getCameraInfo(i, info)
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i
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
        if (checkCameraHardware(this@Step5VerifikasiActivity)) {
            setupCamera()
        }
    }

    override fun setLayout(): Int {
        return R.layout.activity_step5_verifikasi
    }

    private fun checkCameraHardware(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
    }

    private fun getCameraInstance(): Camera? {
        return try {
            Camera.open(findFrontFacingCamera())
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
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
}
