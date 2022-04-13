package com.wanologicalsolutions.apps.bankadmin.repository

import com.tripscon.app.models.response.paging.PagingBag
import com.wanologicalsolutions.apps.bankadmin.config.CONSTANTS
import com.wanologicalsolutions.apps.bankadmin.models.Client
import com.wanologicalsolutions.apps.bankadmin.models.CoreUser
import com.wanologicalsolutions.apps.bankadmin.models.requests.LoginRequest
import com.wanologicalsolutions.apps.bankadmin.network.responsebodies.ApiResponse
import retrofit2.Response
import retrofit2.http.*

/**
 * Api service
 *
 * @constructor Create empty Api service
 */
interface ApiService {

    /**
     * Login
     *
     * @param data
     * @return
     */
    @POST(CONSTANTS.APIS.LOGIN)
    suspend fun login(@Body data: LoginRequest?): Response<ApiResponse<CoreUser>>


    /**
     * Get clients
     *
     * @param page
     * @return
     */
    @GET(CONSTANTS.APIS.ALL_CLIENTS)
    suspend fun getClients(
        @Query("page") page: Int
    ): Response<ApiResponse<PagingBag<Client>>>


    /**
     * Create client
     *
     * @param data
     * @return
     */
    @FormUrlEncoded
    @POST(CONSTANTS.APIS.CREATE_CLIENT)
    suspend fun createClient(@FieldMap data: Map<String, String>): Response<ApiResponse<Any>>

    /**
     * Update client
     *
     * @param id
     * @param data
     * @return
     */
    @FormUrlEncoded
    @PUT(CONSTANTS.APIS.UPDATE_CLIENT)
    suspend fun updateClient(
        @Path("id") id: String?,
        @FieldMap data: Map<String, String>
    ): Response<ApiResponse<Any>>

    /**
     * Delete client
     *
     * @param id
     * @return
     */
    @DELETE(CONSTANTS.APIS.UPDATE_CLIENT)
    suspend fun deleteClient(
        @Path("id") id: String?
    ): Response<ApiResponse<Any>>

}