package edu.ucne.proyecto_final.presentation.insumo

import edu.ucne.proyecto_final.data.remote.dto.InsumoDto

data class InsumoUiState(
    val insumos: List<InsumoDto> = emptyList(),
    val insumosFiltrados: List<InsumoDto> = emptyList(),
    val isLoadingInsumos: Boolean = false,
    val errorInsumos: String? = null,

    val insumoId: Int = 0,
    val nombre: String = "",
    val categoriaId: Int = 0,
    val categoriaNombre: String = "",
    val proveedorId: Int = 0,
    val proveedorNombre: String = "",
    val stockInicial: Int = 0,

    val isCreating: Boolean = false,
    val isUpdating: Boolean = false,
    val isDeleting: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null,
    val insumoSeleccionado: InsumoDto? = null,

    val searchQuery: String = ""
)