package edu.ucne.proyecto_final.presentation.insumo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import kotlinx.coroutines.delay

data class CategoriaSimple(val id: Int, val nombre: String)
data class ProveedorSimple(val id: Int, val nombre: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsumoScreen(
    viewModel: InsumoViewModel = hiltViewModel(),
    insumoId: Int = 0,
    onNavigateBack: () -> Unit = {},
    categorias: List<CategoriaSimple> = emptyList(),
    proveedores: List<ProveedorSimple> = emptyList()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showCategoriaDialog by remember { mutableStateOf(false) }
    var showProveedorDialog by remember { mutableStateOf(false) }
    val greenColor = Color(0xFF4CAF50)

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
                    Text(
                        if (uiState.insumoId == 0) "Nuevo Insumo" else "Editar Insumo",
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
                .padding(16.dp)
                .background(Color(0xFFF5F5F5)),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ================== Mensajes ==================
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
                        delay(2000)
                        onNavigateBack()
                    }
                }
            }

            // ================== Campos principales ==================
            item {
                OutlinedTextField(
                    value = uiState.nombre,
                    onValueChange = { viewModel.setNombre(it) },
                    label = { Text("Nombre del Insumo") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Build, contentDescription = null, tint = greenColor) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = greenColor,
                        unfocusedBorderColor = greenColor.copy(alpha = 0.5f),
                        cursorColor = greenColor,
                        focusedLabelColor = greenColor
                    )
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Categoría",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = greenColor
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = { showCategoriaDialog = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = greenColor)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(if (uiState.categoriaNombre.isNotEmpty()) uiState.categoriaNombre else "Seleccionar categoría")
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Proveedor",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = greenColor
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = { showProveedorDialog = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.AccountBox, contentDescription = null, tint = greenColor)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(if (uiState.proveedorNombre.isNotEmpty()) uiState.proveedorNombre else "Seleccionar proveedor")
                        }
                    }
                }
            }

            item {
                OutlinedTextField(
                    value = if (uiState.stockInicial == 0) "" else uiState.stockInicial.toString(),
                    onValueChange = { viewModel.setStockInicial(it.toIntOrNull() ?: 0) },
                    label = { Text("Stock Inicial") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Info, contentDescription = null, tint = greenColor) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = greenColor,
                        unfocusedBorderColor = greenColor.copy(alpha = 0.5f),
                        cursorColor = greenColor,
                        focusedLabelColor = greenColor
                    )
                )
            }

            // ================== DETALLES ==================
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Detalles del Insumo",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = greenColor
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        uiState.detalles.forEach { detalle ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text("Nombre: ${detalle.nombre}", fontWeight = FontWeight.Bold)
                                        Text("Cantidad: ${detalle.cantidad}")
                                        Text("Precio Unidad: ${detalle.precioUnidad}")
                                    }
                                    IconButton(onClick = { viewModel.removeDetalle(detalle.insumoDetalleId) }) {
                                        Icon(Icons.Default.Warning, contentDescription = "Eliminar", tint = Color.Red)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                val nuevoDetalle = viewModel.createDetalleTemporal()
                                viewModel.addDetalle(nuevoDetalle)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Agregar Detalle")
                        }
                    }
                }
            }



            // ================== Botones Guardar/Cancelar ==================
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = { viewModel.clearForm(); onNavigateBack() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar", color = greenColor)
                    }
                    Button(
                        onClick = {
                            if (uiState.insumoId == 0) viewModel.createInsumo() else viewModel.updateInsumo()
                        },
                        modifier = Modifier.weight(1f),
                        enabled = !uiState.isCreating && !uiState.isUpdating,
                        colors = ButtonDefaults.buttonColors(containerColor = greenColor)
                    ) {
                        if (uiState.isCreating || uiState.isUpdating) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                        } else {
                            Text(if (uiState.insumoId == 0) "Guardar" else "Actualizar", color = Color.White)
                        }
                    }
                }
            }
        }
    }

    // ================== Dialogos ==================
    if (showCategoriaDialog) {
        AlertDialog(
            onDismissRequest = { showCategoriaDialog = false },
            title = { Text("Seleccionar Categoría") },
            text = {
                LazyColumn {
                    items(categorias.size) { index ->
                        val categoria = categorias[index]
                        TextButton(
                            onClick = { viewModel.setCategoria(categoria.id, categoria.nombre); showCategoriaDialog = false },
                            modifier = Modifier.fillMaxWidth()
                        ) { Text(categoria.nombre, modifier = Modifier.fillMaxWidth()) }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showCategoriaDialog = false }) { Text("Cerrar") }
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
                            onClick = { viewModel.setProveedor(proveedor.id, proveedor.nombre); showProveedorDialog = false },
                            modifier = Modifier.fillMaxWidth()
                        ) { Text(proveedor.nombre, modifier = Modifier.fillMaxWidth()) }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showProveedorDialog = false }) { Text("Cerrar") }
            }
        )
    }
}

// ================== Preview ==================
@Preview(showBackground = true)
@Composable
fun InsumoScreenPreview() {
    val categorias = listOf(CategoriaSimple(1, "Categoría A"), CategoriaSimple(2, "Categoría B"))
    val proveedores = listOf(ProveedorSimple(1, "Proveedor A"), ProveedorSimple(2, "Proveedor B"))
    InsumoScreen(
        categorias = categorias,
        proveedores = proveedores
    )
}
