package cl.jlopezr.multiplatform.core.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

/**
 * Factory para crear instancias de DataStore de manera multiplataforma
 */
expect object DataStoreFactory {
    fun createDataStore(): DataStore<Preferences>
}