package com.wanologicalsolutions.apps.bankadmin.network.exceptions

import com.wanologicalsolutions.apps.bankadmin.config.CONSTANTS
import java.io.IOException

class UnAuthorizedException : IOException() {
    override val message: String
        get() = CONSTANTS.NETWORK.UN_AUTHORIZED
}