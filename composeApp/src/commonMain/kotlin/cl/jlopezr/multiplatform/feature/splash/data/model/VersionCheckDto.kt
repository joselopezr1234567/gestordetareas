package cl.jlopezr.multiplatform.feature.splash.data.model

/**
 * Data Transfer Object para la verificación de versión
 * Representa los datos de verificación de versión que vienen del FakeApiRepository
 */
data class VersionCheckDto(
    val currentVersion: String,
    val latestVersion: String,
    val isUpdateRequired: Boolean,
    val isUpdateAvailable: Boolean,
    val updateUrl: String,
    val releaseNotes: String
)