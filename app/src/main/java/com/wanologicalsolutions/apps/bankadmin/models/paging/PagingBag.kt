package com.tripscon.app.models.response.paging

import com.google.gson.annotations.SerializedName

data class PagingBag<T>(
    @SerializedName("current_page") var currentPage: Int? = null,
    @SerializedName("data") var data: ArrayList<T> = arrayListOf(),
    @SerializedName("from") var from: Int? = null,
    @SerializedName("last_page") var lastPage: Int? = null,
    @SerializedName("links") var links: ArrayList<Any> = arrayListOf(),
    @SerializedName("path") var path: String? = null,
    @SerializedName("per_page") var perPage: Int? = null,
    @SerializedName("to") var to: Int? = null,
    @SerializedName("total") var total: Int? = null
)