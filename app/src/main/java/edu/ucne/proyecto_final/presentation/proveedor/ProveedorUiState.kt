package edu.ucne.proyecto_final.presentation.proveedor

import edu.ucne.proyecto_final.dto.remote.ProveedorDto

data class ProveedorUiState(
    val proveedores: List<ProveedorDto> = emptyList(),
    val proveedoresFiltrados: List<ProveedorDto> = emptyList(),
    val isLoadingProveedores: Boolean = false,
    val errorProveedores: String? = null,

    val proveedorId: Int = 0,
    val nombre: String = "",
    val telefono: String = "",
    val email: String = "",
    val direccion: String = "",

    val isCreating: Boolean = false,
    val isUpdating: Boolean = false,
    val isDeleting: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null,
    val proveedorSeleccionado: ProveedorDto? = null,

    val searchQuery: String = ""
)