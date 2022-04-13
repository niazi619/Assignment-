package com.wanologicalsolutions.apps.bankadmin.models

import com.google.gson.annotations.SerializedName

data class CoreUser(
    @SerializedName("id") val id: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("access_token") var accessToken: String? = "",
)
