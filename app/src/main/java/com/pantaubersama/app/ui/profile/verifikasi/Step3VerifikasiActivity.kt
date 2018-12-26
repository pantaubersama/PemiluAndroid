package com.pantaubersama.app.ui.profile.verifikasi

import android.Manifest
import android.content.ContentUris
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.hardware.Camera
import android.media.ExifInterface
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
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import java.io.FileNotFoundException
import java.io.FileOutputStream
import android.provider.MediaStore
import android.view.Surface
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class Step3VerifikasiActivity : BaseActivity<BasePresenter<*>>() {
    private var permission =
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    private var mCamera: Camera? = null
    private var mPreview: CameraPreview? = null
    private var cameraCallback: Camera.PictureCallback? = null

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
            setupCamera()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permission, PantauConstants.Camera.ASK_PERMISSIONS_REQUEST_CODE)
            }
        }
    }

    private fun setupCamera() {
        if (checkCameraHardware(this@Step3VerifikasiActivity)){
            mCamera = getCameraInstance()
            mCamera?.setDisplayOrientation(90)
            mPreview = mCamera?.let {
                CameraPreview(this, it)
            }
            mPreview?.also {
                camera_preview.addView(it)
            }
            cameraCallback = Camera.PictureCallback { data, camera ->
                val display = windowManager.defaultDisplay
                var rotation = 0
                when (display.getRotation()) {
                    Surface.ROTATION_0 -> rotation = -90
                    Surface.ROTATION_90 -> rotation = 0
                    Surface.ROTATION_180 -> rotation = 90
                    Surface.ROTATION_270 -> rotation = 180
                }

                var bitmap = BitmapTools.toBitmap(data)
                bitmap = BitmapTools.rotate(bitmap, rotation)
                image_preview_container.visibility = View.VISIBLE
                image_preview.setImageBitmap(bitmap)
            }
            capture_button.setOnClickListener {
                mCamera?.takePicture(null, null, cameraCallback)
            }
            close_camera_button.setOnClickListener {
                finish()
            }
        }
    }

    object BitmapTools {
        fun toBitmap(data: ByteArray): Bitmap {
            return BitmapFactory.decodeByteArray(data, 0, data.size)
        }

        fun rotate(`in`: Bitmap, angle: Int): Bitmap {
            val mat = Matrix()
            mat.postRotate(angle.toFloat())
            return Bitmap.createBitmap(`in`, 0, 0, `in`.width, `in`.height, mat, true)
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

//    private fun getOutputMediaFileUri(type: Int): Uri {
//        return Uri.fromFile(getOutputMediaFile(type))
//    }
//
//    private fun getOutputMediaFile(type: Int): File? {
//        val mediaStorageDir = File(
//            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
//            "PantauBersama"
//        )
//
//        mediaStorageDir.apply {
//            if (!exists()) {
//                if (!mkdirs()) {
//                    Timber.e("PantauBersama", "failed to create directory")
//                    return null
//                }
//            }
//        }
//
//        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//        return when (type) {
//            MEDIA_TYPE_IMAGE -> {
//                File("${mediaStorageDir.path}${File.separator}IMG_$timeStamp.jpg")
//            }
//            else -> null
//        }
//    }

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
        if (checkCameraHardware(this@Step3VerifikasiActivity)){
            setupCamera()
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

    private fun getCameraInstance(): Camera? {
        return try {
            Camera.open(findFrontFacingCamera())
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    inner class CameraPreview(
        context: Context,
        private val mCamera: Camera
    ) : SurfaceView(context), SurfaceHolder.Callback {

        private val mHolder: SurfaceHolder = holder.apply {
            addCallback(this@CameraPreview)
            setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        }

        override fun surfaceCreated(holder: SurfaceHolder) {
            mCamera.apply {
                try {
                    setPreviewDisplay(holder)
                    startPreview()
                } catch (e: IOException) {
                    Timber.e("Error setting camera preview: ${e.message}")
                }
            }
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            // empty. Take care of releasing the Camera preview in your activity.
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, w: Int, h: Int) {
            if (mHolder.surface == null) {
                return
            }
            try {
                mCamera.stopPreview()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            mCamera.apply {
                try {
                    setPreviewDisplay(mHolder)
                    startPreview()
                } catch (e: Exception) {
                    Timber.e("Error starting camera preview: ${e.message}")
                }
            }
        }
    }

    fun endActivity() {
        finish()
    }
}
