package com.pantaubersama.app.data.model.tanyakandidat

import com.pantaubersama.app.data.model.user.User
import java.io.Serializable

class TanyaKandidat(
    var id: String? = null,
    var user: User? = null,
    var createdAt: String? = null,
    var question: String? = null,
    var upVoteCount: Int? = null,
    var isUpvoted: Boolean? = false
) : Serializable