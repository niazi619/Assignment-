package com.wanologicalsolutions.apps.bankadmin.helpers.extensions

import android.text.TextUtils
import org.json.JSONArray

fun <String> ArrayList<String>.toJSONArray(): JSONArray {
    val jsonArray = JSONArray()
    this.forEach {
        jsonArray.put(it)
    }
    return jsonArray
}

fun String.isEmailValid(): Boolean {
    return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}