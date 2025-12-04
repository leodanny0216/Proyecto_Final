package edu.ucne.proyecto_final.presentation.insumo

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

data class CategoriaSimple(val id: Int, val nombre: String)
data class ProveedorSimple(val id: Int, val nombre: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsumoScreen(
    viewModel: InsumoViewModel = hiltViewModel(),
    insumoId: Int = 0,
    onNavigateBack: () -> Unit,
    categorias: List<CategoriaSimple> = emptyList(),
    proveedores: List<ProveedorSimple> = emptyList()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showCategoriaDialog by remember { mutableStateOf(false) }
    var showProveedorDialog by remember { mutableStateOf(false) }

    LaunchedEffect(insumoId) {
        if (insumoId > 0) {
            val insumo = uiState.insumos.find { it.insumoId == insumoId }
            insumo?.let { viewModel.setInsumoForEdit(it) }
        } else {
            viewModel.clearForm()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (uiState.insumoId == 0) "Nuevo Insumo" else "Editar Insumo")
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

            item {
                OutlinedTextField(
                    value = uiState.nombre,
                    onValueChange = { viewModel.setNombre(it) },
                    label = { Text("Nombre del Insumo") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Build, contentDescription = null) },
                    singleLine = true
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Categoría",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = { showCategoriaDialog = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (uiState.categoriaNombre.isNotEmpty())
                                    uiState.categoriaNombre
                                else
                                    "Seleccionar categoría"
                            )
                        }
                    }
                }
            }

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
                                text = if (uiState.proveedorNombre.isNotEmpty())
                                    uiState.proveedorNombre
                                else
                                    "Seleccionar proveedor"
                            )
                        }
                    }
                }
            }

            item {
                OutlinedTextField(
                    value = if (uiState.stockInicial == 0) "" else uiState.stockInicial.toString(),
                    onValueChange = {
                        val stock = it.toIntOrNull() ?: 0
                        viewModel.setStockInicial(stock)
                    },
                    label = { Text("Stock Inicial") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Info, contentDescription = null) },
                    singleLine = true
                )
            }

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
                            if (uiState.insumoId == 0) {
                                viewModel.createInsumo()
                            } else {
                                viewModel.updateInsumo()
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
                            Text(if (uiState.insumoId == 0) "Guardar" else "Actualizar")
                        }
                    }
                }
            }
        }
    }
    if (showCategoriaDialog) {
        AlertDialog(
            onDismissRequest = { showCategoriaDialog = false },
            title = { Text("Seleccionar Categoría") },
            text = {
                LazyColumn {
                    items(categorias.size) { index ->
                        val categoria = categorias[index]
                        TextButton(
                            onClick = {
                                viewModel.setCategoria(categoria.id, categoria.nombre)
                                showCategoriaDialog = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = categoria.nombre,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showCategoriaDialog = false }) {
                    Text("Cerrar")
                }
            }
        )
    }

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
                                viewModel.setProveedor(proveedor.id, proveedor.nombre)
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