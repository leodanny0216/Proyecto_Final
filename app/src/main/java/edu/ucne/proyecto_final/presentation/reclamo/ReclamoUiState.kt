// package edu.ucne.proyecto_final.presentation.reclamo
package edu.ucne.proyecto_final.presentation.reclamo

import edu.ucne.proyecto_final.data.remote.dto.ReclamoDto

data class ReclamoUiState(
    val reclamos: List<ReclamoDto> = emptyList(),
    val reclamosFiltrados: List<ReclamoDto> = emptyList(),
    val isLoadingReclamos: Boolean = false,
    val errorReclamos: String? = null,

    val reclamoId: Int = 0,
    val tipoReclamoId: Int = 0,
    val tipoReclamo: String = "",
    val fechaIncidente: String = "",
    val descripcion: String = "",
    val evidencias: List<String> = emptyList(),

    val isCreating: Boolean = false,
    val isUpdating: Boolean = false,
    val isDeleting: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null,
    val reclamoSeleccionado: ReclamoDto? = null,

    val searchQuery: String = ""
)