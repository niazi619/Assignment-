package com.wanologicalsolutions.apps.bankadmin.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_queries")
data class SearchQuery(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "query") val query: String?,
)
