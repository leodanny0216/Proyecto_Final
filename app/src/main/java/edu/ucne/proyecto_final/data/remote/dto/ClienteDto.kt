package edu.ucne.proyecto_final.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ClienteDto(
    val clienteId: Int = 0,
    val nombre: String = "",
    val apellido: String = "",
    val numeroTelefono: String = "",
    val correoElectronico: String = "",
    val direccion: String = "",
    val detalles: List<ClienteDetalleDto>? = null // ⬅️ NULLABLE para manejar null del API
)