package edu.ucne.proyecto_final.presentation.proveedor

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
import edu.ucne.proyecto_final.dto.remote.ProveedorDto
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProveedorListScreen(
    viewModel: ProveedorViewModel = hiltViewModel(),
    onNavigateToProveedor: () -> Unit = {},
    onEditProveedor: (Int) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf<ProveedorDto?>(null) }
    val greenColor = Color(0xFF4CAF50)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Proveedores", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = greenColor)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToProveedor,
                containerColor = greenColor
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo Proveedor", tint = Color.White)
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
            // Barra de búsqueda
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Buscar por nombre, teléfono, email o dirección...") },
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

            // Mensajes de error o éxito
            uiState.errorProveedores?.let { error ->
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

            // Lista de proveedores
            when {
                uiState.isLoadingProveedores -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = greenColor)
                    }
                }
                uiState.proveedoresFiltrados.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.AccountBox, contentDescription = null, modifier = Modifier.size(64.dp), tint = greenColor)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = if (uiState.searchQuery.isBlank()) "No hay proveedores registrados" else "No se encontraron proveedores",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
                else -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(uiState.proveedoresFiltrados) { proveedor ->
                            ProveedorItem(
                                proveedor = proveedor,
                                onEdit = { onEditProveedor(proveedor.proveedorId) },
                                onDelete = { showDeleteDialog = proveedor }
                            )
                        }
                    }
                }
            }
        }
    }

    // Diálogo de confirmación de eliminación
    showDeleteDialog?.let { proveedor ->
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Está seguro de que desea eliminar el proveedor '${proveedor.nombre}'?") },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.deleteProveedor(proveedor.proveedorId); showDeleteDialog = null }
                ) { Text("Eliminar", color = Color.Red) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = null }) { Text("Cancelar", color = greenColor) }
            }
        )
    }
}

@Composable
fun ProveedorItem(
    proveedor: ProveedorDto,
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
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(proveedor.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = greenColor)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Gray)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(proveedor.telefono, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Email, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Gray)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(proveedor.email, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                    }
                    if (proveedor.direccion.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Gray)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(proveedor.direccion, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        }
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
fun ProveedorListScreenPreview() {
    val proveedores = listOf(
        ProveedorDto(1, "Proveedor A", "809-123-4567", "a@correo.com", "Calle 1"),
        ProveedorDto(2, "Proveedor B", "809-987-6543", "b@correo.com", "Calle 2")
    )
    ProveedorListScreen()
}
