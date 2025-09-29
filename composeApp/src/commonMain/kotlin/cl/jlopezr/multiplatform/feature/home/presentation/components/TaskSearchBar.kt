package cl.jlopezr.multiplatform.feature.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.jlopezr.multiplatform.feature.home.domain.model.TaskFilter
import cl.jlopezr.multiplatform.feature.home.domain.model.TaskSortOrder

/**
 * Barra de búsqueda y filtros para las tareas
 */
@Composable
fun TaskSearchBar(
    searchQuery: String,
    currentFilter: TaskFilter,
    currentSortOrder: TaskSortOrder,
    onSearchQueryChange: (String) -> Unit,
    onFilterChange: (TaskFilter) -> Unit,
    onSortOrderChange: (TaskSortOrder) -> Unit,
    modifier: Modifier = Modifier
) {
    var showFilterMenu by remember { mutableStateOf(false) }
    var showSortMenu by remember { mutableStateOf(false) }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Barra de búsqueda
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Buscar tareas...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar"
                    )
                },
                trailingIcon = if (searchQuery.isNotEmpty()) {
                    {
                        IconButton(
                            onClick = { onSearchQueryChange("") }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Limpiar búsqueda"
                            )
                        }
                    }
                } else null,
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Botones de filtro y ordenamiento
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Filtro
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = { showFilterMenu = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filtrar",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = getFilterDisplayName(currentFilter),
                            color = Color.White,
                            style = TextStyle(fontSize = 18.sp)
                        )
                    }
                    
                    DropdownMenu(
                        expanded = showFilterMenu,
                        onDismissRequest = { showFilterMenu = false }
                    ) {
                        TaskFilter.values().forEach { filter ->
                            DropdownMenuItem(
                                text = { 
                                    Text(
                                        text = getFilterDisplayName(filter),
                                        color = Color.White,
                                        style = TextStyle(fontSize = 18.sp)
                                    ) 
                                },
                                onClick = {
                                    onFilterChange(filter)
                                    showFilterMenu = false
                                }
                            )
                        }
                    }
                }
                
                // Ordenamiento
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = { showSortMenu = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Sort,
                            contentDescription = "Ordenar",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = getSortOrderDisplayName(currentSortOrder),
                            color = Color.White,
                            style = TextStyle(fontSize = 18.sp)
                        )
                    }
                    
                    DropdownMenu(
                        expanded = showSortMenu,
                        onDismissRequest = { showSortMenu = false }
                    ) {
                        TaskSortOrder.values().forEach { sortOrder ->
                            DropdownMenuItem(
                                text = { 
                                    Text(
                                        text = getSortOrderDisplayName(sortOrder),
                                        color = Color.White,
                                        style = TextStyle(fontSize = 18.sp)
                                    ) 
                                },
                                onClick = {
                                    onSortOrderChange(sortOrder)
                                    showSortMenu = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Obtiene el nombre para mostrar del filtro
 */
private fun getFilterDisplayName(filter: TaskFilter): String {
    return when (filter) {
        TaskFilter.ALL -> "Todas"
        TaskFilter.PENDING -> "𝒫𝑒𝓃𝒹𝒾𝑒𝓃𝓉𝑒𝓈"
        TaskFilter.COMPLETED -> "Completadas"
    }
}

/**
 * Obtiene el nombre para mostrar del orden
 */
private fun getSortOrderDisplayName(sortOrder: TaskSortOrder): String {
    return when (sortOrder) {
        TaskSortOrder.CREATED_DATE_DESC -> "ℳá𝓈 𝓇𝑒𝒸𝒾𝑒𝓃𝓉𝑒𝓈"
        TaskSortOrder.CREATED_DATE_ASC -> "Más antiguas"
        TaskSortOrder.DUE_DATE_ASC -> "Vencimiento ↑"
        TaskSortOrder.DUE_DATE_DESC -> "Vencimiento ↓"
        TaskSortOrder.PRIORITY_DESC -> "Prioridad ↓"
        TaskSortOrder.PRIORITY_ASC -> "Prioridad ↑"
        TaskSortOrder.TITLE_ASC -> "Título A-Z"
        TaskSortOrder.TITLE_DESC -> "Título Z-A"
        TaskSortOrder.COMPLETION_STATUS -> "Estado de completado"
    }
}