package cl.jlopezr.multiplatform.feature.splash.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import cl.jlopezr.multiplatform.feature.splash.presentation.SplashStep
import cl.jlopezr.multiplatform.feature.splash.presentation.SplashUiEvent
import cl.jlopezr.multiplatform.feature.splash.presentation.SplashUiState
import cl.jlopezr.multiplatform.feature.splash.presentation.SplashViewModel
import org.koin.compose.viewmodel.koinViewModel

/**
 * Pantalla principal del Splash
 * Implementa una UI moderna con animaciones y estados de carga
 */
@Composable
fun SplashScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: SplashViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // Efectos de navegación
    LaunchedEffect(uiState.shouldNavigateToLogin) {
        if (uiState.shouldNavigateToLogin) {
            onNavigateToLogin()
        }
    }
    
    LaunchedEffect(uiState.shouldNavigateToHome) {
        if (uiState.shouldNavigateToHome) {
            onNavigateToHome()
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        when {
            uiState.shouldShowMaintenanceScreen -> {
                MaintenanceContent(
                    config = uiState.splashConfig,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            uiState.shouldShowUpdateDialog -> {
                UpdateRequiredContent(
                    onUpdateClick = { viewModel.onEvent(SplashUiEvent.UpdateApp) },
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            uiState.errorMessage != null -> {
                ErrorContent(
                    message = uiState.errorMessage!!,
                    onRetry = { viewModel.onEvent(SplashUiEvent.RetryLoading) },
                    onDismiss = { viewModel.onEvent(SplashUiEvent.DismissError) },
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            else -> {
                SplashContent(
                    uiState = uiState,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

/**
 * Contenido principal del splash durante la carga
 */
@Composable
private fun SplashContent(
    uiState: SplashUiState,
    modifier: Modifier = Modifier
) {
    val alphaAnimation by animateFloatAsState(
        targetValue = if (uiState.isLoading) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "alpha"
    )
    
    val progressAnimation by animateFloatAsState(
        targetValue = uiState.loadingProgress,
        animationSpec = tween(durationMillis = 800),
        label = "progress"
    )
    
    Column(
        modifier = modifier
            .alpha(alphaAnimation)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Nombre de la aplicación con fuente especial
        Text(
            text = "𝖏𝖑𝖔𝖕𝖊𝖟𝖗",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        // Indicador de progreso
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LinearProgressIndicator(
                progress = { progressAnimation },
                modifier = Modifier
                    .width(200.dp)
                    .height(4.dp),
                color = Color.White,
                trackColor = Color.Gray,
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = getStepMessage(uiState.currentStep),
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Contenido para modo de mantenimiento
 */
@Composable
private fun MaintenanceContent(
    config: cl.jlopezr.multiplatform.feature.splash.domain.model.SplashConfig?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🔧",
                fontSize = 48.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Mantenimiento en Progreso",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "La aplicación está temporalmente en mantenimiento. Por favor, inténtalo más tarde.",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        }
    }
}

/**
 * Contenido para actualización requerida
 */
@Composable
private fun UpdateRequiredContent(
    onUpdateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "📱",
                fontSize = 48.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Actualización Requerida",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Para continuar usando la aplicación, necesitas actualizar a la versión más reciente.",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = onUpdateClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Actualizar Ahora")
            }
        }
    }
}

/**
 * Contenido para errores
 */
@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "⚠️",
                fontSize = 48.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Error de Conexión",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = message,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancelar")
                }
                
                Button(
                    onClick = onRetry,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Reintentar")
                }
            }
        }
    }
}

/**
 * Obtiene el mensaje correspondiente al paso actual
 */
private fun getStepMessage(step: SplashStep): String {
    return when (step) {
        SplashStep.LOADING_CONFIG -> "Cargando configuración..."
        SplashStep.CHECKING_VERSION -> "Verificando versión..."
        SplashStep.VALIDATING_SESSION -> "Validando sesión..."
        SplashStep.COMPLETED -> "¡Listo!"
        SplashStep.ERROR -> "Error en la carga"
    }
}