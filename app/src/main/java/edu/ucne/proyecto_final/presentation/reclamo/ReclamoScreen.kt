// package edu.ucne.proyecto_final.presentation.reclamo
package edu.ucne.proyecto_final.presentation.reclamo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.text.SimpleDateFormat
import java.util.*

data class TipoReclamoSimple(val id: Int, val nombre: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReclamoScreen(
    viewModel: ReclamoViewModel = hiltViewModel(),
    reclamoId: Int = 0,
    onNavigateBack: () -> Unit,
    tiposReclamo: List<TipoReclamoSimple> = listOf(
        TipoReclamoSimple(1, "Producto defectuoso"),
        TipoReclamoSimple(2, "Servicio deficiente"),
        TipoReclamoSimple(3, "Entrega tardía"),
        TipoReclamoSimple(4, "Producto incorrecto"),
        TipoReclamoSimple(5, "Otro")
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    var showTipoDialog by remember { mutableStateOf(false) }
    var nuevaEvidencia by remember { mutableStateOf("") }
    var showAddEvidenciaDialog by remember { mutableStateOf(false) }
    var fechaManual by remember { mutableStateOf("") }

    LaunchedEffect(uiState.fechaIncidente) {
        fechaManual = uiState.fechaIncidente
    }

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
                            onClick = { showTipoDialog = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (uiState.tipoReclamo.isNotEmpty())
                                    uiState.tipoReclamo
                                else
                                    "Seleccionar tipo de reclamo"
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
                            text = "Fecha del Incidente",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = fechaManual,
                            onValueChange = {
                                fechaManual = it
                                // Actualizar el ViewModel cuando el usuario termina de editar
                                viewModel.setFechaIncidente(it)
                            },
                            label = { Text("YYYY-MM-DD") },
                            modifier = Modifier.fillMaxWidth(),
                            leadingIcon = {
                                Icon(
                                    Icons.Default.DateRange,
                                    contentDescription = "Fecha"
                                )
                            },
                            placeholder = { Text("Ej: 2023-12-31") },
                            singleLine = true,
                            supportingText = {
                                if (fechaManual.isNotEmpty()) {
                                    Text(
                                        text = if (esFechaValida(fechaManual))
                                            "Fecha válida"
                                        else
                                            "Formato debe ser YYYY-MM-DD",
                                        color = if (esFechaValida(fechaManual))
                                            MaterialTheme.colorScheme.primary
                                        else
                                            MaterialTheme.colorScheme.error
                                    )
                                }
                            },
                            isError = fechaManual.isNotEmpty() && !esFechaValida(fechaManual)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Ingrese la fecha en formato YYYY-MM-DD",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            item {
                Column {
                    OutlinedTextField(
                        value = uiState.descripcion,
                        onValueChange = { viewModel.setDescripcion(it) },
                        label = { Text("Descripción del Reclamo") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Default.Create, contentDescription = null) },
                        minLines = 3,
                        maxLines = 5
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${uiState.descripcion.length} caracteres (mínimo 10)",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (uiState.descripcion.length >= 10)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.error
                    )
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
                            IconButton(onClick = { showAddEvidenciaDialog = true }) {
                                Icon(Icons.Default.Add, contentDescription = "Agregar evidencia")
                            }
                        }

                        if (uiState.evidencias.isEmpty()) {
                            Text(
                                text = "No hay evidencias agregadas",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {
                            Column {
                                uiState.evidencias.forEachIndexed { index, evidencia ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.surface
                                        )
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                Icons.Default.Send,
                                                contentDescription = null,
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = evidencia,
                                                modifier = Modifier.weight(1f),
                                                style = MaterialTheme.typography.bodySmall,
                                                maxLines = 1
                                            )
                                            IconButton(
                                                onClick = { viewModel.removeEvidencia(index) },
                                                modifier = Modifier.size(24.dp)
                                            ) {
                                                Icon(
                                                    Icons.Default.Delete,
                                                    contentDescription = "Eliminar",
                                                    tint = MaterialTheme.colorScheme.error
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            viewModel.clearForm()
                            fechaManual = ""
                            onNavigateBack()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar")
                    }

                    Button(
                        onClick = {
                            // Primero asegurar que la fecha se guardó del campo manual
                            if (fechaManual.isNotEmpty() && esFechaValida(fechaManual)) {
                                viewModel.setFechaIncidente(fechaManual)
                            }

                            if (uiState.reclamoId == 0) {
                                viewModel.createReclamo()
                            } else {
                                viewModel.updateReclamo()
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = !uiState.isCreating && !uiState.isUpdating &&
                                uiState.descripcion.length >= 10 &&
                                uiState.tipoReclamoId > 0 &&
                                fechaManual.isNotEmpty() &&
                                esFechaValida(fechaManual)
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

    if (showTipoDialog) {
        AlertDialog(
            onDismissRequest = { showTipoDialog = false },
            title = { Text("Seleccionar Tipo de Reclamo") },
            text = {
                LazyColumn {
                    items(tiposReclamo.size) { index ->
                        val tipo = tiposReclamo[index]
                        TextButton(
                            onClick = {
                                viewModel.setTipoReclamo(tipo.id, tipo.nombre)
                                showTipoDialog = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = tipo.nombre)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showTipoDialog = false }) {
                    Text("Cerrar")
                }
            }
        )
    }

    // Diálogo para agregar evidencia
    if (showAddEvidenciaDialog) {
        AlertDialog(
            onDismissRequest = { showAddEvidenciaDialog = false },
            title = { Text("Agregar Evidencia") },
            text = {
                Column {
                    OutlinedTextField(
                        value = nuevaEvidencia,
                        onValueChange = { nuevaEvidencia = it },
                        label = { Text("URL de la evidencia") },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("https://example.com/evidencia.jpg") }
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (nuevaEvidencia.isNotBlank()) {
                            viewModel.addEvidencia(nuevaEvidencia)
                            nuevaEvidencia = ""
                            showAddEvidenciaDialog = false
                        }
                    }
                ) {
                    Text("Agregar")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showAddEvidenciaDialog = false
                    nuevaEvidencia = ""
                }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

fun esFechaValida(fecha: String): Boolean {
    return try {
        if (!Regex("^\\d{4}-\\d{2}-\\d{2}\$").matches(fecha)) {
            return false
        }

        val partes = fecha.split("-")
        if (partes.size != 3) return false

        val anio = partes[0].toInt()
        val mes = partes[1].toInt()
        val dia = partes[2].toInt()

        if (mes < 1 || mes > 12) return false
        if (dia < 1 || dia > 31) return false
        if (anio < 1900 || anio > 2100) return false

        when (mes) {
            2 -> {
                val esBisiesto = (anio % 4 == 0 && anio % 100 != 0) || (anio % 400 == 0)
                return if (esBisiesto) dia <= 29 else dia <= 28
            }
            4, 6, 9, 11 -> return dia <= 30
            else -> return dia <= 31
        }
    } catch (e: Exception) {
        false
    }
}