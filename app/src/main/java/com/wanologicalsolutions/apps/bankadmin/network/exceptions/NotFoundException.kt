package com.wanologicalsolutions.apps.bankadmin.network.exceptions

import com.wanologicalsolutions.apps.bankadmin.config.CONSTANTS
import java.io.IOException

class NotFoundException : IOException() {
    override val message: String
        get() = CONSTANTS.NETWORK.NOT_FOUND
}