package edu.ucne.proyecto_final.presentation.usuario

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: UsuarioViewModel = hiltViewModel(),
    onRegisterClick: () -> Unit,
    onNavigateToLogin: () -> Unit,
    goBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.successMessage) {
        if (uiState.successMessage != null && uiState.successMessage!!.contains("creado")) {
            onRegisterClick()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Registro") },
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF4CAF50),
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFE8F5E9),
                            Color.White
                        )
                    )
                )
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Crear Cuenta",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF2E7D32),
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Usuario
            OutlinedTextField(
                value = uiState.userName,
                onValueChange = { viewModel.setUserName(it) },
                label = { Text("Nombre de usuario") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),

            )

            Spacer(modifier = Modifier.height(16.dp))

            // Contraseña
            OutlinedTextField(
                value = uiState.password,
                onValueChange = { viewModel.setPassword(it) },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (uiState.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { viewModel.togglePasswordVisibility() }) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = if (uiState.passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                            tint = Color(0xFF2E7D32)
                        )
                    }
                },
                shape = RoundedCornerShape(16.dp),

            )

            Spacer(modifier = Modifier.height(16.dp))

            // Confirmar contraseña
            OutlinedTextField(
                value = uiState.confirmarPassword,
                onValueChange = { viewModel.setConfirmarPassword(it) },
                label = { Text("Confirmar contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (uiState.confirmarPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { viewModel.toggleConfirmarPasswordVisibility() }) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = if (uiState.confirmarPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                            tint = Color(0xFF2E7D32)
                        )
                    }
                },
                shape = RoundedCornerShape(16.dp),
            )

            uiState.errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón Registrar
            Button(
                onClick = { viewModel.createUsuario() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50),
                    contentColor = Color.White
                ),
                enabled = !uiState.isCreating
            ) {
                if (uiState.isCreating) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White
                    )
                } else {
                    Text("Registrarse")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onNavigateToLogin) {
                Text("¿Ya tienes cuenta? Inicia Sesión", color = Color(0xFF2E7D32))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(
        onRegisterClick = {},
        onNavigateToLogin = {},
        goBack = {}
    )
}
