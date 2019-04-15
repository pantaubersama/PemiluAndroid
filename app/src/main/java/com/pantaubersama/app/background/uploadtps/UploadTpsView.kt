package com.pantaubersama.app.background.uploadtps

interface UploadTpsView {
    fun increaseProgress(progress: Int, title: String)
    fun showError(throwable: Throwable)
    fun showFailed(message: String?)
    fun onSuccessPublishTps()
    fun setMaxProgress(availableFormCount: Int)
}