package com.wanologicalsolutions.apps.bankadmin.repository

import com.wanologicalsolutions.apps.bankadmin.models.requests.LoginRequest
import com.wanologicalsolutions.apps.bankadmin.network.middlewares.flowResponse
import com.wanologicalsolutions.apps.bankadmin.repository.datasource.ApiDataSource
import com.wanologicalsolutions.apps.bankadmin.repository.datasource.LocalSearchDataSource
import javax.inject.Inject

/**
 * Api repository
 *
 * @property apiDataSource
 * @property localDataSource
 * @constructor Create empty Api repository
 */
class ApiRepository @Inject constructor(
    private val apiDataSource: ApiDataSource,
    private val localDataSource: LocalSearchDataSource
) {

    /**
     * Login
     *
     * @param data
     */
    fun login(data: LoginRequest?) =
        flowResponse { apiDataSource.login(data) }

    /**
     * Create client
     *
     * @param data
     */
    fun createClient(data: Map<String, String> = mapOf()) =
        flowResponse { apiDataSource.createClient(data) }

    /**
     * Update client
     *
     * @param id
     * @param data
     */
    fun updateClient(id: String?, data: Map<String, String> = mapOf()) =
        flowResponse { apiDataSource.updateClient(id, data) }


    /**
     * Delete client
     *
     * @param id
     */
    fun deleteClient(id: String?) =
        flowResponse { apiDataSource.deleteClient(id) }


    fun recentSearches() = localDataSource.searchQueries()

}