package cl.jlopezr.multiplatform.core.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import okio.Path.Companion.toPath
import java.io.File

/**
 * Implementación de DataStoreFactory para Android
 */
actual object DataStoreFactory {
    private lateinit var context: Context
    
    @Volatile
    private var dataStoreInstance: DataStore<Preferences>? = null
    
    fun initialize(context: Context) {
        this.context = context.applicationContext // Usar applicationContext para evitar memory leaks
    }
    
    actual fun createDataStore(): DataStore<Preferences> {
        // Double-checked locking pattern más robusto
        return dataStoreInstance ?: synchronized(this) {
            dataStoreInstance ?: run {
                if (!::context.isInitialized) {
                    throw IllegalStateException("DataStoreFactory must be initialized with a Context before creating DataStore")
                }
                
                PreferenceDataStoreFactory.createWithPath(
                    produceFile = { 
                        File(context.filesDir, "tasks_preferences.preferences_pb").absolutePath.toPath()
                    }
                ).also { 
                    dataStoreInstance = it 
                }
            }
        }
    }
}