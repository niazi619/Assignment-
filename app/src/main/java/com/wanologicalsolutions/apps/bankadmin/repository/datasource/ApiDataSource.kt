package com.wanologicalsolutions.apps.bankadmin.repository.datasource

import com.wanologicalsolutions.apps.bankadmin.models.requests.LoginRequest
import com.wanologicalsolutions.apps.bankadmin.network.middlewares.BaseDataSource
import com.wanologicalsolutions.apps.bankadmin.repository.ApiService
import javax.inject.Inject

/**
 * Api data source
 *
 * @property apiService
 * @constructor Create empty Api data source
 */
class ApiDataSource @Inject constructor(private val apiService: ApiService) : BaseDataSource() {

    /**
     * Login
     *
     * @param data
     */
    suspend fun login(data: LoginRequest?) =
        getResult { apiService.login(data) }

    /**
     * Get clients
     *
     * @param page
     * @param data
     */
    suspend fun getClients(page: Int, data: Map<String, String> = mapOf()) =
        getResult { apiService.getClients(page) }

    /**
     * Create client
     *
     * @param data
     */
    suspend fun createClient(data: Map<String, String>) =
        getResult { apiService.createClient(data) }

    /**
     * Update client
     *
     * @param id
     * @param data
     */
    suspend fun updateClient(id: String?, data: Map<String, String>) =
        getResult { apiService.updateClient(id, data) }

    /**
     * Delete client
     *
     * @param id
     */
    suspend fun deleteClient(id: String?) =
        getResult { apiService.deleteClient(id) }

}