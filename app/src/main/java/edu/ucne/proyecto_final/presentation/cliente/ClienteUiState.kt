package edu.ucne.proyecto_final.presentation.cliente

import edu.ucne.proyecto_final.data.remote.dto.ClienteDto
import edu.ucne.proyecto_final.data.remote.dto.ClienteDetalleDto

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
    val detalles: List<ClienteDetalleDto> = emptyList(),

    // Detalle temporal para agregar
    val detalleNombre: String = "",
    val detalleApellido: String = "",
    val detalleTelefono: String = "",
    val detalleCorreo: String = "",
    val detalleDireccion: String = "",
    val detalleNotas: String = "",
    val detalleCodigoCliente: String = "",

    val isCreating: Boolean = false,
    val isUpdating: Boolean = false,
    val isDeleting: Boolean = false,
    val isSavingDetalle: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null,
    val clienteSeleccionado: ClienteDto? = null,

    val searchQuery: String = "",
    val showDetalleDialog: Boolean = false,

    // Indica si el cliente ya está guardado en el servidor
    val clienteGuardado: Boolean = false
)