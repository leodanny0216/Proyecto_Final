package edu.ucne.proyecto_final.presentation.proveedor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProveedorScreen(
    viewModel: ProveedorViewModel = hiltViewModel(),
    proveedorId: Int = 0,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

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
                title = {
                    Text(if (uiState.proveedorId == 0) "Nuevo Proveedor" else "Editar Proveedor")
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Mensajes de error
            item {
                uiState.errorMessage?.let { error ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Warning,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = error, color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }

            // Mensaje de éxito
            item {
                uiState.successMessage?.let { message ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = message)
                        }
                    }
                    LaunchedEffect(Unit) {
                        kotlinx.coroutines.delay(2000)
                        onNavigateBack()
                    }
                }
            }

            // Campo de nombre
            item {
                OutlinedTextField(
                    value = uiState.nombre,
                    onValueChange = { viewModel.setNombre(it) },
                    label = { Text("Nombre del Proveedor") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.AccountBox, contentDescription = null) },
                    singleLine = true,
                    isError = uiState.errorMessage?.contains("nombre", ignoreCase = true) == true
                )
            }

            // Campo de teléfono
            item {
                OutlinedTextField(
                    value = uiState.telefono,
                    onValueChange = { viewModel.setTelefono(it) },
                    label = { Text("Teléfono") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    isError = uiState.errorMessage?.contains("teléfono", ignoreCase = true) == true
                )
            }

            // Campo de email
            item {
                OutlinedTextField(
                    value = uiState.email,
                    onValueChange = { viewModel.setEmail(it) },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    isError = uiState.errorMessage?.contains("email", ignoreCase = true) == true
                )
            }

            // Campo de dirección
            item {
                OutlinedTextField(
                    value = uiState.direccion,
                    onValueChange = { viewModel.setDireccion(it) },
                    label = { Text("Dirección (Opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                    maxLines = 3,
                    minLines = 2
                )
            }

            // Botones de acción
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            viewModel.clearForm()
                            onNavigateBack()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar")
                    }

                    Button(
                        onClick = {
                            if (uiState.proveedorId == 0) {
                                viewModel.createProveedor()
                            } else {
                                viewModel.updateProveedor()
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = !uiState.isCreating && !uiState.isUpdating
                    ) {
                        if (uiState.isCreating || uiState.isUpdating) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text(if (uiState.proveedorId == 0) "Guardar" else "Actualizar")
                        }
                    }
                }
            }
        }
    }
}