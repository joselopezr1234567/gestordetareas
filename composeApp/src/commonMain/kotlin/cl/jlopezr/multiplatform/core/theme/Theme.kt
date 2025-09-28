package cl.jlopezr.multiplatform.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import org.koin.compose.koinInject

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    surface = Color(0xFF121212),
    surfaceVariant = SurfaceVariantDark,
    outline = OutlineDark,
    error = ErrorColorDark
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    surface = Color(0xFFFFFFFF),
    surfaceVariant = SurfaceVariantLight,
    outline = OutlineLight,
    error = ErrorColor
)

/**
 * Tema principal de la aplicación
 * Maneja automáticamente el cambio entre tema claro y oscuro
 */
@Composable
fun AgendaTareasTheme(
    themeManager: ThemeManager = koinInject(),
    content: @Composable () -> Unit
) {
    val currentTheme by themeManager.currentTheme.collectAsState()
    val isDarkTheme by themeManager.isDarkTheme.collectAsState()
    
    // Determinar si usar tema oscuro
    val useDarkTheme = when (currentTheme) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }
    
    val colorScheme = if (useDarkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

/**
 * Extensiones para acceder a colores personalizados
 */
val ColorScheme.taskPriorityHigh: Color
    @Composable get() = if (isSystemInDarkTheme()) TaskPriorityHighDark else TaskPriorityHigh

val ColorScheme.taskPriorityMedium: Color
    @Composable get() = if (isSystemInDarkTheme()) TaskPriorityMediumDark else TaskPriorityMedium

val ColorScheme.taskPriorityLow: Color
    @Composable get() = if (isSystemInDarkTheme()) TaskPriorityLowDark else TaskPriorityLow

val ColorScheme.successColor: Color
    @Composable get() = if (isSystemInDarkTheme()) SuccessColorDark else SuccessColor

val ColorScheme.warningColor: Color
    @Composable get() = if (isSystemInDarkTheme()) WarningColorDark else WarningColor

val ColorScheme.infoColor: Color
    @Composable get() = if (isSystemInDarkTheme()) InfoColorDark else InfoColor