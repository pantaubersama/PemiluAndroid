package com.pantaubersama.app.data.remote.exception

import java.lang.RuntimeException

open class ErrorException : RuntimeException {

    private var code = -1

    constructor() : super("Oops, terjadi kesalahan")

    constructor(message: String) : super(message)

    constructor(code: Int, message: String) : super(message) {
        this.code = code
    }

    fun getCode(): Int {
        return code
    }

    fun setCode(code: Int) {
        this.code = code
    }

    override val message: String?
        get() = if (super.message.equals("")) {
            "oops, terjadi kesalahan" // Unknown Error
        } else {
            super.message
        }
}