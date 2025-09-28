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
    
    fun initialize(context: Context) {
        this.context = context
    }
    
    actual fun createDataStore(): DataStore<Preferences> {
        return PreferenceDataStoreFactory.createWithPath(
            produceFile = { 
                File(context.filesDir, "tasks_preferences.preferences_pb").absolutePath.toPath()
            }
        )
    }
}