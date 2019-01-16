package com.pantaubersama.app.utils

import android.annotation.SuppressLint
import android.graphics.ImageFormat
import android.hardware.camera2.* // ktlint-disable
import android.media.Image
import android.media.ImageReader
import android.os.Handler
import android.os.HandlerThread
import android.util.Size
import android.view.Surface
import android.view.TextureView
import java.util.Collections
import java.util.Arrays
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit

class Camera2Tools {
    companion object {
        private val mCameraOpenCloseLock = Semaphore(1)
        private var mBackgroundHandler: Handler? = null
        private var mBackgroundThread: HandlerThread? = null
        private lateinit var mPreviewRequestBuilder: CaptureRequest.Builder
        private var mCameraDevice: CameraDevice? = null
        private var mCaptureSession: CameraCaptureSession? = null
        private var mImageReader: ImageReader? = null
        private var mFlashSupported: Boolean = false
        private var mPreviewRequest: CaptureRequest? = null
        var mTextureView: TextureView? = null
        /**
         * Camera state: Showing camera preview.
         */
        private val STATE_PREVIEW = 0

        /**
         * Camera state: Waiting for the focus to be locked.
         */
        private val STATE_WAITING_LOCK = 1

        /**
         * Camera state: Waiting for the exposure to be precapture state.
         */
        private val STATE_WAITING_PRECAPTURE = 2

        /**
         * Camera state: Waiting for the exposure state to be something other than precapture.
         */
        private val STATE_WAITING_NON_PRECAPTURE = 3

        /**
         * Camera state: Picture was taken.
         */
        private val STATE_PICTURE_TAKEN = 4

        private var mState = STATE_PREVIEW

        lateinit var cameraId: String

        private val mStateCallback = object : CameraDevice.StateCallback() {
            override fun onOpened(cameraDevice: CameraDevice?) {
                mCameraOpenCloseLock.release()
                mCameraDevice = cameraDevice
                createCameraPreviewSession()
            }

            override fun onClosed(camera: CameraDevice?) {}
            override fun onDisconnected(camera: CameraDevice?) {}
            override fun onError(camera: CameraDevice?, error: Int) {}
        }

        private fun createCameraPreviewSession() {
            val texture = mTextureView?.getSurfaceTexture()
            // We configure the size of default buffer to be the size of camera preview we want.
            texture!!.setDefaultBufferSize(mTextureView!!.width, mTextureView!!.height)
            // This is the output Surface we need to start preview.
            val surface = Surface(texture)

            // We set up a CaptureRequest.Builder with the output Surface.
            mPreviewRequestBuilder = mCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            mPreviewRequestBuilder.addTarget(surface)

            mCameraDevice?.createCaptureSession(Arrays.asList(surface, mImageReader?.surface),
                    object : CameraCaptureSession.StateCallback() {

                        override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                            // The camera is already closed
                            if (null == mCameraDevice) {
                                return
                            }

                            // When the session is ready, we start displaying the preview.
                            mCaptureSession = cameraCaptureSession
                            try {
                                // Auto focus should be continuous for camera preview.
                                mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
                                // Flash is automatically enabled when necessary.
                                setAutoFlash(mPreviewRequestBuilder)
                                // Finally, we start displaying the camera preview.
                                mPreviewRequest = mPreviewRequestBuilder?.build()
                                mCaptureSession?.setRepeatingRequest(mPreviewRequest,
                                        mCaptureCallback, mBackgroundHandler)
                            } catch (e: CameraAccessException) {
                                e.printStackTrace()
                            }
                        }

                        override fun onConfigureFailed(cameraCaptureSession: CameraCaptureSession) {
                        }
                    }, null
            )
        }

        private fun setAutoFlash(requestBuilder: CaptureRequest.Builder) {
            if (mFlashSupported) {
                requestBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                        CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH)
            }
        }

        @SuppressLint("MissingPermission")
        fun openCamera(manager: CameraManager, mTextureView: TextureView?, cameraId: String) {
            this.mTextureView = mTextureView
            this.cameraId = cameraId
            setUpCamera(manager)
            startBackgroundHandler()
            try {
                if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                    throw RuntimeException("Time out waiting to lock camera opening.")
                }
                manager.openCamera(cameraId, mStateCallback, mBackgroundHandler)
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                throw RuntimeException("Interrupted while trying to lock camera opening.", e)
            }
        }

        private fun setUpCamera(manager: CameraManager) {
            val characteristics = manager.getCameraCharacteristics(cameraId)

            val cameraDirection = characteristics.get(CameraCharacteristics.LENS_FACING)
            if (cameraDirection != null &&
                    cameraDirection == CameraCharacteristics.LENS_FACING_FRONT) {
                CameraCharacteristics.LENS_FACING_BACK
            } else
                CameraCharacteristics.LENS_FACING_FRONT

            val map = characteristics.get(
                    CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
            // For still image captures, we use the largest available size.
            val largest = Collections.max(
                    Arrays.asList<Size>(*map.getOutputSizes(ImageFormat.JPEG)),
                    CompareSizesByArea())
            mImageReader = ImageReader.newInstance(largest.width, largest.height,
                    ImageFormat.JPEG, /*maxImages*/2)

            // Check if the flash is supported.
            val available = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)
            mFlashSupported = available ?: false
        }

        interface ImageHandler {
            fun handleImage(image: Image): Runnable
        }

        internal class CompareSizesByArea : Comparator<Size> {

            override fun compare(lhs: Size, rhs: Size): Int {
                // We cast here to ensure the multiplications won't overflow
                return java.lang.Long.signum(lhs.width.toLong() * lhs.height - rhs.width.toLong() * rhs.height)
            }
        }

        fun startBackgroundHandler() {
            if (mBackgroundThread != null) return
            mBackgroundThread = HandlerThread("Camera-$cameraId").also {
                it.start()
                mBackgroundHandler = Handler(it.looper)
            }
        }

        fun takePicture(handler: ImageHandler) {
            // Set up a callback for Image Reader
            mImageReader?.setOnImageAvailableListener(object : ImageReader.OnImageAvailableListener {
                override fun onImageAvailable(reader: ImageReader) {
                    val image = reader.acquireNextImage()
                    mBackgroundHandler?.post(handler.handleImage(image = image))
                }
            }, mBackgroundHandler)

            // Start locking focus
            lockFocus()
        }

        private fun lockFocus() {
            try {
                // This is how to tell the camera to lock focus.
                mPreviewRequestBuilder?.set(CaptureRequest.CONTROL_AF_TRIGGER,
                        CameraMetadata.CONTROL_AF_TRIGGER_START)
                // Tell #mCaptureCallback to wait for the lock.
                mState = STATE_WAITING_LOCK
                mCaptureSession?.capture(mPreviewRequestBuilder?.build(), mCaptureCallback,
                        mBackgroundHandler)
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }
        }

        fun unlockFocus() {
            try {
                // Reset the auto-focus trigger
                mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                        CameraMetadata.CONTROL_AF_TRIGGER_CANCEL)
                setAutoFlash(mPreviewRequestBuilder)
                mCaptureSession?.capture(mPreviewRequestBuilder.build(), mCaptureCallback,
                        mBackgroundHandler)
                // After this, the camera will go back to the normal state of preview.
                mState = STATE_PREVIEW
                mCaptureSession?.setRepeatingRequest(mPreviewRequest, mCaptureCallback,
                        mBackgroundHandler)
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }
        }

        private val mCaptureCallback = object : CameraCaptureSession.CaptureCallback() {
            private fun process(result: CaptureResult) {
                when (mState) {
                    STATE_PREVIEW -> {
                        // We have nothing to do when the camera preview is working normally.
                    }
                    STATE_WAITING_LOCK -> {
                        val afState = result.get(CaptureResult.CONTROL_AF_STATE)
                        if (afState == null) {
                            captureStillPicture()
                        } else if (CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED == afState || CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED == afState) {
                            // CONTROL_AE_STATE can be null on some devices
                            val aeState = result.get(CaptureResult.CONTROL_AE_STATE)
                            if (aeState == null || aeState == CaptureResult.CONTROL_AE_STATE_CONVERGED) {
                                mState = Camera2Tools.STATE_PICTURE_TAKEN
                                captureStillPicture()
                            } else {
                                runPrecaptureSequence()
                            }
                        }
                    }
                    STATE_WAITING_PRECAPTURE -> {
                        // CONTROL_AE_STATE can be null on some devices
                        val aeState = result.get(CaptureResult.CONTROL_AE_STATE)
                        if (aeState == null ||
                                aeState == CaptureResult.CONTROL_AE_STATE_PRECAPTURE ||
                                aeState == CaptureRequest.CONTROL_AE_STATE_FLASH_REQUIRED) {
                            mState = STATE_WAITING_NON_PRECAPTURE
                        }
                    }
                    STATE_WAITING_NON_PRECAPTURE -> {
                        // CONTROL_AE_STATE can be null on some devices
                        val aeState = result.get(CaptureResult.CONTROL_AE_STATE)
                        if (aeState == null || aeState != CaptureResult.CONTROL_AE_STATE_PRECAPTURE) {
                            mState = STATE_PICTURE_TAKEN
                            captureStillPicture()
                        }
                    }
                }
            }

            override fun onCaptureProgressed(session: CameraCaptureSession, request: CaptureRequest, partialResult: CaptureResult) {
                process(partialResult)
            }

            override fun onCaptureCompleted(session: CameraCaptureSession, request: CaptureRequest, result: TotalCaptureResult) {
                process(result)
            }
        }

        private fun runPrecaptureSequence() {
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER,
                    CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER_START)
            // Tell #mCaptureCallback to wait for the precapture sequence to be set.
            mState = STATE_WAITING_PRECAPTURE
            mCaptureSession?.capture(mPreviewRequestBuilder.build(), mCaptureCallback,
                    mBackgroundHandler)
        }

        private fun captureStillPicture() {
            // This is the CaptureRequest.Builder that we use to take a picture.
            val captureBuilder = mCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
            captureBuilder.addTarget(mImageReader?.surface)

            // Use the same AE and AF modes as the preview.
            captureBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
            setAutoFlash(captureBuilder)

            val CaptureCallback = object : CameraCaptureSession.CaptureCallback() {

                override fun onCaptureCompleted(
                    session: CameraCaptureSession,
                    request: CaptureRequest,
                    result: TotalCaptureResult
                ) {
                }
            }

            mCaptureSession?.stopRepeating()
            mCaptureSession?.abortCaptures()
            mCaptureSession?.capture(captureBuilder.build(), CaptureCallback, null)
        }

        fun closeCamera() {
            try {
                mCameraOpenCloseLock.acquire()
                if (null != mCaptureSession) {
                    mCaptureSession?.close()
                    mCaptureSession = null
                }
                if (null != mCameraDevice) {
                    mCameraDevice?.close()
                    mCameraDevice = null
                }
                if (null != mImageReader) {
                    mImageReader?.close()
                    mImageReader = null
                }
            } catch (e: InterruptedException) {
                throw RuntimeException("Interrupted while trying to lock camera closing.", e)
            } finally {
                mCameraOpenCloseLock.release()
            }
        }

        fun stopBackgroundThread() {
            mBackgroundThread?.quitSafely()
            try {
                mBackgroundThread?.join()
                mBackgroundThread = null
                mBackgroundHandler = null
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }
}