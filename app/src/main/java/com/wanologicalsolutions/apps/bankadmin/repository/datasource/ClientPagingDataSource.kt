package com.wanologicalsolutions.apps.bankadmin.repository.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.wanologicalsolutions.apps.bankadmin.helpers.extensions.toArrayList
import com.wanologicalsolutions.apps.bankadmin.models.Client
import com.wanologicalsolutions.apps.bankadmin.models.SearchQuery
import com.wanologicalsolutions.apps.bankadmin.network.enums.Status
import com.wanologicalsolutions.apps.bankadmin.network.middlewares.toThrow

class ClientPagingDataSource(
    private val apiDataSource: ApiDataSource,
    private val localDataSource: LocalSearchDataSource,
    private val search: String? = null,
) : PagingSource<Int, Client>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Client> {
        return try {
            val nextPage = params.key ?: 1
            val response = apiDataSource.getClients(nextPage, mapOf())
            return if (response.status == Status.SUCCESS) {
                val data = response.data!!
                if (data.success == true) {
                    if (!search.isNullOrEmpty()) {
                        localDataSource.insertSearchQuery(SearchQuery(null, search))
                        return LoadResult.Page(
                            data = data.data?.data?.filter {
                                (it.firstName?.contains(search ?: "", ignoreCase = true) ?: false)
                                        || (it.lastName?.contains(search ?: "", ignoreCase = true)
                                    ?: false)
                                        || (it.email?.contains(search ?: "", ignoreCase = true)
                                    ?: false)
                            }?.toArrayList() ?: arrayListOf(),
                            prevKey = null,
                            nextKey = null
                        )
                    } else
                        LoadResult.Page(
                            data = data.data?.data ?: arrayListOf(),
                            prevKey = if (nextPage == 1) null else nextPage - 1,
                            nextKey = if (nextPage < data.data?.lastPage!!) nextPage + 1 else null
                        )
                } else {
                    LoadResult.Error(data.message?.toThrow()!!)
                }
            } else {
                LoadResult.Error(response.message?.toThrow()!!)
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Client>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }

}
