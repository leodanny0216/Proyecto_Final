package edu.ucne.proyecto_final.presentation.compra

import edu.ucne.proyecto_final.data.remote.dto.CompraDetalleDto
import edu.ucne.proyecto_final.data.remote.dto.CompraDto

data class CompraUiState(
    val compraId: Int = 0,

    val proveedorId: Int = 0,
    val fecha: String = "",

    val compras: List<CompraDto> = emptyList(),
    val comprasFiltradas: List<CompraDto> = emptyList(),

    val detalles: List<CompraDetalleDto> = emptyList(),
    val detallesTemporales: List<DetalleCompraTemporal> = emptyList(),

    // Campo para detalle actual
    val detalleActual: DetalleCompraTemporal = DetalleCompraTemporal(),

    // Dialogo proveedor
    val showProveedorDialog: Boolean = false,

    val searchQuery: String = "",

    // Loading flags
    val isLoadingCompras: Boolean = false,
    val isLoadingDetalles: Boolean = false,
    val isCreating: Boolean = false,
    val isDeleting: Boolean = false,

    // Messages
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val errorCompras: String? = null,
    val errorDetalles: String? = null
) {
    val total: Double get() = detallesTemporales.sumOf { it.subtotal }
}



