package edu.ucne.proyecto_final.presentation.categoria

import edu.ucne.proyecto_final.data.remote.dto.CategoriaDto

data class CategoriaUiState(
    val categorias: List<CategoriaDto> = emptyList(),
    val isLoadingCategorias: Boolean = false,
    val errorCategorias: String? = null,
    val categoriaId: Int = 0,
    val nombre: String = "",
    val descripcion: String = "",
    val isCreating: Boolean = false,
    val isUpdating: Boolean = false,
    val isDeleting: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null,
    val categoriaSeleccionada: CategoriaDto? = null,
    val searchQuery: String = "",
    val categoriasFiltradas: List<CategoriaDto> = emptyList()
)
