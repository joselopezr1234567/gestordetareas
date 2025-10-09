package cl.jlopezr.multiplatform.feature.login.presentation

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun LoginScreen(
    onNavigateToHome: () -> Unit,
    viewModel: LoginViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Navegar al home cuando el login sea exitoso
    LaunchedEffect(uiState.isLoginSuccessful) {
        if (uiState.isLoginSuccessful) {
            onNavigateToHome()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Black
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Título
                Text(
                    text = "𝕴𝖓𝖎𝖈𝖎𝖆𝖗 𝖘𝖊𝖘𝖎ó𝖓",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo de Email
                OutlinedTextField(
                    value = uiState.email,
                    onValueChange = { viewModel.onEvent(LoginUiEvent.EmailChanged(it)) },
                    label = { 
                        Text(
                            text = "𝙴𝚖𝚊𝚒𝚕",
                            color = Color.White
                        ) 
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email",
                            tint = Color.White
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    isError = uiState.emailError != null,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        disabledTextColor = Color.White,
                        errorTextColor = Color.White,
                        focusedBorderColor = if (uiState.emailError == null && uiState.email.contains("@")) Color(0xFF27F5DD) else Color.White,
                        unfocusedBorderColor = if (uiState.emailError == null && uiState.email.contains("@")) Color(0xFF27F5DD) else Color.Gray,
                        errorBorderColor = Color.Red,
                        cursorColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White,
                        disabledLabelColor = Color.White,
                        errorLabelColor = Color.White,
                        focusedLeadingIconColor = Color.White,
                        unfocusedLeadingIconColor = Color.White,
                        disabledLeadingIconColor = Color.White,
                        errorLeadingIconColor = Color.White
                    ),
                    singleLine = true
                )

                // Error de Email
                if (uiState.emailError != null) {
                    Text(
                        text = uiState.emailError!!,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Campo de Contraseña
                OutlinedTextField(
                    value = uiState.password,
                    onValueChange = { viewModel.onEvent(LoginUiEvent.PasswordChanged(it)) },
                    label = { 
                        Text(
                            text = "𝙲𝚘𝚗𝚝𝚛𝚊𝚜𝚎ñ𝚊",
                            color = Color.White
                        ) 
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Contraseña",
                            tint = Color.White
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = { viewModel.onEvent(LoginUiEvent.TogglePasswordVisibility) }
                        ) {
                            Text(
                                text = if (uiState.isPasswordVisible) "👁️" else "🙈",
                                color = Color.White
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (uiState.isPasswordVisible) 
                        VisualTransformation.None 
                    else 
                        PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    isError = uiState.passwordError != null,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        disabledTextColor = Color.White,
                        errorTextColor = Color.White,
                        focusedBorderColor = if (uiState.passwordError == null && uiState.password.isNotBlank()) 
                            Color(0xFF27F5DD) else Color.White,
                        unfocusedBorderColor = if (uiState.passwordError == null && uiState.password.isNotBlank()) 
                            Color(0xFF27F5DD) else Color.Gray,
                        errorBorderColor = Color.Red,
                        cursorColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White,
                        disabledLabelColor = Color.White,
                        errorLabelColor = Color.White,
                        focusedLeadingIconColor = Color.White,
                        unfocusedLeadingIconColor = Color.White,
                        disabledLeadingIconColor = Color.White,
                        errorLeadingIconColor = Color.White,
                        focusedTrailingIconColor = Color.White,
                        unfocusedTrailingIconColor = Color.White,
                        disabledTrailingIconColor = Color.White,
                        errorTrailingIconColor = Color.White
                    ),
                    singleLine = true
                )

                // Error de Contraseña
                if (uiState.passwordError != null) {
                    Text(
                        text = uiState.passwordError!!,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Checkbox Recordar sesión
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = uiState.rememberMe,
                        onCheckedChange = { viewModel.onEvent(LoginUiEvent.RememberMeChanged(it)) },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color.White,
                            uncheckedColor = Color.Gray,
                            checkmarkColor = Color.Black
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "𝚁𝚎𝚌𝚘𝚛𝚍𝚊𝚛 𝚜𝚎𝚜𝚒ó𝚗",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Botón de Login
                Button(
                    onClick = { viewModel.onEvent(LoginUiEvent.Login) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = uiState.isFormValid && !uiState.isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White,
                        disabledContainerColor = Color.Black,
                        disabledContentColor = Color.White,
                    ),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(2.dp, Color.White)
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "𝕴𝖓𝖎𝖈𝖎𝖆𝖗 𝖘𝖊𝖘𝖎ó𝖓",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Mensaje de Error General
                if (uiState.errorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = uiState.errorMessage!!,
                        color = Color.Red,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}