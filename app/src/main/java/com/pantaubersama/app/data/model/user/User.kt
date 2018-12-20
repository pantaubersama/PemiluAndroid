package com.pantaubersama.app.data.model.user

import java.io.Serializable

class User(
    var id: String? = null,
    var name: String? = null,
    var username: String? = null,
    var bio: String? = null
) : Serializable