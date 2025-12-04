package edu.ucne.proyecto_final.data.remote.dto

import kotlinx.serialization.Serializable
@Serializable
data class CompraDetalleDto(
    val compraDetalleId: Int = 0,
    val compraId: Int = 0,
    val fecha: String = "",
    val total: Double = 0.0,
    val fechaFormateada: String = "",
    val totalFormateado: String = "",
    val compra: String = ""
)