package com.pantaubersama.app.data.remote.exception

open class ErrorInvalidTokenException : ErrorException {
    constructor() : super("Invalid token") {
        setCode(401)
    }
}