package edu.ucne.proyecto_final.data.remote.dto

import edu.ucne.proyecto_final.dto.remote.ProveedorDto
import kotlinx.serialization.Serializable
@Serializable
data class CompraDto(
    val compraId: Int = 0,
    val articulo: String = "",
    val cantidad: Int = 0,
    val proveedorId: Int = 0,
    val proveedorNombre: String = "",
    val fecha: String = "",
    val proveedor: ProveedorDto? = null,
    val detalles: List<CompraDetalleDto> = emptyList()
)

