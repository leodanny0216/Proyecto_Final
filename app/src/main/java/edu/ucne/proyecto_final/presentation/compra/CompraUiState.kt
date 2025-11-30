package edu.ucne.proyecto_final.presentation.compra

import edu.ucne.proyecto_final.data.remote.dto.CompraDto
import edu.ucne.proyecto_final.dto.remote.ProveedorDto

data class CompraUiState(
    val compras: List<CompraDto> = emptyList(),
    val comprasFiltradas: List<CompraDto> = emptyList(),
    val isLoadingCompras: Boolean = false,
    val errorCompras: String? = null,

    val compraId: Int = 0,
    val proveedor: ProveedorDto? = null,
    val fecha: String = "",
    val articulo: String = "",
    val cantidad: Int = 0,
    val total: Int = 0,

    val isCreating: Boolean = false,
    val isUpdating: Boolean = false,
    val isDeleting: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null,
    val compraSeleccionada: CompraDto? = null,

    val searchQuery: String = ""
)