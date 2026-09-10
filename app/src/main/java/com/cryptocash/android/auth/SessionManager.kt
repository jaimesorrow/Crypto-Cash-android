package com.cryptocash.android.auth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class SessionManager(private val context: Context) {

    companion object {
        private val KEY_FIRST_RUN = booleanPreferencesKey("first_run")
        private val KEY_PIN_HASH = stringPreferencesKey("pin_hash")
        private val KEY_LAST_ACTIVE = longPreferencesKey("last_active")
        private val KEY_GUEST_MODE = booleanPreferencesKey("guest_mode")
        private const val SESSION_TIMEOUT_MS = 5 * 60 * 1000L
    }

    fun isFirstRun(): Boolean = runBlocking {
        context.dataStore.data.first()[KEY_FIRST_RUN] ?: true
    }

    fun setFirstRunComplete() = runBlocking {
        context.dataStore.edit { it[KEY_FIRST_RUN] = false }
    }

    fun setPinHash(hash: String) = runBlocking {
        context.dataStore.edit { it[KEY_PIN_HASH] = hash }
    }

    fun getPinHash(): String? = runBlocking {
        context.dataStore.data.first()[KEY_PIN_HASH]
    }

    fun hasPinSet(): Boolean = getPinHash() != null

    fun updateLastActive() = runBlocking {
        context.dataStore.edit { it[KEY_LAST_ACTIVE] = System.currentTimeMillis() }
    }

    fun isSessionExpired(): Boolean = runBlocking {
        val last = context.dataStore.data.first()[KEY_LAST_ACTIVE] ?: return@runBlocking true
        System.currentTimeMillis() - last > SESSION_TIMEOUT_MS
    }

    fun setGuestMode(isGuest: Boolean) = runBlocking {
        context.dataStore.edit { it[KEY_GUEST_MODE] = isGuest }
    }

    fun isGuestMode(): Boolean = runBlocking {
        context.dataStore.data.first()[KEY_GUEST_MODE] ?: false
    }

    fun clearSession() = runBlocking {
        context.dataStore.edit { prefs ->
            prefs.remove(KEY_LAST_ACTIVE)
            prefs.remove(KEY_GUEST_MODE)
        }
    }
}
