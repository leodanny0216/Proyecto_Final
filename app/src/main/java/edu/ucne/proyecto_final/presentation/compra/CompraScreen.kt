package edu.ucne.proyecto_final.presentation.compra

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.ucne.proyecto_final.dto.remote.ProveedorDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompraScreen(
    viewModel: CompraViewModel = hiltViewModel(),
    compraId: Int = 0,
    onNavigateBack: () -> Unit,
    proveedores: List<ProveedorDto> = emptyList()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showProveedorDialog by remember { mutableStateOf(false) }

    LaunchedEffect(compraId) {
        if (compraId > 0) {
            val compra = uiState.compras.find { it.compraId == compraId }
            compra?.let { viewModel.setCompraForEdit(it) }
        } else {
            viewModel.clearForm()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (uiState.compraId == 0) "Nueva Compra" else "Editar Compra")
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

            // Selección de proveedor
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Proveedor",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = { showProveedorDialog = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.AccountBox, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = uiState.proveedor?.nombre ?: "Seleccionar proveedor"
                            )
                        }
                    }
                }
            }

            // Campo de fecha
            item {
                OutlinedTextField(
                    value = uiState.fecha,
                    onValueChange = { viewModel.setFecha(it) },
                    label = { Text("Fecha (YYYY-MM-DD)") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) },
                    singleLine = true
                )
            }

            // Campo de artículo
            item {
                OutlinedTextField(
                    value = uiState.articulo,
                    onValueChange = { viewModel.setArticulo(it) },
                    label = { Text("Artículo") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.ShoppingCart, contentDescription = null) },
                    singleLine = true
                )
            }

            // Campo de cantidad
            item {
                OutlinedTextField(
                    value = if (uiState.cantidad == 0) "" else uiState.cantidad.toString(),
                    onValueChange = {
                        val cantidad = it.toIntOrNull() ?: 0
                        viewModel.setCantidad(cantidad)
                    },
                    label = { Text("Cantidad") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Add, contentDescription = null) },
                    singleLine = true
                )
            }

            // Total
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Total: ${uiState.total}",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
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
                            if (uiState.compraId == 0) {
                                viewModel.createCompra()
                            } else {
                                viewModel.updateCompra()
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
                            Text(if (uiState.compraId == 0) "Guardar" else "Actualizar")
                        }
                    }
                }
            }
        }
    }

    // Diálogo para seleccionar proveedor
    if (showProveedorDialog) {
        AlertDialog(
            onDismissRequest = { showProveedorDialog = false },
            title = { Text("Seleccionar Proveedor") },
            text = {
                LazyColumn {
                    items(proveedores.size) { index ->
                        val proveedor = proveedores[index]
                        TextButton(
                            onClick = {
                                viewModel.setProveedor(proveedor)
                                showProveedorDialog = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = proveedor.nombre,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showProveedorDialog = false }) {
                    Text("Cerrar")
                }
            }
        )
    }
}