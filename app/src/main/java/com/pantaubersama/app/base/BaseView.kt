package com.pantaubersama.app.base

/**
 * @author edityomurti on 14/12/2018 17:37
 */
interface BaseView {
    fun showLoading(message: String)
    fun dismissLoading()
    fun showError(throwable: Throwable)
}