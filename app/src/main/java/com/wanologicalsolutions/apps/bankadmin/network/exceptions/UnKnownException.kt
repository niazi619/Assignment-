package com.wanologicalsolutions.apps.bankadmin.network.exceptions

import com.wanologicalsolutions.apps.bankadmin.config.CONSTANTS
import java.lang.Exception

class UnKnownException : Exception() {
    override val message: String
        get() = CONSTANTS.NETWORK.UNKNOWN_ERROR
}