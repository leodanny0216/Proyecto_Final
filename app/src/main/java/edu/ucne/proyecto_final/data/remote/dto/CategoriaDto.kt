package edu.ucne.proyecto_final.data.remote.dto

import kotlinx.serialization.Serializable
import java.util.Date
@Serializable
data class CategoriaDto(
    val categoriaId: Int = 0,
    val nombre: String = "",
    val descripcion: String = ""
)