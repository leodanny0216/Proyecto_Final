package edu.ucne.proyecto_final.presentation.reclamo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.ucne.proyecto_final.data.remote.dto.ReclamoDto
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReclamoListScreen(
    viewModel: ReclamoViewModel = hiltViewModel(),
    onNavigateToReclamo: () -> Unit = {},
    onEditReclamo: (Int) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf<ReclamoDto?>(null) }
    val greenColor = Color(0xFF4CAF50)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Reclamos", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = greenColor)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToReclamo,
                containerColor = greenColor
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo Reclamo", tint = Color.White)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .background(Color(0xFFF5F5F5))
        ) {
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Buscar por descripción, tipo o fecha...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = greenColor) },
                trailingIcon = {
                    if (uiState.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.setSearchQuery("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Limpiar", tint = greenColor)
                        }
                    }
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = greenColor,
                    unfocusedBorderColor = greenColor.copy(alpha = 0.5f),
                    cursorColor = greenColor
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Mensaje de error
            uiState.errorReclamos?.let { error ->
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
                Spacer(modifier = Modifier.height(16.dp))
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
                Spacer(modifier = Modifier.height(16.dp))
                LaunchedEffect(Unit) {
                    delay(3000)
                    viewModel.clearMessages()
                }
            }

            // Lista de reclamos
            when {
                uiState.isLoadingReclamos -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = greenColor)
                    }
                }
                uiState.reclamosFiltrados.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Warning, contentDescription = null, modifier = Modifier.size(64.dp), tint = greenColor)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = if (uiState.searchQuery.isBlank())
                                    "No hay reclamos registrados"
                                else
                                    "No se encontraron reclamos",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.reclamosFiltrados) { reclamo ->
                            ReclamoItem(
                                reclamo = reclamo,
                                onEdit = { onEditReclamo(reclamo.reclamoId) },
                                onDelete = { showDeleteDialog = reclamo }
                            )
                        }
                    }
                }
            }
        }
    }

    // Diálogo de confirmación de eliminación
    showDeleteDialog?.let { reclamo ->
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Está seguro de que desea eliminar el reclamo?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteReclamo(reclamo.reclamoId)
                        showDeleteDialog = null
                    }
                ) { Text("Eliminar", color = greenColor) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = null }) { Text("Cancelar", color = greenColor) }
            }
        )
    }
}

@Composable
fun ReclamoItem(
    reclamo: ReclamoDto,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val greenColor = Color(0xFF4CAF50)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onEdit),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Tipo: ${reclamo.tipoReclamo}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Fecha: ${reclamo.fechaIncidente}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = reclamo.descripcion,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    if (reclamo.evidencias.isNotEmpty()) {
                        Text(
                            text = "Evidencias: ${reclamo.evidencias.size}",
                            style = MaterialTheme.typography.bodySmall,
                            color = greenColor
                        )
                    }
                }
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar", tint = greenColor)
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                    }
                }
            }
        }
    }
}

// ================== PREVIEW ==================
@Preview(showBackground = true)
@Composable
fun ReclamoListScreenPreview() {
    ReclamoListScreen()
}
