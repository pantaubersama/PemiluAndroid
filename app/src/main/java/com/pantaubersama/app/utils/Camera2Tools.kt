package com.pantaubersama.app.utils

import android.annotation.SuppressLint
import android.graphics.ImageFormat
import android.graphics.Point
import android.graphics.SurfaceTexture
import android.hardware.camera2.* // ktlint-disable
import android.media.Image
import android.media.ImageReader
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.util.Size
import android.view.Surface
import android.view.TextureView
import android.view.WindowManager
import java.util.ArrayList
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

        /**
         * Max preview width that is guaranteed by Camera2 API
         */
        private val MAX_PREVIEW_WIDTH = 1920

        /**
         * Max preview height that is guaranteed by Camera2 API
         */
        private val MAX_PREVIEW_HEIGHT = 1080

        private var mState = STATE_PREVIEW

        lateinit var cameraId: String

        lateinit var previewSize: Size

        lateinit var windowManager: WindowManager

        private var mSensorOrientation: Int = 0

        fun bitmapRotation(): Int {
            var rotation = 0
            when (windowManager.defaultDisplay.rotation) {
                Surface.ROTATION_0 -> rotation = 90
                Surface.ROTATION_90 -> rotation = 0
                Surface.ROTATION_180 -> rotation = 270
                Surface.ROTATION_270 -> rotation = 180
            }
            return rotation
        }

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
            texture!!.setDefaultBufferSize(previewSize.width, previewSize.height)
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
        fun openCamera(manager: CameraManager, windowManager: WindowManager, mTextureView: TextureView?, cameraId: String) {
            this.windowManager = windowManager
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

            // To otpimal size when preview ini some devices, like preview size on Nexus and Huawei devices.
//            previewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture::class.java), mTextureView!!.width, mTextureView!!.height)

            mImageReader = ImageReader.newInstance(largest.getWidth(), largest.getHeight(),
                    ImageFormat.JPEG, /*maxImages*/2)

            // Find out if we need to swap dimension to get the preview size relative to sensor
            // coordinate.
            val displayRotation = windowManager.getDefaultDisplay().getRotation()
            //noinspection ConstantConditions
            mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)!!
            var swappedDimensions = false
            when (displayRotation) {
                Surface.ROTATION_0, Surface.ROTATION_180 -> if (mSensorOrientation == 90 || mSensorOrientation == 270) {
                    swappedDimensions = true
                }
                Surface.ROTATION_90, Surface.ROTATION_270 -> if (mSensorOrientation == 0 || mSensorOrientation == 180) {
                    swappedDimensions = true
                }
                else -> Log.e("Camera", "Display rotation is invalid: $displayRotation")
            }

            val displaySize = Point()
            windowManager.getDefaultDisplay().getSize(displaySize)
            var rotatedPreviewWidth = mTextureView?.width
            var rotatedPreviewHeight = mTextureView?.height
            var maxPreviewWidth = displaySize.x
            var maxPreviewHeight = displaySize.y

            if (swappedDimensions) {
                rotatedPreviewWidth = mTextureView?.width
                rotatedPreviewHeight = mTextureView?.height
                maxPreviewWidth = displaySize.y
                maxPreviewHeight = displaySize.x
            }

            if (maxPreviewWidth > MAX_PREVIEW_WIDTH) {
                maxPreviewWidth = MAX_PREVIEW_WIDTH
            }

            if (maxPreviewHeight > MAX_PREVIEW_HEIGHT) {
                maxPreviewHeight = MAX_PREVIEW_HEIGHT
            }

            // Danger, W.R.! Attempting to use too large a preview size could  exceed the camera
            // bus' bandwidth limitation, resulting in gorgeous previews but the storage of
            // garbage capture data.
            previewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture::class.java), mTextureView!!.width, mTextureView!!.height)

            // We fit the aspect ratio of TextureView to the size of preview we picked.
//            val orientation = windowManager.defaultDisplay.rotation
//            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                mTextureView?.setAspectRatio(
//                        previewSize.getWidth(), previewSize.getHeight())
//            } else {
//                mTextureView?.setAspectRatio(
//                        previewSize.getHeight(), previewSize.getWidth())
//            }

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
                if (cameraId.equals("0"))
                    mCaptureSession?.capture(mPreviewRequestBuilder?.build(), mCaptureCallback,
                            mBackgroundHandler)
                else
                    captureStillPicture()
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

            // Orientation
//            val rotation = windowManager.getDefaultDisplay().getRotation()
//            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, getOrientation(rotation))

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

        private fun chooseOptimalSize(outputSizes: Array<Size>, width: Int, height: Int): Size {
            val preferredRatio = height / width.toDouble()
            var currentOptimalSize = outputSizes[0]
            var currentOptimalRatio = currentOptimalSize.width / currentOptimalSize.height.toDouble()
            for (currentSize in outputSizes) {
                val currentRatio = currentSize.width / currentSize.height.toDouble()
                if (Math.abs(preferredRatio - currentRatio) < Math.abs(preferredRatio - currentOptimalRatio)) {
                    currentOptimalSize = currentSize
                    currentOptimalRatio = currentRatio
                }
            }
            return currentOptimalSize
        }

        /**
         * Given `choices` of `Size`s supported by a camera, choose the smallest one that
         * is at least as large as the respective texture view size, and that is at most as large as the
         * respective max size, and whose aspect ratio matches with the specified value. If such size
         * doesn't exist, choose the largest one that is at most as large as the respective max size,
         * and whose aspect ratio matches with the specified value.
         *
         * @param choices The list of sizes that the camera supports for the intended output
         * class
         * @param textureViewWidth The width of the texture view relative to sensor coordinate
         * @param textureViewHeight The height of the texture view relative to sensor coordinate
         * @param maxWidth The maximum width that can be chosen
         * @param maxHeight The maximum height that can be chosen
         * @param aspectRatio The aspect ratio
         * @return The optimal `Size`, or an arbitrary one if none were big enough
         */
        private fun chooseOptimalSize(
            choices: Array<Size>,
            textureViewWidth: Int,
            textureViewHeight: Int,
            maxWidth: Int,
            maxHeight: Int,
            aspectRatio: Size
        ): Size {

            // Collect the supported resolutions that are at least as big as the preview Surface
            val bigEnough = ArrayList<Size>()
            // Collect the supported resolutions that are smaller than the preview Surface
            val notBigEnough = ArrayList<Size>()
            val w = aspectRatio.width
            val h = aspectRatio.height
            for (option in choices) {
                if (option.width <= maxWidth && option.height <= maxHeight &&
                        option.height == option.width * h / w) {
                    if (option.width >= textureViewWidth && option.height >= textureViewHeight) {
                        bigEnough.add(option)
                    } else {
                        notBigEnough.add(option)
                    }
                }
            }

            // Pick the smallest of those big enough. If there is no one big enough, pick the
            // largest of those not big enough.
            if (bigEnough.size > 0) {
                return Collections.min(bigEnough, CompareSizesByArea())
            } else if (notBigEnough.size > 0) {
                return Collections.max(notBigEnough, CompareSizesByArea())
            } else {
                Log.e("Camera", "Couldn't find any suitable preview size")
                return choices[0]
            }
        }

        private fun getOrientation(rotation: Int): Int {
            return (bitmapRotation() + mSensorOrientation + 270) % 360
        }
    }
}