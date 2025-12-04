package edu.ucne.proyecto_final.dto.remote

import kotlinx.serialization.Serializable

@Serializable
data class ProveedorDto(
    val proveedorId: Int = 0,
    val nombre: String = "",
    val telefono: String = "",
    val email: String = "",
    val direccion: String = ""
)

