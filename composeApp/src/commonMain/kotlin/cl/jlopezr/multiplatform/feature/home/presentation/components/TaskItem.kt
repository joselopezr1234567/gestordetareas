package cl.jlopezr.multiplatform.feature.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cl.jlopezr.multiplatform.feature.home.domain.model.Task
import cl.jlopezr.multiplatform.feature.home.domain.model.TaskPriority
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskItem(
    task: Task,
    onEdit: (String) -> Unit,
    onDelete: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { onEdit(task.id) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (task.isCompleted) {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium,
                        textDecoration = if (task.isCompleted) {
                            TextDecoration.LineThrough
                        } else {
                            TextDecoration.None
                        }
                    ),
                    color = if (task.isCompleted) {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                

                if (!task.description.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (task.isCompleted) {
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    PriorityIndicator(priority = task.priority)
                    

                    task.dueDate?.let { dueDate ->
                        Spacer(modifier = Modifier.width(8.dp))
                        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                        val isOverdue = dueDate < now && !task.isCompleted
                        
                        Text(
                            text = "Vence: ${dueDate.date}",
                            style = MaterialTheme.typography.bodySmall,
                            color = when {
                                isOverdue -> MaterialTheme.colorScheme.error
                                dueDate.date == now.date -> MaterialTheme.colorScheme.primary
                                else -> MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                    }
                }
            }
            

            Row {
                IconButton(
                    onClick = { onEdit(task.id) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar tarea",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                IconButton(
                    onClick = { showDeleteDialog = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar tarea",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
    

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar tarea") },
            text = { Text("¿Estás seguro de que quieres eliminar esta tarea?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete(task.id)
                        showDeleteDialog = false
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
    

}


@Composable
private fun PriorityIndicator(
    priority: TaskPriority,
    modifier: Modifier = Modifier
) {
    val (color, text) = when (priority) {
        TaskPriority.HIGH -> MaterialTheme.colorScheme.error to "Alta"
        TaskPriority.MEDIUM -> Color(0xFFFF9800) to "Media"
        TaskPriority.LOW -> MaterialTheme.colorScheme.primary to "Baja"
    }
    
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.1f))
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.Medium
        )
    }
}