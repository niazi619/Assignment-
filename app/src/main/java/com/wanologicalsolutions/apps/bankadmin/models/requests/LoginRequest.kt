package com.wanologicalsolutions.apps.bankadmin.models.requests

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email") var email: String? = null,
    @SerializedName("password") var password: String? = null
)
