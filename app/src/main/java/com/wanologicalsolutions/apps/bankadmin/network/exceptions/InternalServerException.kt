package com.wanologicalsolutions.apps.bankadmin.network.exceptions

import com.wanologicalsolutions.apps.bankadmin.config.CONSTANTS
import java.io.IOException

class InternalServerException : IOException() {
    override val message: String
        get() = CONSTANTS.NETWORK.INTERNAL_SERVER_ERROR
}