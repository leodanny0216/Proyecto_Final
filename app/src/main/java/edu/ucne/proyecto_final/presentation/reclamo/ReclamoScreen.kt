package edu.ucne.proyecto_final.presentation.reclamo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

data class TipoReclamoSimple(val id: Int, val nombre: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReclamoScreen(
    viewModel: ReclamoViewModel = hiltViewModel(),
    reclamoId: Int = 0,
    onNavigateBack: () -> Unit = {},
    tiposReclamo: List<TipoReclamoSimple> = listOf(
        TipoReclamoSimple(1, "Producto defectuoso"),
        TipoReclamoSimple(2, "Servicio deficiente"),
        TipoReclamoSimple(3, "Entrega tardía"),
        TipoReclamoSimple(4, "Producto incorrecto"),
        TipoReclamoSimple(5, "Otro")
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    val greenColor = Color(0xFF4CAF50)

    var showTipoDialog by remember { mutableStateOf(false) }
    var nuevaEvidencia by remember { mutableStateOf("") }
    var showAddEvidenciaDialog by remember { mutableStateOf(false) }
    var fechaManual by remember { mutableStateOf(uiState.fechaIncidente) }

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
                    Text(
                        if (uiState.reclamoId == 0) "Nuevo Reclamo" else "Editar Reclamo",
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = greenColor)
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
            // Mensaje de error
            item {
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
            }

            // Mensaje de éxito
            item {
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
                        kotlinx.coroutines.delay(2000)
                        onNavigateBack()
                    }
                }
            }

            // Selección de tipo de reclamo
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)),
                    shape = RoundedCornerShape(12.dp)
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
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = greenColor
                            )
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

            // Fecha del incidente
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)),
                    shape = RoundedCornerShape(12.dp)
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
                                viewModel.setFechaIncidente(it)
                            },
                            label = { Text("YYYY-MM-DD") },
                            leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = "Fecha") },
                            placeholder = { Text("Ej: 2023-12-31") },
                            singleLine = true,
                            supportingText = {
                                if (fechaManual.isNotEmpty()) {
                                    Text(
                                        text = if (esFechaValida(fechaManual))
                                            "Fecha válida" else "Formato debe ser YYYY-MM-DD",
                                        color = if (esFechaValida(fechaManual)) greenColor else Color.Red
                                    )
                                }
                            },
                            isError = fechaManual.isNotEmpty() && !esFechaValida(fechaManual)

                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Ingrese la fecha en formato YYYY-MM-DD",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
            }

            // Descripción
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
                        color = if (uiState.descripcion.length >= 10) greenColor else Color.Red
                    )
                }
            }

            // Evidencias
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)),
                    shape = RoundedCornerShape(12.dp)
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
                                Icon(Icons.Default.Add, contentDescription = "Agregar evidencia", tint = greenColor)
                            }
                        }

                        if (uiState.evidencias.isEmpty()) {
                            Text(
                                text = "No hay evidencias agregadas",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        } else {
                            Column {
                                uiState.evidencias.forEachIndexed { index, evidencia ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color.White),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(20.dp), tint = greenColor)
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
                                                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Botones
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
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = greenColor)
                    ) {
                        Text("Cancelar")
                    }

                    Button(
                        onClick = {
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
                                esFechaValida(fechaManual),
                        colors = ButtonDefaults.buttonColors(containerColor = greenColor)
                    ) {
                        if (uiState.isCreating || uiState.isUpdating) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White
                            )
                        } else {
                            Text(if (uiState.reclamoId == 0) "Guardar" else "Actualizar", color = Color.White)
                        }
                    }
                }
            }
        }
    }

    // Diálogos
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
                            Text(text = tipo.nombre, color = greenColor)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showTipoDialog = false }) {
                    Text("Cerrar", color = greenColor)
                }
            }
        )
    }

    if (showAddEvidenciaDialog) {
        AlertDialog(
            onDismissRequest = { showAddEvidenciaDialog = false },
            title = { Text("Agregar Evidencia") },
            text = {
                OutlinedTextField(
                    value = nuevaEvidencia,
                    onValueChange = { nuevaEvidencia = it },
                    label = { Text("URL de la evidencia") },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("https://example.com/evidencia.jpg") },

                )
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
                ) { Text("Agregar", color = greenColor) }
            },
            dismissButton = {
                TextButton(onClick = {
                    showAddEvidenciaDialog = false
                    nuevaEvidencia = ""
                }) { Text("Cancelar", color = greenColor) }
            }
        )
    }
}

fun esFechaValida(fecha: String): Boolean {
    return try {
        if (!Regex("^\\d{4}-\\d{2}-\\d{2}\$").matches(fecha)) return false
        val partes = fecha.split("-")
        val anio = partes[0].toInt()
        val mes = partes[1].toInt()
        val dia = partes[2].toInt()
        if (mes !in 1..12) return false
        if (dia !in 1..31) return false
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

// ================== PREVIEW ==================
@Preview(showBackground = true)
@Composable
fun ReclamoScreenPreview() {
    ReclamoScreen()
}
