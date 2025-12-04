
package edu.ucne.proyecto_final.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReclamoDto(
    val reclamoId: Int = 0,
    val tipoReclamoId: Int = 0,
    val tipoReclamo: String = "",
    val fechaIncidente: String = "",
    val descripcion: String = "",
    val evidencias: List<String> = emptyList()
)