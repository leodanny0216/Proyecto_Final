package edu.ucne.proyecto_final.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class InsumoDetalleDto(
    val insumoDetalleId: Int = 0,
    val insumoId: Int = 0,
    val nombre: String = "",
    val descripcion: String = "",
    val cantidad: Int = 0,
    val precioUnidad: Int = 0,
    val fechaAdquisicion: String = "",
    val proveedorId: Int = 0,
    val proveedorNombre: String = "",
    val categoriaId: Int = 0,
    val categoriaNombre: String = "",
    val fechaAdquisicionFormateada: String = "",
    val valorTotal: Int = 0,
    val valorTotalFormateado: String = "",
    val insumo: InsumoDto? = null
)