package com.wanologicalsolutions.apps.bankadmin.helpers.extensions

fun <T> List<T>.toArrayList(): ArrayList<T> {
    return ArrayList(this)
}

fun <T> Array<T>.toArrayList(): ArrayList<T> {
    return ArrayList(this.asList())
}