package edu.ucne.proyecto_final.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class InsumoDto(
    val insumoId: Int = 0,
    val nombre: String = "",
    val categoriaId: Int = 0,
    val categoriaNombre: String = "",
    val proveedorId: Int = 0,
    val proveedorNombre: String = "",
    val stockInicial: Int = 0,
    val detalles: List<InsumoDetalleDto>? = null // Hacerlo nullable
)