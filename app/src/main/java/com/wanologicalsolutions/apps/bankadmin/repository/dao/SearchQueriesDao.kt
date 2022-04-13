package com.wanologicalsolutions.apps.bankadmin.repository.dao

import androidx.room.*
import com.wanologicalsolutions.apps.bankadmin.models.SearchQuery
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchQueriesDao {

    @Query("SELECT * FROM search_queries ORDER BY id DESC LIMIT 3 ;")
    fun recentSearchQueries(): Flow<List<SearchQuery>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(query: SearchQuery)

    @Delete
    suspend fun delete(query: SearchQuery)
}