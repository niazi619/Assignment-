package com.wanologicalsolutions.apps.bankadmin.config

enum class MODE {
    LOCAL,
    STAGING,
    PRODUCTION
}

object CONSTANTS {

    // Configuration Controls
    object CONFIG {
        val APP_MODE = MODE.STAGING
        fun isProductionMode(): Boolean = APP_MODE == MODE.PRODUCTION
    }

    // Host
    object SERVER {
        private const val LOCAL_BASE_URL = ""
        private const val STAGING_BASE_URL = "https://5ff9be5217386d0017b5210a.mockapi.io/"
        private const val PRODUCTION_BASE_URL = ""
        val BASE_URL: String
            get() {
                return when (CONFIG.APP_MODE) {
                    MODE.PRODUCTION -> PRODUCTION_BASE_URL
                    MODE.STAGING -> STAGING_BASE_URL
                    else -> LOCAL_BASE_URL
                }
            }
    }

    object API {
        const val V1 = "api/v1/"
    }

    // Apis
    object APIS {
        const val LOGIN = API.V1.plus("login")
        const val ALL_CLIENTS = API.V1.plus("clients")
        const val CREATE_CLIENT = API.V1.plus("clients")
        const val UPDATE_CLIENT = API.V1.plus("clients/{id}")
    }

    // Network
    object NETWORK {
        const val READ_TIMEOUT: Long = 30
        const val CONNECTION_TIMEOUT: Long = 10

        const val SOMETHING_WENT_WRONG = "Something went wrong.\nReason: %s"
        const val INTERNET_CONNECTIVITY = "Internet connection issue"
        const val NOT_FOUND = "Not Found"
        const val UN_AUTHORIZED = "User Unauthorized"
        const val INTERNAL_SERVER_ERROR = "Server Internal Error"
        const val UNKNOWN_ERROR = "Unknown Error Occurred"
    }

    object MESSAGES {
        const val SOMETHING_WENT_WRONG_PUBLIC = "Something went wrong. Please try later."
        const val UPLOADING_FAILED = "Something went wrong during uploading. Please try later."
        const val AGREE_POLICIES = "Please agree with all terms and policies by marking check."
    }
}