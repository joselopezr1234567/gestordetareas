package cl.jlopezr.multiplatform.feature.home.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import cl.jlopezr.multiplatform.feature.home.domain.model.Task


@Composable
fun TaskActionDialog(
    task: Task?,
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onCompleteTask: () -> Unit,
    onEditTask: () -> Unit
) {
    if (isVisible && task != null) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Task Reminder",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        
                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close dialog"
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = task.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            
                            if (!task.description.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = task.description,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            

                            Surface(
                                color = when (task.priority.name) {
                                    "HIGH" -> MaterialTheme.colorScheme.errorContainer
                                    "MEDIUM" -> MaterialTheme.colorScheme.primaryContainer
                                    else -> MaterialTheme.colorScheme.secondaryContainer
                                },
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "Priority: ${task.priority.name}",
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = when (task.priority.name) {
                                        "HIGH" -> MaterialTheme.colorScheme.onErrorContainer
                                        "MEDIUM" -> MaterialTheme.colorScheme.onPrimaryContainer
                                        else -> MaterialTheme.colorScheme.onSecondaryContainer
                                    }
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    

                    Text(
                        text = "What would you like to do with this task?",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        Button(
                            onClick = {
                                onCompleteTask()
                                onDismiss()
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Complete")
                        }
                        

                        OutlinedButton(
                            onClick = {
                                onEditTask()
                                onDismiss()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Edit")
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    

                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Dismiss",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}