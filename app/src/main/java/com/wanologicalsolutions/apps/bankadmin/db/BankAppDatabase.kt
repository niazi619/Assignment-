package com.wanologicalsolutions.apps.bankadmin.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.wanologicalsolutions.apps.bankadmin.models.SearchQuery
import com.wanologicalsolutions.apps.bankadmin.repository.dao.SearchQueriesDao

@Database(entities = [SearchQuery::class], version = 1)
abstract class BankAppDatabase : RoomDatabase() {

    abstract fun locationQueriesDao(): SearchQueriesDao

    companion object {
        @Volatile
        private var instance: BankAppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                BankAppDatabase::class.java,
                "BankApp.db"
            ).build()
    }
}