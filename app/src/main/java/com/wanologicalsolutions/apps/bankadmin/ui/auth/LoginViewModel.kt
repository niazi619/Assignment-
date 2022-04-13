package com.wanologicalsolutions.apps.bankadmin.ui.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import com.wanologicalsolutions.apps.bankadmin.helpers.session.SessionManager
import com.wanologicalsolutions.apps.bankadmin.models.CoreUser
import com.wanologicalsolutions.apps.bankadmin.models.requests.LoginRequest
import com.wanologicalsolutions.apps.bankadmin.repository.ApiRepository
import javax.inject.Inject

/**
 * Login view model
 *
 * @property apiRepository
 * @constructor Create empty Login view model
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val apiRepository: ApiRepository
) : ViewModel() {


    var loginRequest =
        MutableLiveData(LoginRequest())

    /**
     * Request login
     *
     */
    fun requestLogin() = apiRepository
        .login(loginRequest.value)
        .map {
            if (it.data?.success == true)
                SessionManager.login(
                    CoreUser(
                        it.data.data?.id,
                        it.data.data?.name,
                    )
                )
            it
        }
        .asLiveData(viewModelScope.coroutineContext)

}