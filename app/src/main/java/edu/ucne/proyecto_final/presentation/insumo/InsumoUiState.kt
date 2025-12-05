package edu.ucne.proyecto_final.presentation.insumo

import edu.ucne.proyecto_final.data.remote.dto.InsumoDto
import edu.ucne.proyecto_final.data.remote.dto.InsumoDetalleDto

data class InsumoUiState(
    val insumoId: Int = 0,
    val nombre: String = "",
    val categoriaId: Int = 0,
    val categoriaNombre: String = "",
    val proveedorId: Int = 0,
    val proveedorNombre: String = "",
    val stockInicial: Int = 0,
    val detalles: List<InsumoDetalleDto> = emptyList(),
    val insumoSeleccionado: InsumoDto? = null,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val isCreating: Boolean = false,
    val isUpdating: Boolean = false,
    val isDeleting: Boolean = false,
    val insumos: List<InsumoDto> = emptyList(),
    val insumosFiltrados: List<InsumoDto> = emptyList(),
    val searchQuery: String = "",
    val isLoadingInsumos: Boolean = false,
    val errorInsumos: String? = null
)
