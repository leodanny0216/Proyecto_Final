package edu.ucne.proyecto_final.presentation.compra

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import edu.ucne.proyecto_final.data.remote.dto.CompraDetalleDto
import edu.ucne.proyecto_final.data.remote.dto.CompraDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompraListScreen(
    viewModel: CompraViewModel = hiltViewModel(),
    onNavigateToCompra: () -> Unit = {},
    onEditCompra: (Int) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf<CompraDto?>(null) }
    val greenColor = Color(0xFF4CAF50)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Compras", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = greenColor
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToCompra,
                containerColor = greenColor
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva Compra", tint = Color.White)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            // Barra de búsqueda
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Buscar por artículo o ID...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = greenColor) },
                trailingIcon = {
                    if (uiState.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.setSearchQuery("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Limpiar")
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
            uiState.errorCompras?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Lista de compras
            when {
                uiState.isLoadingCompras -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = greenColor)
                    }
                }

                uiState.comprasFiltradas.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = if (uiState.searchQuery.isBlank())
                                "No hay compras registradas"
                            else
                                "No se encontraron compras",
                            color = Color.Gray
                        )
                    }
                }

                else -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(uiState.comprasFiltradas) { compra ->
                            val detalles: List<CompraDetalleDto> =
                                uiState.detalles.filter { it.compraId == compra.compraId }

                            CompraItem(
                                compra = compra,
                                detalles = detalles,
                                onEdit = { onEditCompra(compra.compraId) },
                                onDelete = { showDeleteDialog = compra },
                                greenColor = greenColor
                            )
                        }
                    }
                }
            }
        }
    }

    // Diálogo de confirmación de eliminación
    showDeleteDialog?.let { compra ->
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Está seguro de que desea eliminar la compra ID ${compra.compraId}?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteCompra(compra.compraId)
                    showDeleteDialog = null
                }) { Text("Eliminar", color = greenColor) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = null }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
fun CompraItem(
    compra: CompraDto,
    detalles: List<CompraDetalleDto>,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    greenColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onEdit),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Compra ID: ${compra.compraId}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = greenColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text("Proveedor: ${compra.proveedorNombre}")
            Spacer(modifier = Modifier.height(4.dp))
            Text("Fecha: ${compra.fecha}")
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
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

@Preview(showBackground = true)
@Composable
fun CompraListScreenPreview() {
    // Preview con datos ficticios
    val fakeCompras = listOf(
        CompraDto(compraId = 1, proveedorNombre = "Proveedor A", fecha = "2025-12-04"),
        CompraDto(compraId = 2, proveedorNombre = "Proveedor B", fecha = "2025-12-03")
    )
    val fakeDetalles = listOf(
        CompraDetalleDto(compraId = 1, fecha = "2025-12-04"),
        CompraDetalleDto(compraId = 2, fecha = "2025-12-03")
    )
    val fakeUiState = object {
        val searchQuery = ""
        val errorCompras: String? = null
        val isLoadingCompras = false
        val comprasFiltradas = fakeCompras
        val detalles = fakeDetalles
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        fakeUiState.comprasFiltradas.forEach { compra ->
            CompraItem(
                compra = compra,
                detalles = fakeUiState.detalles.filter { it.compraId == compra.compraId },
                onEdit = {},
                onDelete = {},
                greenColor = Color(0xFF4CAF50)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
