package cl.jlopezr.multiplatform.feature.splash.data.model


data class VersionCheckDto(
    val currentVersion: String,
    val latestVersion: String,
    val isUpdateRequired: Boolean,
    val isUpdateAvailable: Boolean,
    val updateUrl: String,
    val releaseNotes: String
)