package com.pantaubersama.app.ui.profile.verifikasi

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Camera
import android.os.Build
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import com.pantaubersama.app.R
import com.pantaubersama.app.base.BaseActivity
import com.pantaubersama.app.base.BasePresenter
import com.pantaubersama.app.utils.PantauConstants
import kotlinx.android.synthetic.main.activity_step3_verifikasi.*
import timber.log.Timber
import java.io.IOException

class Step3VerifikasiActivity : BaseActivity<BasePresenter<*>>() {
    private var permission =
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    private var mCamera: Camera? = null
    private var mPreview: CameraPreview? = null
    private val mPicture: Camera.PictureCallback? = null

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun fetchIntentExtra() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initPresenter(): BasePresenter<*>? {
        return null
    }

    override fun setupUI() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()

        if (isHaveStorageAndCameraPermission()) {
            if (checkCameraHardware(this@Step3VerifikasiActivity)){
                mCamera = Camera.open()
                mCamera?.setDisplayOrientation(90)
                mPreview = CameraPreview(this@Step3VerifikasiActivity, mCamera)
                camera_preview.addView(mPreview)
                capture_button.setOnClickListener {
                    mCamera?.takePicture(null, null, mPicture)
                }
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permission, PantauConstants.Camera.ASK_PERMISSIONS_REQUEST_CODE)
            }
        }
    }

    private fun isHaveStorageAndCameraPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val storagePermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            val cameraPermission = checkSelfPermission(Manifest.permission.CAMERA)
            val writeExternalPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            return !(storagePermission != PackageManager.PERMISSION_GRANTED || cameraPermission != PackageManager.PERMISSION_GRANTED || writeExternalPermission != PackageManager.PERMISSION_GRANTED)
        } else {
            return true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PantauConstants.Camera.ASK_PERMISSIONS_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (checkCameraHardware(this@Step3VerifikasiActivity)){
                mCamera = Camera.open()
                mCamera?.setDisplayOrientation(90)
                mPreview = CameraPreview(this@Step3VerifikasiActivity, mCamera)
                camera_preview.addView(mPreview)
                capture_button.setOnClickListener {
                    mCamera?.takePicture(null, null, mPicture)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (checkCameraHardware(this@Step3VerifikasiActivity)){
            mCamera = Camera.open()
            mCamera?.setDisplayOrientation(90)
            mPreview = CameraPreview(this@Step3VerifikasiActivity, mCamera)
            camera_preview.addView(mPreview)
            capture_button.setOnClickListener {
                mCamera?.takePicture(null, null, mPicture)
            }
        }
    }

    override fun setLayout(): Int {
        return R.layout.activity_step3_verifikasi
    }

    override fun showLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dismissLoading() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun checkCameraHardware(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
    }

    fun getCameraInstance(): Camera? {
        return try {
            Camera.open() // attempt to get a Camera instance
        } catch (e: Exception) {
            // Camera is not available (in use or does not exist)
            e.printStackTrace()
            null // returns null if camera is unavailable
        }
    }

    inner class CameraPreview(context: Context, private var mCamera: Camera?) : SurfaceView(context), SurfaceHolder.Callback {
        private val mHolder: SurfaceHolder

        init {
            mHolder = holder
            mHolder.addCallback(this)
            // deprecated setting, but required on Android versions prior to 3.0
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        }

        override fun surfaceCreated(holder: SurfaceHolder) {
            try {
                // create the surface and start camera preview
                if (mCamera == null) {
                    mCamera!!.setPreviewDisplay(holder)
                    mCamera!!.startPreview()
                }
            } catch (e: IOException) {
                Timber.e(View.VIEW_LOG_TAG+"Error setting camera preview: " + e.message)
            }

        }

        fun refreshCamera(camera: Camera?) {
            if (mHolder.surface == null) {
                // preview surface does not exist
                return
            }
            // stop preview before making changes
            try {
                mCamera!!.stopPreview()
            } catch (e: Exception) {
                // ignore: tried to stop a non-existent preview
            }

            // set preview size and make any resize, rotate or
            // reformatting changes here
            // start preview with new settings
            setCamera(camera)
            try {
                mCamera!!.setPreviewDisplay(mHolder)
                mCamera!!.startPreview()
            } catch (e: Exception) {
                Timber.e(View.VIEW_LOG_TAG+"Error starting camera preview: " + e.message)
            }

        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, w: Int, h: Int) {
            // If your preview can change or rotate, take care of those events here.
            // Make sure to stop the preview before resizing or reformatting it.
            refreshCamera(mCamera)
        }

        fun setCamera(camera: Camera?) {
            //method to set a camera instance
            mCamera = camera
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            // TODO Auto-generated method stub
            // mCamera.release();
        }
    }
}
