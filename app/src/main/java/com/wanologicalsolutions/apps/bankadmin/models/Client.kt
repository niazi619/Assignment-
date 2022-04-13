package com.wanologicalsolutions.apps.bankadmin.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import org.json.JSONArray

@Parcelize
data class Client(
    @SerializedName("email") var email: String? = null,
    @SerializedName("first_name") var firstName: String? = null,
    @SerializedName("last_name") var lastName: String? = null,
    @SerializedName("personal_id") var personalId: String? = null,
    @SerializedName("profile_photo") var profilePhoto: String? = null,
    @SerializedName("mobile_number") var mobileNumber: String? = null,
    @SerializedName("gender") var gender: String? = null,
    @SerializedName("country") var country: String? = null,
    @SerializedName("city") var city: String? = null,
    @SerializedName("street") var street: String? = null,
    @SerializedName("zip") var zip: String? = null,
    @SerializedName("accounts") var accounts: String = "[]",
    @SerializedName("id") var id: String? = null
) : Parcelable {

    fun processAccounts() = arrayListOf<String>().apply {
        if (accounts.startsWith("[") && accounts.endsWith("]")) {
            JSONArray(accounts).let {
                (0 until it.length()).forEach { i ->
                    this.add(it.getString(i))
                }
            }
        }
    }
}