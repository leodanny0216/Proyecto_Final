package edu.ucne.proyecto_final.presentation.reclamo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

// Modelo temporal para tipos de reclamo
data class TipoReclamoSimple(val id: Int, val nombre: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReclamoScreen(
    viewModel: ReclamoViewModel = hiltViewModel(),
    reclamoId: Int = 0,
    onNavigateBack: () -> Unit,
    tiposReclamo: List<TipoReclamoSimple> = listOf(
        TipoReclamoSimple(1, "Producto defectuoso"),
        TipoReclamoSimple(2, "Servicio inadecuado"),
        TipoReclamoSimple(3, "Retraso en entrega"),
        TipoReclamoSimple(4, "Otro")
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    var showTipoReclamoDialog by remember { mutableStateOf(false) }
    var showEvidenciaDialog by remember { mutableStateOf(false) }
    var nuevaEvidencia by remember { mutableStateOf("") }

    LaunchedEffect(reclamoId) {
        if (reclamoId > 0) {
            val reclamo = uiState.reclamos.find { it.reclamoId == reclamoId }
            reclamo?.let { viewModel.setReclamoForEdit(it) }
        } else {
            viewModel.clearForm()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (uiState.reclamoId == 0) "Nuevo Reclamo" else "Editar Reclamo")
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

            // Selección de tipo de reclamo
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Tipo de Reclamo",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = { showTipoReclamoDialog = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Info, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (uiState.tipoReclamoNombre.isNotEmpty())
                                    uiState.tipoReclamoNombre
                                else
                                    "Seleccionar tipo"
                            )
                        }
                    }
                }
            }

            // Campo de fecha del incidente
            item {
                OutlinedTextField(
                    value = uiState.fechaIncidente,
                    onValueChange = { viewModel.setFechaIncidente(it) },
                    label = { Text("Fecha del Incidente (YYYY-MM-DD)") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) },
                    singleLine = true
                )
            }

            // Campo de descripción
            item {
                OutlinedTextField(
                    value = uiState.descripcion,
                    onValueChange = { viewModel.setDescripcion(it) },
                    label = { Text("Descripción del Reclamo") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                    minLines = 4,
                    maxLines = 6,
                    supportingText = {
                        Text("${uiState.descripcion.length} caracteres (mínimo 10)")
                    }
                )
            }

            // Sección de evidencias
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Evidencias",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            IconButton(onClick = { showEvidenciaDialog = true }) {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = "Agregar evidencia",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        if (uiState.evidencias.isEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No hay evidencias agregadas",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Lista de evidencias
            items(uiState.evidencias) { evidencia ->
                EvidenciaItem(
                    evidencia = evidencia,
                    onRemove = { viewModel.removeEvidencia(evidencia) }
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
                            if (uiState.reclamoId == 0) {
                                viewModel.createReclamo()
                            } else {
                                viewModel.updateReclamo()
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
                            Text(if (uiState.reclamoId == 0) "Guardar" else "Actualizar")
                        }
                    }
                }
            }
        }
    }

    // Diálogo para seleccionar tipo de reclamo
    if (showTipoReclamoDialog) {
        AlertDialog(
            onDismissRequest = { showTipoReclamoDialog = false },
            title = { Text("Seleccionar Tipo de Reclamo") },
            text = {
                LazyColumn {
                    items(tiposReclamo.size) { index ->
                        val tipo = tiposReclamo[index]
                        TextButton(
                            onClick = {
                                viewModel.setTipoReclamo(tipo.id, tipo.nombre)
                                showTipoReclamoDialog = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = tipo.nombre,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showTipoReclamoDialog = false }) {
                    Text("Cerrar")
                }
            }
        )
    }

    // Diálogo para agregar evidencia
    if (showEvidenciaDialog) {
        AlertDialog(
            onDismissRequest = {
                showEvidenciaDialog = false
                nuevaEvidencia = ""
            },
            title = { Text("Agregar Evidencia") },
            text = {
                OutlinedTextField(
                    value = nuevaEvidencia,
                    onValueChange = { nuevaEvidencia = it },
                    label = { Text("URL o descripción de la evidencia") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.addEvidencia(nuevaEvidencia)
                        nuevaEvidencia = ""
                        showEvidenciaDialog = false
                    },
                    enabled = nuevaEvidencia.isNotBlank()
                ) {
                    Text("Agregar")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showEvidenciaDialog = false
                    nuevaEvidencia = ""
                }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun EvidenciaItem(
    evidencia: String,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Info,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = evidencia,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            IconButton(onClick = onRemove) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}