package com.pantaubersama.app.ui.profile.verifikasi.step3

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Point
import android.hardware.Camera
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
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
import com.pantaubersama.app.utils.ImageUtil
import com.pantaubersama.app.ui.widget.ImageChooserTools
import com.pantaubersama.app.utils.ToastUtil
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import android.graphics.BitmapFactory
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraManager
import android.media.Image
import android.view.TextureView
import com.pantaubersama.app.utils.Camera2Tools
import java.io.File

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
    private var mTextureView: TextureView? = null
    private lateinit var manager: CameraManager
    private var imageFile: File? = null
    val CAMERA_FRONT = "1"
    val CAMERA_BACK = "0"
    var mCameraId = CAMERA_BACK
    private var isOpenGallery = false

    override fun statusBarColor(): Int? {
        return 0
    }

    override fun fetchIntentExtra() {
//        TODO("not implemented") //To change body of createdAt functions use File | Settings | File Templates.
    }

    override fun initInjection(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    private val mSurfaceTextureListener = object : TextureView.SurfaceTextureListener {

        override fun onSurfaceTextureAvailable(texture: SurfaceTexture, width: Int, height: Int) {
            Camera2Tools.openCamera(manager, windowManager, mTextureView, mCameraId)
        }

        override fun onSurfaceTextureSizeChanged(texture: SurfaceTexture, width: Int, height: Int) {
            // config
        }

        override fun onSurfaceTextureDestroyed(texture: SurfaceTexture): Boolean {
            return true
        }

        override fun onSurfaceTextureUpdated(texture: SurfaceTexture) {}
    }

    override fun setupUI(savedInstanceState: Bundle?) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()

        if (isHaveStorageAndCameraPermission()) {
            manager = this.getSystemService(Context.CAMERA_SERVICE) as CameraManager
            mTextureView = findViewById(R.id.texture_view)

            if (mTextureView!!.isAvailable()) {
                Camera2Tools.openCamera(manager, windowManager, mTextureView, mCameraId)
            } else {
                mTextureView!!.setSurfaceTextureListener(mSurfaceTextureListener)
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permission, PantauConstants.Camera.ASK_PERMISSIONS_REQUEST_CODE)
            }
        }
        capture_button.setOnClickListener {
            Camera2Tools.takePicture(object : Camera2Tools.Companion.ImageHandler {
                override fun handleImage(image: Image): Runnable {
                    return Runnable {
                        val bitmap = mTextureView!!.getBitmap(mTextureView!!.width, mTextureView!!.height)
                        imageFile = ImageUtil.getImageFile(this@Step3VerifikasiActivity, bitmap)
                        ktpSelfie = createFromFile(imageFile!!)
                        runOnUiThread(Runnable {
                            kotlin.run {
                                viewPreviewFromCamera()
                                image_preview.setImageBitmap(bitmap)
                            }
                        })
                    }
                }
            })
        }
        switch_camera.setOnClickListener {
            val camerasNumber = Camera.getNumberOfCameras()
            if (camerasNumber > 1) {
                chooseCamera()
            }
        }
        close_camera_button.setOnClickListener {
            finish()
        }
        next_button.setOnClickListener {
            presenter.submitSelfieKtp(ktpSelfie)
        }
        retake_button.setOnClickListener {
            Camera2Tools.unlockFocus()
            openCamera()
        }
        choose_image.setOnClickListener {
            if (isHaveStorageAndCameraPermission()) {
                isOpenGallery = true
                Camera2Tools.closeCamera()
                Camera2Tools.stopBackgroundThread()
                val intentGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(Intent.createChooser(intentGallery, "Pilih"), PantauConstants.RequestCode.RC_STORAGE)
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(permission, PantauConstants.Camera.ASK_PERMISSIONS_REQUEST_CODE)
                }
            }
        }
    }

    private fun restartActivity() {
        finish()
        startActivity(intent)
    }

    // camera 1
    private fun setupCamera() {
        if (checkCameraHardware(this@Step3VerifikasiActivity)) {
            try {
                mCamera = Camera.open(findFrontFacingCamera())
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
        }
    }

    private fun chooseCamera() {
        if (isFrontCamera) {
            val cameraId = findBackFacingCamera()
            if (cameraId >= 0) {
                mCameraId = cameraId.toString()
                Camera2Tools.closeCamera()
                Camera2Tools.stopBackgroundThread()
                openCamera()
            }
        } else {
            val cameraId = findFrontFacingCamera()
            if (cameraId >= 0) {
                mCameraId = cameraId.toString()
                Camera2Tools.closeCamera()
                Camera2Tools.stopBackgroundThread()
                openCamera()
            }
        }
    }

    private fun getCameraCallback(): Camera.PictureCallback? {
        return Camera.PictureCallback { data, camera ->
            var bitmap = ImageUtil.BitmapTools.toBitmap(data)
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
            bitmap = ImageUtil.BitmapTools.rotate(bitmap, rotation)
            image_preview_container.visibility = View.VISIBLE
            image_preview.setImageBitmap(bitmap)
            isPreview = true

            val file = ImageUtil.getImageFile(this, bitmap)
            ktpSelfie = createFromFile(file)
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
//            setupCamera()
            openCamera()
        }
    }

    override fun onResume() {
        super.onResume()
        if (!isOpenGallery)
            openCamera()
        isOpenGallery = false
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
        Camera2Tools.closeCamera()
        Camera2Tools.stopBackgroundThread()
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (isPreview) {
            Camera2Tools.unlockFocus()
            openCamera()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PantauConstants.RequestCode.RC_STORAGE) {
                if (data != null) {
                    val file = ImageChooserTools.proccedImageFromStorage(data, this@Step3VerifikasiActivity)
                    ktpSelfie = createFromFile(file)
                    viewPreviewFromGallery()
                    var bitmap = BitmapFactory.decodeFile(file.absolutePath)
//                    bitmap = ImageUtil.BitmapTools.rotate(bitmap, ImageUtil.bitmapRotation(windowManager))
                    image_preview.setImageBitmap(bitmap)
                } else {
                    ToastUtil.show(this@Step3VerifikasiActivity, getString(R.string.failed_load_image_alert))
                }
            }
        }
    }

    private fun createFromFile(file: File): MultipartBody.Part {
        val type: String
        val extension = MimeTypeMap.getFileExtensionFromUrl(file.absolutePath)
        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)!!
        val reqFile = RequestBody.create(MediaType.parse(type), file)
        val image = MultipartBody.Part.createFormData("ktp_selfie", file.name, reqFile)
        return image
    }

    fun getBitmap(image: Image): Bitmap {
        val buffer = image.planes[0].buffer
        val bytes = ByteArray(buffer.capacity())
        buffer.get(bytes)
        val bitmapImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, null)
        image.close()
        return bitmapImage
    }

    fun switchCamera() {
        if (mCameraId.equals(CAMERA_FRONT)) {
            mCameraId = CAMERA_BACK
            Camera2Tools.closeCamera()
            Camera2Tools.stopBackgroundThread()
            openCamera()
        } else if (mCameraId.equals(CAMERA_BACK)) {
            mCameraId = CAMERA_FRONT
            Camera2Tools.closeCamera()
            Camera2Tools.stopBackgroundThread()
            openCamera()
        }
    }

    fun openCamera() {
        viewTakeCamera()
        if (mTextureView!!.isAvailable()) {
            Camera2Tools.openCamera(manager, windowManager, mTextureView, mCameraId)
        } else {
            mTextureView!!.setSurfaceTextureListener(mSurfaceTextureListener)
        }
    }

    fun viewTakeCamera() {
        frame_camera.visibility = View.VISIBLE
        camera_button_container.visibility = View.VISIBLE
        image_preview_container.visibility = View.GONE
        texture_view.visibility = View.VISIBLE
        image_preview.visibility = View.GONE
        isPreview = false
    }

    fun viewPreviewFromCamera() {
        frame_camera.visibility = View.GONE
        camera_button_container.visibility = View.GONE
        image_preview_container.visibility = View.VISIBLE
        texture_view.visibility = View.VISIBLE
        image_preview.visibility = View.VISIBLE
        isPreview = true
    }

    fun viewPreviewFromGallery() {
        frame_camera.visibility = View.GONE
        camera_button_container.visibility = View.GONE
        image_preview_container.visibility = View.VISIBLE
        texture_view.visibility = View.VISIBLE
        image_preview.visibility = View.VISIBLE
        isPreview = true
    }
}