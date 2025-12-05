package edu.ucne.proyecto_final.presentation.insumo

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
import edu.ucne.proyecto_final.data.remote.dto.InsumoDto
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsumoListScreen(
    viewModel: InsumoViewModel = hiltViewModel(),
    onNavigateToInsumo: () -> Unit = {},
    onEditInsumo: (Int) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf<InsumoDto?>(null) }
    val greenColor = Color(0xFF4CAF50)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Insumos", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = greenColor)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToInsumo,
                containerColor = greenColor
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo Insumo", tint = Color.White)
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
                placeholder = { Text("Buscar por nombre, categoría o proveedor...") },
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
                    cursorColor = greenColor,
                    focusedLabelColor = greenColor
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Mensajes de error
            uiState.errorInsumos?.let { error ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = Color.Red)
                        Spacer(Modifier.width(8.dp))
                        Text(text = error, color = Color.Red)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Mensajes de éxito
            uiState.successMessage?.let { message ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
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
                LaunchedEffect(Unit) { delay(3000); viewModel.clearMessages() }
            }

            when {
                uiState.isLoadingInsumos -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = greenColor)
                    }
                }
                uiState.insumosFiltrados.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Build, contentDescription = null, modifier = Modifier.size(64.dp), tint = greenColor)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = if (uiState.searchQuery.isBlank()) "No hay insumos registrados" else "No se encontraron insumos",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
                else -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(uiState.insumosFiltrados) { insumo ->
                            InsumoItem(insumo = insumo, onEdit = { onEditInsumo(insumo.insumoId) }, onDelete = { showDeleteDialog = insumo }, greenColor)
                        }
                    }
                }
            }
        }
    }

    showDeleteDialog?.let { insumo ->
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Está seguro de que desea eliminar el insumo '${insumo.nombre}'?") },
            confirmButton = {
                TextButton(onClick = { viewModel.deleteInsumo(insumo.insumoId); showDeleteDialog = null }) {
                    Text("Eliminar", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = null }) {
                    Text("Cancelar", color = greenColor)
                }
            }
        )
    }
}

@Composable
fun InsumoItem(
    insumo: InsumoDto,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    greenColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onEdit),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(insumo.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = greenColor)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Categoría: ${insumo.categoriaNombre}", style = MaterialTheme.typography.bodyMedium, color = greenColor.copy(alpha = 0.8f))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Proveedor: ${insumo.proveedorNombre}", style = MaterialTheme.typography.bodyMedium, color = greenColor.copy(alpha = 0.8f))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Stock Inicial: ${insumo.stockInicial}",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (insumo.stockInicial > 0) greenColor else Color.Red
                    )
                }
                Row {
                    IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, contentDescription = "Editar", tint = greenColor) }
                    IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red) }
                }
            }
        }
    }
}


