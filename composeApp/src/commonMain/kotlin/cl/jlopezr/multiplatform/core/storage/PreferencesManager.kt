package cl.jlopezr.multiplatform.core.storage

/**
 * Gestor de preferencias para almacenamiento local
 * Abstrae el almacenamiento de datos simples de forma persistente
 * En una implementación real usaría SharedPreferences (Android) o UserDefaults (iOS)
 */
class PreferencesManager {
    
    // Simulación de almacenamiento local (en una app real sería persistente)
    private val storage = mutableMapOf<String, Any>()
    
    /**
     * Guarda un string en las preferencias
     */
    suspend fun putString(key: String, value: String) {
        storage[key] = value
    }
    
    /**
     * Obtiene un string de las preferencias
     */
    suspend fun getString(key: String, defaultValue: String? = null): String? {
        return storage[key] as? String ?: defaultValue
    }
    
    /**
     * Guarda un boolean en las preferencias
     */
    suspend fun putBoolean(key: String, value: Boolean) {
        storage[key] = value
    }
    
    /**
     * Obtiene un boolean de las preferencias
     */
    suspend fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return storage[key] as? Boolean ?: defaultValue
    }
    
    /**
     * Guarda un int en las preferencias
     */
    suspend fun putInt(key: String, value: Int) {
        storage[key] = value
    }
    
    /**
     * Obtiene un int de las preferencias
     */
    suspend fun getInt(key: String, defaultValue: Int = 0): Int {
        return storage[key] as? Int ?: defaultValue
    }
    
    /**
     * Guarda un long en las preferencias
     */
    suspend fun putLong(key: String, value: Long) {
        storage[key] = value
    }
    
    /**
     * Obtiene un long de las preferencias
     */
    suspend fun getLong(key: String, defaultValue: Long = 0L): Long {
        return storage[key] as? Long ?: defaultValue
    }
    
    /**
     * Elimina una clave específica de las preferencias
     */
    suspend fun remove(key: String) {
        storage.remove(key)
    }
    
    /**
     * Limpia todas las preferencias
     */
    suspend fun clear() {
        storage.clear()
    }
    
    /**
     * Verifica si existe una clave en las preferencias
     */
    suspend fun contains(key: String): Boolean {
        return storage.containsKey(key)
    }
    
    /**
     * Obtiene todas las claves almacenadas
     */
    suspend fun getAllKeys(): Set<String> {
        return storage.keys.toSet()
    }
}