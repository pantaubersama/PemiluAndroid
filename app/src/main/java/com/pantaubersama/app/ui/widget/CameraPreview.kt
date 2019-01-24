package com.pantaubersama.app.ui.widget

import android.content.Context
import android.hardware.Camera
import android.view.SurfaceHolder
import android.view.SurfaceView
import timber.log.Timber
import java.io.IOException

class CameraPreview(
    context: Context,
    private var mCamera: Camera?
) : SurfaceView(context), SurfaceHolder.Callback {

    private val mHolder: SurfaceHolder = holder.apply {
        addCallback(this@CameraPreview)
        setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        mCamera.apply {
            try {
                this?.setPreviewDisplay(holder)
                this?.startPreview()
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
            mCamera?.stopPreview()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mCamera.apply {
            try {
                this?.setPreviewDisplay(mHolder)
                this?.startPreview()
            } catch (e: Exception) {
                Timber.e("Error starting camera preview: ${e.message}")
            }
        }
    }

    fun refreshCamera(camera: Camera?) {
        if (mHolder.surface == null) {
            return
        }
        try {
            mCamera?.stopPreview()
        } catch (e: Exception) {
        }
        setCamera(camera)
        try {
            mCamera?.setPreviewDisplay(mHolder)
            mCamera?.startPreview()
        } catch (e: Exception) {
            Timber.e("Error starting camera preview: ${e.message}")
        }
    }

    private fun setCamera(camera: Camera?) {
        if (camera != null) {
            mCamera = camera
        }
    }

    fun releaseCamera() {
        if (mCamera != null) {
            mCamera?.stopPreview()
            mCamera?.setPreviewCallback(null)
            mCamera?.release()
            mCamera = null
        }
    }
}