package com.wanologicalsolutions.apps.bankadmin.helpers.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import java.util.*

@HiltAndroidApp
class BankApp : Application() {

    companion object {
        lateinit var instance: BankApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}