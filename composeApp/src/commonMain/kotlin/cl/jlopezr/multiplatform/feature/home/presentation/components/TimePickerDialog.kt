package cl.jlopezr.multiplatform.feature.home.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.datetime.*

/**
 * Componente de diálogo para seleccionar fecha y hora del recordatorio
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    onDateTimeSelected: (LocalDateTime) -> Unit,
    onDismiss: () -> Unit,
    initialDateTime: LocalDateTime? = null
) {
    val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val initDateTime = initialDateTime ?: currentDateTime
    
    // Estados para fecha y hora
    var selectedYear by remember { mutableIntStateOf(initDateTime.year) }
    var selectedMonth by remember { mutableIntStateOf(initDateTime.monthNumber) }
    var selectedDay by remember { mutableIntStateOf(initDateTime.dayOfMonth) }
    var selectedHour by remember { mutableIntStateOf(initDateTime.hour) }
    var selectedMinute by remember { mutableIntStateOf(initDateTime.minute) }
    
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                // Título
                Text(
                    text = "Configurar Recordatorio",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Sección de Fecha
                Text(
                    text = "📅 Seleccionar Fecha",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Selectores de fecha en fila
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Día
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Día",
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        
                        var dayExpanded by remember { mutableStateOf(false) }
                        val daysInMonth = when (selectedMonth) {
                            1, 3, 5, 7, 8, 10, 12 -> 31
                            4, 6, 9, 11 -> 30
                            2 -> if (selectedYear % 4 == 0 && (selectedYear % 100 != 0 || selectedYear % 400 == 0)) 29 else 28
                            else -> 31
                        }
                        
                        ExposedDropdownMenuBox(
                            expanded = dayExpanded,
                            onExpandedChange = { dayExpanded = !dayExpanded }
                        ) {
                            OutlinedTextField(
                                value = selectedDay.toString(),
                                onValueChange = { },
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dayExpanded) },
                                modifier = Modifier.menuAnchor()
                            )
                            
                            ExposedDropdownMenu(
                                expanded = dayExpanded,
                                onDismissRequest = { dayExpanded = false }
                            ) {
                                for (day in 1..daysInMonth) {
                                    DropdownMenuItem(
                                        text = { Text(day.toString()) },
                                        onClick = {
                                            selectedDay = day
                                            dayExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    
                    // Mes
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Mes",
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        
                        var monthExpanded by remember { mutableStateOf(false) }
                        val months = listOf(
                            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
                        )
                        
                        ExposedDropdownMenuBox(
                            expanded = monthExpanded,
                            onExpandedChange = { monthExpanded = !monthExpanded }
                        ) {
                            OutlinedTextField(
                                value = months[selectedMonth - 1],
                                onValueChange = { },
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = monthExpanded) },
                                modifier = Modifier.menuAnchor()
                            )
                            
                            ExposedDropdownMenu(
                                expanded = monthExpanded,
                                onDismissRequest = { monthExpanded = false }
                            ) {
                                months.forEachIndexed { index, month ->
                                    DropdownMenuItem(
                                        text = { Text(month) },
                                        onClick = {
                                            selectedMonth = index + 1
                                            // Ajustar día si es necesario
                                            val maxDays = when (selectedMonth) {
                                                1, 3, 5, 7, 8, 10, 12 -> 31
                                                4, 6, 9, 11 -> 30
                                                2 -> if (selectedYear % 4 == 0 && (selectedYear % 100 != 0 || selectedYear % 400 == 0)) 29 else 28
                                                else -> 31
                                            }
                                            if (selectedDay > maxDays) selectedDay = maxDays
                                            monthExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    
                    // Año
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Año",
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        
                        var yearExpanded by remember { mutableStateOf(false) }
                        val currentYear = currentDateTime.year
                        val years = (currentYear..currentYear + 5).toList()
                        
                        ExposedDropdownMenuBox(
                            expanded = yearExpanded,
                            onExpandedChange = { yearExpanded = !yearExpanded }
                        ) {
                            OutlinedTextField(
                                value = selectedYear.toString(),
                                onValueChange = { },
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = yearExpanded) },
                                modifier = Modifier.menuAnchor()
                            )
                            
                            ExposedDropdownMenu(
                                expanded = yearExpanded,
                                onDismissRequest = { yearExpanded = false }
                            ) {
                                years.forEach { year ->
                                    DropdownMenuItem(
                                        text = { Text(year.toString()) },
                                        onClick = {
                                            selectedYear = year
                                            yearExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Sección de Hora
                Text(
                    text = "🕐 Seleccionar Hora",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Selectores de hora
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Hora
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Hora",
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        
                        var hourExpanded by remember { mutableStateOf(false) }
                        
                        ExposedDropdownMenuBox(
                            expanded = hourExpanded,
                            onExpandedChange = { hourExpanded = !hourExpanded }
                        ) {
                            OutlinedTextField(
                                value = selectedHour.toString().padStart(2, '0'),
                                onValueChange = { },
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = hourExpanded) },
                                modifier = Modifier.menuAnchor()
                            )
                            
                            ExposedDropdownMenu(
                                expanded = hourExpanded,
                                onDismissRequest = { hourExpanded = false }
                            ) {
                                for (hour in 0..23) {
                                    DropdownMenuItem(
                                        text = { Text(hour.toString().padStart(2, '0')) },
                                        onClick = {
                                            selectedHour = hour
                                            hourExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    
                    Text(
                        text = ":",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    // Minutos
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Minutos",
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        
                        var minuteExpanded by remember { mutableStateOf(false) }
                        
                        ExposedDropdownMenuBox(
                            expanded = minuteExpanded,
                            onExpandedChange = { minuteExpanded = !minuteExpanded }
                        ) {
                            OutlinedTextField(
                                value = selectedMinute.toString().padStart(2, '0'),
                                onValueChange = { },
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = minuteExpanded) },
                                modifier = Modifier.menuAnchor()
                            )
                            
                            ExposedDropdownMenu(
                                expanded = minuteExpanded,
                                onDismissRequest = { minuteExpanded = false }
                            ) {
                                for (minute in 0..59) {
                                    DropdownMenuItem(
                                        text = { Text(minute.toString().padStart(2, '0')) },
                                        onClick = {
                                            selectedMinute = minute
                                            minuteExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Resumen de la selección
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        Column {
                            Text(
                                text = "Recordatorio programado:",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            
                            Text(
                                text = "${selectedDay}/${selectedMonth}/${selectedYear} - ${selectedHour.toString().padStart(2, '0')}:${selectedMinute.toString().padStart(2, '0')}",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Botones de acción
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
                        onClick = {
                            try {
                                val localDate = LocalDate(selectedYear, selectedMonth, selectedDay)
                                val localTime = LocalTime(selectedHour, selectedMinute)
                                val finalDateTime = LocalDateTime(localDate, localTime)
                                onDateTimeSelected(finalDateTime)
                            } catch (e: Exception) {
                                // En caso de fecha inválida, usar fecha actual
                                val localTime = LocalTime(selectedHour, selectedMinute)
                                val finalDateTime = LocalDateTime(currentDateTime.date, localTime)
                                onDateTimeSelected(finalDateTime)
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Guardar")
                    }
                }
            }
        }
    }
}