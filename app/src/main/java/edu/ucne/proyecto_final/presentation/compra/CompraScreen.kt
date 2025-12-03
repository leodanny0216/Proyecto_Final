package edu.ucne.proyecto_final.presentation.compra

import androidx.compose.foundation.clickable
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
import edu.ucne.proyecto_final.dto.remote.ProveedorDto
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompraScreen(
    viewModel: CompraViewModel = hiltViewModel(),
    proveedores: List<ProveedorDto>,
    compraId: Int? = null,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

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
            title = { Text("Seleccionar Proveedor") },
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
                                }
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(proveedor.nombre, fontWeight = FontWeight.Bold)
                                Text(proveedor.email)
                                Text(proveedor.telefono)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { viewModel.hideProveedorSelector() }) {
                    Text("Cerrar")
                }
            }
        )
    }

    // ================== UI PRINCIPAL ==================
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar Compra") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            if (uiState.detallesTemporales.isNotEmpty() && uiState.proveedorId > 0) {
                FloatingActionButton(
                    onClick = { viewModel.createCompra() }
                ) {
                    if (uiState.isCreating) {
                        CircularProgressIndicator(
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(Icons.Default.Check, contentDescription = "Guardar")
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
        ) {

            // ---------- ERRORES ----------
            uiState.errorMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(10.dp))
            }

            // ---------- ÉXITO ----------
            uiState.successMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.height(10.dp))
            }

            // ---------- PROVEEDOR ----------
            OutlinedButton(
                onClick = { viewModel.showProveedorSelector(proveedores) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Face, contentDescription = null)
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
                leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) }
            )

            Spacer(Modifier.height(20.dp))

            Text("Agregar Detalle", fontWeight = FontWeight.Bold)

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
                    onValueChange = {
                        viewModel.setDetalleCantidad(it.toIntOrNull() ?: 0)
                    },
                    modifier = Modifier.weight(1f),
                    label = { Text("Cantidad") }
                )
                OutlinedTextField(
                    value = uiState.detalleActual.precioUnitario.takeIf { it > 0 }?.toString()
                        ?: "",
                    onValueChange = {
                        viewModel.setDetallePrecioUnitario(it.toDoubleOrNull() ?: 0.0)
                    },
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
                        uiState.detalleActual.precioUnitario > 0
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text("Agregar Detalle")
            }

            Spacer(Modifier.height(20.dp))

            // ---------- LISTA DE DETALLES ----------
            if (uiState.detallesTemporales.isNotEmpty()) {
                Text(
                    "Detalles (${uiState.detallesTemporales.size})",
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(10.dp))

                LazyColumn {
                    items(uiState.detallesTemporales) { det ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(det.articulo, fontWeight = FontWeight.Bold)
                                    Text("${det.cantidad} x ${det.precioUnitario}")
                                    Text("Subtotal: ${det.subtotal}")
                                }
                                IconButton(onClick = {
                                    val index = uiState.detallesTemporales.indexOf(det)
                                    viewModel.removeDetalleTemporal(index)
                                }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))

                Text(
                    "TOTAL: $${String.format("%.2f", uiState.total)}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
