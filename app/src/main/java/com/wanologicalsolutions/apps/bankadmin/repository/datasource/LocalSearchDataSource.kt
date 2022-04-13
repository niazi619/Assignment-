package com.wanologicalsolutions.apps.bankadmin.repository.datasource

import com.wanologicalsolutions.apps.bankadmin.db.BankAppDatabase
import com.wanologicalsolutions.apps.bankadmin.models.SearchQuery
import javax.inject.Inject

class LocalSearchDataSource @Inject constructor(private val database: BankAppDatabase) {

    suspend fun insertSearchQuery(query: SearchQuery) =
        database.locationQueriesDao().insert(query)

    suspend fun deleteSearchQuery(query: SearchQuery) =
        database.locationQueriesDao().delete(query)

    fun searchQueries() = database.locationQueriesDao().recentSearchQueries()

}