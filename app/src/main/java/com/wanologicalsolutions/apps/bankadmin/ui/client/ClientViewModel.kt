package com.wanologicalsolutions.apps.bankadmin.ui.client

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.wanologicalsolutions.apps.bankadmin.helpers.extensions.toJSONArray
import com.wanologicalsolutions.apps.bankadmin.models.Client
import dagger.hilt.android.lifecycle.HiltViewModel
import com.wanologicalsolutions.apps.bankadmin.repository.ApiRepository
import javax.inject.Inject

/**
 * Client view model
 *
 * @property apiRepository
 * @constructor Create empty Client view model
 */
@HiltViewModel
class ClientViewModel @Inject constructor(
    private val apiRepository: ApiRepository
) : ViewModel() {

    val clientId = MutableLiveData<String?>()
    val client = MutableLiveData<Client?>()

    val email = MutableLiveData<String?>()
    val firstName = MutableLiveData<String?>()
    val lastName = MutableLiveData<String?>()
    val personalId = MutableLiveData<String?>()
    val mobileNumber = MutableLiveData<String?>()
    val gender = MutableLiveData<String?>()
    val country = MutableLiveData<String?>()
    val city = MutableLiveData<String?>()
    val street = MutableLiveData<String?>()
    val zipCode = MutableLiveData<String?>()
    val accounts = MutableLiveData<ArrayList<String>>(arrayListOf())

    fun requestCreateClient() = apiRepository
        .createClient(mutableMapOf<String, String>().apply {
            put("email", email.value ?: "")
            put("first_name", firstName.value ?: "")
            put("last_name", lastName.value ?: "")
            put("personal_id", personalId.value ?: "")
            // put("profile_photo", email.value ?: "")
            put("mobile_number", mobileNumber.value ?: "")
            put("gender", gender.value ?: "")
            put("country", country.value ?: "")
            put("city", city.value ?: "")
            put("street", street.value ?: "")
            put("zip", zipCode.value ?: "")
            put("accounts", accounts.value?.toJSONArray()?.toString() ?: "[]")
        })
        .asLiveData(viewModelScope.coroutineContext)

    fun requestUpdateClient() = apiRepository
        .updateClient(clientId.value, mutableMapOf<String, String>().apply {
            put("email", email.value ?: "")
            put("first_name", firstName.value ?: "")
            put("last_name", lastName.value ?: "")
            put("personal_id", personalId.value ?: "")
            // put("profile_photo", email.value ?: "")
            put("mobile_number", mobileNumber.value ?: "")
            put("gender", gender.value ?: "")
            put("country", country.value ?: "")
            put("city", city.value ?: "")
            put("street", street.value ?: "")
            put("zip", zipCode.value ?: "")
            put("accounts", accounts.value?.toJSONArray()?.toString() ?: "[]")
        })
        .asLiveData(viewModelScope.coroutineContext)

}