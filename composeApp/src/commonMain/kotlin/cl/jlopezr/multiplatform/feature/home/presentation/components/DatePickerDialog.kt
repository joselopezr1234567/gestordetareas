package cl.jlopezr.multiplatform.feature.home.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.datetime.*

/**
 * Componente de diálogo para seleccionar fecha
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    onDateSelected: (LocalDateTime) -> Unit,
    onDismiss: () -> Unit,
    initialDate: LocalDateTime? = null
) {
    val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val selectedDate = remember { 
        mutableStateOf(initialDate ?: currentDate)
    }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Seleccionar fecha",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // DatePicker simple usando Material3
                val datePickerState = rememberDatePickerState(
                    initialSelectedDateMillis = selectedDate.value.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
                )
                
                DatePicker(
                    state = datePickerState,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar")
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Button(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val instant = Instant.fromEpochMilliseconds(millis)
                                val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault())
                                // Mantener la hora actual o usar mediodía si no hay hora inicial
                                val finalDateTime = if (initialDate != null) {
                                    LocalDateTime(
                                        localDate.date,
                                        initialDate.time
                                    )
                                } else {
                                    LocalDateTime(
                                        localDate.date,
                                        LocalTime(12, 0) // Mediodía por defecto
                                    )
                                }
                                onDateSelected(finalDateTime)
                            }
                        }
                    ) {
                        Text("Aceptar")
                    }
                }
            }
        }
    }
}