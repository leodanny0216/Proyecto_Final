package edu.ucne.proyecto_final.presentation.compra

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
fun CompraScreen(
    viewModel: CompraViewModel = hiltViewModel(),
    proveedores: List<ProveedorDto> = emptyList(),
    compraId: Int? = null,
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val greenColor = Color(0xFF4CAF50)

    // Auto limpiar mensaje éxito
    LaunchedEffect(uiState.successMessage) {
        if (uiState.successMessage != null) {
            delay(2500)
            viewModel.clearMessages()
        }
    }

    // ================== DIALOGO PROVEEDOR ==================
    if (uiState.showProveedorDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.hideProveedorSelector() },
            title = { Text("Seleccionar Proveedor", fontWeight = FontWeight.Bold, color = greenColor) },
            text = {
                LazyColumn {
                    items(proveedores) { proveedor ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    viewModel.setProveedorId(proveedor.proveedorId)
                                    viewModel.hideProveedorSelector()
                                },
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(proveedor.nombre, fontWeight = FontWeight.Bold, color = greenColor)
                                Text(proveedor.email)
                                Text(proveedor.telefono)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { viewModel.hideProveedorSelector() }) {
                    Text("Cerrar", color = greenColor)
                }
            }
        )
    }

    // ================== UI PRINCIPAL ==================
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar Compra", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = greenColor)
            )
        },
        floatingActionButton = {
            if (uiState.detallesTemporales.isNotEmpty() && uiState.proveedorId > 0) {
                FloatingActionButton(
                    onClick = { viewModel.createCompra() },
                    containerColor = greenColor
                ) {
                    if (uiState.isCreating) {
                        CircularProgressIndicator(strokeWidth = 2.dp, color = Color.White)
                    } else {
                        Icon(Icons.Default.Check, contentDescription = "Guardar", tint = Color.White)
                    }
                }
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
        ) {

            // ---------- ERRORES ----------
            uiState.errorMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(10.dp))
            }

            // ---------- ÉXITO ----------
            uiState.successMessage?.let {
                Text(it, color = greenColor, fontWeight = FontWeight.Medium)
                Spacer(Modifier.height(10.dp))
            }

            // ---------- PROVEEDOR ----------
            OutlinedButton(
                onClick = { viewModel.showProveedorSelector(proveedores) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = greenColor)
            ) {
                Icon(Icons.Default.Face, contentDescription = null, tint = greenColor)
                Spacer(Modifier.width(8.dp))
                Text(
                    if (uiState.proveedorId > 0)
                        "Proveedor seleccionado: ${uiState.proveedorId}"
                    else "Seleccionar proveedor"
                )
            }

            Spacer(Modifier.height(16.dp))

            // ---------- FECHA ----------
            OutlinedTextField(
                value = uiState.fecha,
                onValueChange = viewModel::setFecha,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Fecha (YYYY-MM-DD)") },
                leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null, tint = greenColor) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = greenColor,
                    unfocusedBorderColor = greenColor.copy(alpha = 0.5f),
                    cursorColor = greenColor,
                    focusedLabelColor = greenColor
                )
            )

            Spacer(Modifier.height(20.dp))

            Text("Agregar Detalle", fontWeight = FontWeight.Bold, color = greenColor)

            // ---------- FORM DETALLE ----------
            OutlinedTextField(
                value = uiState.detalleActual.articulo,
                onValueChange = viewModel::setDetalleArticulo,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Artículo") }
            )
            Spacer(Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = uiState.detalleActual.cantidad.takeIf { it > 0 }?.toString() ?: "",
                    onValueChange = { viewModel.setDetalleCantidad(it.toIntOrNull() ?: 0) },
                    modifier = Modifier.weight(1f),
                    label = { Text("Cantidad") }
                )
                OutlinedTextField(
                    value = uiState.detalleActual.precioUnitario.takeIf { it > 0 }?.toString() ?: "",
                    onValueChange = { viewModel.setDetallePrecioUnitario(it.toDoubleOrNull() ?: 0.0) },
                    modifier = Modifier.weight(1f),
                    label = { Text("Precio") }
                )
            }

            Spacer(Modifier.height(8.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { viewModel.addDetalle() },
                enabled = uiState.detalleActual.articulo.isNotBlank() &&
                        uiState.detalleActual.cantidad > 0 &&
                        uiState.detalleActual.precioUnitario > 0,
                colors = ButtonDefaults.buttonColors(containerColor = greenColor)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                Spacer(Modifier.width(6.dp))
                Text("Agregar Detalle", color = Color.White)
            }

            Spacer(Modifier.height(20.dp))

            // ---------- LISTA DE DETALLES ----------
            if (uiState.detallesTemporales.isNotEmpty()) {
                Text(
                    "Detalles (${uiState.detallesTemporales.size})",
                    fontWeight = FontWeight.Bold,
                    color = greenColor
                )
                Spacer(Modifier.height(10.dp))

                LazyColumn {
                    items(uiState.detallesTemporales) { det ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(det.articulo, fontWeight = FontWeight.Bold, color = greenColor)
                                    Text("${det.cantidad} x ${det.precioUnitario}")
                                    Text("Subtotal: ${det.subtotal}")
                                }
                                IconButton(onClick = {
                                    val index = uiState.detallesTemporales.indexOf(det)
                                    viewModel.removeDetalleTemporal(index)
                                }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))

                Text(
                    "TOTAL: $${String.format("%.2f", uiState.total)}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = greenColor
                )
            }
        }
    }
}

// ================== PREVIEW ==================
@Preview(showBackground = true)
@Composable
fun CompraScreenPreview() {
    val fakeProveedores = listOf(
        ProveedorDto(proveedorId = 1, nombre = "Proveedor A", email = "a@correo.com", telefono = "123456789"),
        ProveedorDto(proveedorId = 2, nombre = "Proveedor B", email = "b@correo.com", telefono = "987654321")
    )

    CompraScreen(
        proveedores = fakeProveedores,
        onNavigateBack = {}
    )
}
