package edu.ucne.proyecto_final.presentation.usuario

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: UsuarioViewModel = hiltViewModel(),
    onLoginClick: (Int) -> Unit,
    onNavigateToRegister: () -> Unit,
    goBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Iniciar Sesión", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF4CAF50) // Verde principal
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFE8F5E9), // Verde muy suave
                            Color.White
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Bienvenido",
                    fontSize = 28.sp,
                    color = Color(0xFF2E7D32), // Verde oscuro
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(8.dp, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF1F8E9) // Verde muy suave/blanco
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        OutlinedTextField(
                            value = uiState.userName,
                            onValueChange = { viewModel.setUserName(it) },
                            label = { Text("Nombre de usuario") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = uiState.password,
                            onValueChange = { viewModel.setPassword(it) },
                            label = { Text("Contraseña") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            visualTransformation = if (uiState.passwordVisible) {
                                VisualTransformation.None
                            } else {
                                PasswordVisualTransformation()
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            trailingIcon = {
                                IconButton(onClick = { viewModel.togglePasswordVisibility() }) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = if (uiState.passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                                        tint = Color(0xFF2E7D32)
                                    )
                                }
                            }
                        )

                        uiState.errorMessage?.let { error ->
                            Text(
                                text = error,
                                color = Color(0xFFD32F2F),
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                if (uiState.userName.isNotBlank() && uiState.password.isNotBlank()) {
                                    onLoginClick(1)
                                } else {
                                    viewModel.uiState.value.copy(
                                        errorMessage = "Usuario y contraseña requeridos"
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4CAF50), // Verde principal
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Iniciar Sesión", fontSize = 18.sp)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        TextButton(onClick = onNavigateToRegister) {
                            Text("¿No tienes cuenta? Regístrate", color = Color(0xFF2E7D32))
                        }
                    }
                }
            }
        }
    }
}
