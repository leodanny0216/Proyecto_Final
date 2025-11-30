package edu.ucne.proyecto_final.presentation.cliente

import edu.ucne.proyecto_final.data.remote.dto.ClienteDto

data class ClienteUiState(
    val clientes: List<ClienteDto> = emptyList(),
    val clientesFiltrados: List<ClienteDto> = emptyList(),
    val isLoadingClientes: Boolean = false,
    val errorClientes: String? = null,

    val clienteId: Int = 0,
    val nombre: String = "",
    val apellido: String = "",
    val numeroTelefono: String = "",
    val correoElectronico: String = "",
    val direccion: String = "",
    val detalles: List<String> = emptyList(),

    val isCreating: Boolean = false,
    val isUpdating: Boolean = false,
    val isDeleting: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null,
    val clienteSeleccionado: ClienteDto? = null,

    val searchQuery: String = ""
)
