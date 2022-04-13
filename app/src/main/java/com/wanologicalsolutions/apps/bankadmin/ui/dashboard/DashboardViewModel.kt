package com.wanologicalsolutions.apps.bankadmin.ui.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import com.wanologicalsolutions.apps.bankadmin.models.Client
import com.wanologicalsolutions.apps.bankadmin.models.SearchQuery
import com.wanologicalsolutions.apps.bankadmin.repository.datasource.ApiDataSource
import com.wanologicalsolutions.apps.bankadmin.repository.ApiRepository
import com.wanologicalsolutions.apps.bankadmin.repository.datasource.ClientPagingDataSource
import com.wanologicalsolutions.apps.bankadmin.repository.datasource.LocalSearchDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Dashboard view model
 *
 * @property apiRepository
 * @property apiDataSource
 * @constructor Create empty Dashboard view model
 */
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val apiRepository: ApiRepository,
    private val apiDataSource: ApiDataSource,
    private val localSearchDataSource: LocalSearchDataSource
) : ViewModel() {

    val searchQuery = MutableLiveData<String?>(null)

    private val _clients: Flow<PagingData<Client>> = Pager(PagingConfig(10)) {
        ClientPagingDataSource(
            apiDataSource,
            localSearchDataSource,
            searchQuery.value
        )
    }.flow.cachedIn(viewModelScope)

    val clients: Flow<PagingData<Client>>
        get() = _clients


    val recentSearches: Flow<List<SearchQuery>>
        get() = apiRepository.recentSearches()

    val clientId = MutableLiveData<String?>(null)

    fun requestDelete() = apiRepository
        .deleteClient(clientId.value)
        .asLiveData(viewModelScope.coroutineContext)
}