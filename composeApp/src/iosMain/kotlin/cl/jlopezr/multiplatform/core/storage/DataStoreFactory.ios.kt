package cl.jlopezr.multiplatform.core.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import kotlinx.cinterop.ExperimentalForeignApi
import okio.Path.Companion.toPath
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

/**
 * Implementación de DataStoreFactory para iOS
 */
actual object DataStoreFactory {
    private var dataStoreInstance: DataStore<Preferences>? = null
    
    @OptIn(ExperimentalForeignApi::class)
    actual fun createDataStore(): DataStore<Preferences> {
        // Patrón singleton simple para iOS
        return dataStoreInstance ?: run {
            val newInstance = PreferenceDataStoreFactory.createWithPath(
                produceFile = {
                    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
                        directory = NSDocumentDirectory,
                        inDomain = NSUserDomainMask,
                        appropriateForURL = null,
                        create = false,
                        error = null,
                    )
                    val path = requireNotNull(documentDirectory).path + "/tasks_preferences.preferences_pb"
                    path.toPath()
                }
            )
            dataStoreInstance = newInstance
            newInstance
        }
    }
}