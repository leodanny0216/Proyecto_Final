package edu.ucne.proyecto_final.data.remote.dto

import edu.ucne.proyecto_final.dto.remote.ProveedorDto
import kotlinx.serialization.Serializable

@Serializable
data class CompraDetalleDto(
    val compraDetalleId: Int = 0,
    val compraId: Int = 0,
    val fecha: String = "",
    val total: Int = 0,
    val compra: CompraDto? = null
)
