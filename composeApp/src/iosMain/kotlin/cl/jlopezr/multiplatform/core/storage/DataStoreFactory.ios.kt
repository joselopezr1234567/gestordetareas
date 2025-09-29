package cl.jlopezr.multiplatform.core.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

/**
 * Implementación de DataStoreFactory para iOS
 */
actual object DataStoreFactory {
    @Volatile
    private var dataStoreInstance: DataStore<Preferences>? = null
    
    actual fun createDataStore(): DataStore<Preferences> {
        // Double-checked locking pattern más robusto
        return dataStoreInstance ?: synchronized(this) {
            dataStoreInstance ?: run {
                PreferenceDataStoreFactory.createWithPath(
                    produceFile = {
                        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
                            directory = NSDocumentDirectory,
                            inDomain = NSUserDomainMask,
                            appropriateForURL = null,
                            create = false,
                            error = null,
                        )
                        requireNotNull(documentDirectory).path + "/tasks_preferences.preferences_pb"
                    }
                ).also { 
                    dataStoreInstance = it 
                }
            }
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun createDataStore(producePath: () -> String): DataStore<Preferences> =
    androidx.datastore.preferences.core.PreferenceDataStoreFactory.createWithPath(
        produceFile = { producePath() }
    )