package com.pantaubersama.app.background.uploadtps

interface UploadTpsView {
    fun increaseProgress(progress: Int)
    fun showError(throwable: Throwable)
    fun showFailed()
}