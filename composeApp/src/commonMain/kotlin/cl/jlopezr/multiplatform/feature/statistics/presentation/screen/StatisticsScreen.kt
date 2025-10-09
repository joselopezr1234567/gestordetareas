package cl.jlopezr.multiplatform.feature.statistics.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cl.jlopezr.multiplatform.feature.statistics.presentation.event.StatisticsUiEvent
import cl.jlopezr.multiplatform.feature.statistics.presentation.viewmodel.StatisticsViewModel
import org.koin.compose.viewmodel.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: StatisticsViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState
    

    LaunchedEffect(Unit) {
        viewModel.onEvent(StatisticsUiEvent.LoadStatistics)
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver"
                )
            }
            Text(
                text = "Estadísticas",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            
            uiState.showEmptyState -> {
                EmptyStatisticsState()
            }
            
            uiState.errorMessage != null -> {
                ErrorState(
                    message = uiState.errorMessage,
                    onRetry = { viewModel.onEvent(StatisticsUiEvent.LoadStatistics) }
                )
            }
            
            else -> {
                StatisticsContent(
                    uiState = uiState,
                    onRefresh = { viewModel.onEvent(StatisticsUiEvent.RefreshStatistics) }
                )
            }
        }
    }
}

@Composable
private fun StatisticsContent(
    uiState: cl.jlopezr.multiplatform.feature.statistics.presentation.state.StatisticsUiState,
    onRefresh: () -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
            StatisticsCard(
                title = "Resumen General",
                icon = Icons.Default.Assessment
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatisticItem(
                        label = "Total de tareas",
                        value = uiState.statistics.totalTasks.toString(),
                        icon = Icons.Default.Assignment,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    StatisticItem(
                        label = "Tareas completadas",
                        value = uiState.statistics.completedTasks.toString(),
                        icon = Icons.Default.CheckCircle,
                        color = Color(0xFF4CAF50)
                    )
                    
                    StatisticItem(
                        label = "Tareas pendientes",
                        value = uiState.statistics.pendingTasks.toString(),
                        icon = Icons.Default.Schedule,
                        color = Color(0xFFFF9800)
                    )
                    
                    StatisticItem(
                        label = "Tareas vencidas",
                        value = uiState.statistics.overdueTasks.toString(),
                        icon = Icons.Default.Warning,
                        color = Color(0xFFF44336)
                    )
                }
            }
        }
        

        item {
            StatisticsCard(
                title = "Productividad",
                icon = Icons.Default.TrendingUp
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Percent,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Tasa de completitud",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Text(
                            text = "${(uiState.statistics.completionRate * 10).toInt() / 10.0}%",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    

                    LinearProgressIndicator(
                        progress = { uiState.statistics.completionRate / 100f },
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                    
                    StatisticItem(
                        label = "Tareas para hoy",
                        value = uiState.statistics.todayTasks.toString(),
                        icon = Icons.Default.Today,
                        color = Color(0xFF2196F3)
                    )
                    
                    StatisticItem(
                        label = "Tareas de alta prioridad",
                        value = uiState.statistics.highPriorityTasks.toString(),
                        icon = Icons.Default.PriorityHigh,
                        color = Color(0xFFE91E63)
                    )
                }
            }
        }
        

        item {
            Button(
                onClick = onRefresh,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Actualizar estadísticas")
            }
        }
    }
}

@Composable
private fun StatisticsCard(
    title: String,
    icon: ImageVector,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            content()
        }
    }
}

@Composable
private fun StatisticItem(
    label: String,
    value: String,
    icon: ImageVector,
    color: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
private fun EmptyStatisticsState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.BarChart,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.outline
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No hay datos para mostrar",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Crea algunas tareas para ver tus estadísticas",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Error,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Error al cargar estadísticas",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Reintentar")
        }
    }
}