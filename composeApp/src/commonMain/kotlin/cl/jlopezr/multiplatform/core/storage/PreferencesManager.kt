package cl.jlopezr.multiplatform.core.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


class PreferencesManager(
    private val dataStore: DataStore<Preferences>
) {
    

    suspend fun putString(key: String, value: String) {
        val prefKey = stringPreferencesKey(key)
        dataStore.edit { preferences ->
            preferences[prefKey] = value
        }
    }
    

    suspend fun getString(key: String, defaultValue: String? = null): String? {
        val prefKey = stringPreferencesKey(key)
        return dataStore.data.map { preferences ->
            preferences[prefKey] ?: defaultValue
        }.first()
    }
    

    suspend fun putBoolean(key: String, value: Boolean) {
        val prefKey = booleanPreferencesKey(key)
        dataStore.edit { preferences ->
            preferences[prefKey] = value
        }
    }
    

    suspend fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        val prefKey = booleanPreferencesKey(key)
        return dataStore.data.map { preferences ->
            preferences[prefKey] ?: defaultValue
        }.first()
    }
    

    suspend fun putInt(key: String, value: Int) {
        val prefKey = intPreferencesKey(key)
        dataStore.edit { preferences ->
            preferences[prefKey] = value
        }
    }
    

    suspend fun getInt(key: String, defaultValue: Int = 0): Int {
        val prefKey = intPreferencesKey(key)
        return dataStore.data.map { preferences ->
            preferences[prefKey] ?: defaultValue
        }.first()
    }
    

    suspend fun putLong(key: String, value: Long) {
        val prefKey = longPreferencesKey(key)
        dataStore.edit { preferences ->
            preferences[prefKey] = value
        }
    }
    

    suspend fun getLong(key: String, defaultValue: Long = 0L): Long {
        val prefKey = longPreferencesKey(key)
        return dataStore.data.map { preferences ->
            preferences[prefKey] ?: defaultValue
        }.first()
    }
    

    suspend fun remove(key: String) {
        dataStore.edit { preferences ->
            val stringKey = stringPreferencesKey(key)
            val booleanKey = booleanPreferencesKey(key)
            val intKey = intPreferencesKey(key)
            val longKey = longPreferencesKey(key)
            
            preferences.remove(stringKey)
            preferences.remove(booleanKey)
            preferences.remove(intKey)
            preferences.remove(longKey)
        }
    }
    

    suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
    

    suspend fun contains(key: String): Boolean {
        val stringKey = stringPreferencesKey(key)
        val booleanKey = booleanPreferencesKey(key)
        val intKey = intPreferencesKey(key)
        val longKey = longPreferencesKey(key)
        
        return dataStore.data.map { preferences ->
            preferences.contains(stringKey) || 
            preferences.contains(booleanKey) || 
            preferences.contains(intKey) || 
            preferences.contains(longKey)
        }.first()
    }
    

    suspend fun getAllKeys(): Set<String> {
        return dataStore.data.map { preferences ->
            preferences.asMap().keys.map { it.name }.toSet()
        }.first()
    }
}