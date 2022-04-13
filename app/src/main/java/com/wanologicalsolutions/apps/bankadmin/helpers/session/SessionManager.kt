package com.wanologicalsolutions.apps.bankadmin.helpers.session

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.asLiveData
import com.wanologicalsolutions.apps.bankadmin.helpers.app.BankApp
import com.wanologicalsolutions.apps.bankadmin.models.CoreUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.io.IOException

enum class UserRole {
    TRAVELER,
    HOST,
    SERVICE_PROVIDER;

    fun toRoleId() = when (this) {
        TRAVELER -> 0
        HOST -> 2
        else -> 1
    }
}

private val Context.userPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "user"
)

object SessionManager {

    private val dataStore: DataStore<Preferences>
        get() = BankApp.instance.userPreferencesDataStore

    val isLogin: Boolean
        get() = isLoginFlow.asLiveData().value ?: false

    suspend fun isLogin() = dataStore.data.firstOrNull()?.get(IS_LOGIN) ?: false

    val isLoginFlow: Flow<Boolean> = dataStore.data.map { preference ->
        preference[IS_LOGIN] ?: false
    }

    val user: Flow<CoreUser> = dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map { preference ->
            CoreUser(
                preference[USER_ID],
                preference[USER_NAME],
            )
        }

    suspend fun login(user: CoreUser) {
        dataStore.edit { preferences ->
            preferences[IS_LOGIN] = true
            preferences[USER_ID] = user.id ?: "0"
            preferences[USER_NAME] = user.name ?: ""
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    private val IS_LOGIN = booleanPreferencesKey("isLogin")
    private val USER_ID = stringPreferencesKey("id")
    private val USER_NAME = stringPreferencesKey("name")

}