package edu.ucne.proyecto_final.presentation.proveedor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProveedorScreen(
    viewModel: ProveedorViewModel = hiltViewModel(),
    proveedorId: Int = 0,
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val greenColor = Color(0xFF4CAF50)

    LaunchedEffect(proveedorId) {
        if (proveedorId > 0) {
            val proveedor = uiState.proveedores.find { it.proveedorId == proveedorId }
            proveedor?.let { viewModel.setProveedorForEdit(it) }
        } else {
            viewModel.clearForm()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (uiState.proveedorId == 0) "Nuevo Proveedor" else "Editar Proveedor", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = greenColor)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .background(Color(0xFFF5F5F5)),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Mensajes de error
            uiState.errorMessage?.let { error ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = Color.Red)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = error, color = Color.Red)
                    }
                }
            }

            // Mensaje de éxito
            uiState.successMessage?.let { message ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = greenColor)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = message, color = greenColor)
                    }
                }
                LaunchedEffect(Unit) {
                    delay(2000)
                    onNavigateBack()
                }
            }

            // Campo de nombre
            OutlinedTextField(
                value = uiState.nombre,
                onValueChange = { viewModel.setNombre(it) },
                label = { Text("Nombre del Proveedor") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.AccountBox, contentDescription = null, tint = greenColor) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = greenColor,
                    unfocusedBorderColor = greenColor.copy(alpha = 0.5f),
                    cursorColor = greenColor,
                    focusedLabelColor = greenColor
                )
            )

            // Campo de teléfono
            OutlinedTextField(
                value = uiState.telefono,
                onValueChange = { viewModel.setTelefono(it) },
                label = { Text("Teléfono") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = greenColor) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = greenColor,
                    unfocusedBorderColor = greenColor.copy(alpha = 0.5f),
                    cursorColor = greenColor
                )
            )

            // Campo de email
            OutlinedTextField(
                value = uiState.email,
                onValueChange = { viewModel.setEmail(it) },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = greenColor) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = greenColor,
                    unfocusedBorderColor = greenColor.copy(alpha = 0.5f),
                    cursorColor = greenColor
                )
            )

            // Campo de dirección
            OutlinedTextField(
                value = uiState.direccion,
                onValueChange = { viewModel.setDireccion(it) },
                label = { Text("Dirección (Opcional)") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = greenColor) },
                maxLines = 3,
                minLines = 2,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = greenColor,
                    unfocusedBorderColor = greenColor.copy(alpha = 0.5f),
                    cursorColor = greenColor
                )
            )

            // Botones de acción
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = { viewModel.clearForm(); onNavigateBack() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = greenColor)
                ) {
                    Text("Cancelar")
                }

                Button(
                    onClick = {
                        if (uiState.proveedorId == 0) viewModel.createProveedor() else viewModel.updateProveedor()
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !uiState.isCreating && !uiState.isUpdating,
                    colors = ButtonDefaults.buttonColors(containerColor = greenColor)
                ) {
                    if (uiState.isCreating || uiState.isUpdating) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                    } else {
                        Text(if (uiState.proveedorId == 0) "Guardar" else "Actualizar", color = Color.White)
                    }
                }
            }
        }
    }
}

// ================== PREVIEW ==================
@Preview(showBackground = true)
@Composable
fun ProveedorScreenPreview() {
    ProveedorScreen()
}
