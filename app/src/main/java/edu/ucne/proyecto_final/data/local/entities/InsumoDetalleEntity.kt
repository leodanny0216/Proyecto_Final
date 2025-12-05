// file: data/local/entity/InsumoDetalleEntity.kt
package edu.ucne.proyecto_final.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "insumo_detalles")
data class InsumoDetalleEntity(
    @PrimaryKey(autoGenerate = false)
    val insumoDetalleId: Int,
    val insumoId: Int,
    val nombre: String,
    val descripcion: String,
    val cantidad: Int,
    val precioUnidad: Int,
    val fechaAdquisicion: String,
    val proveedorId: Int,
    val proveedorNombre: String,
    val categoriaId: Int,
    val categoriaNombre: String,
    val valorTotal: Int,
    val updatedAt: Long = System.currentTimeMillis(),
    val isDeleted: Boolean = false,
    val syncStatus: SyncStatus = SyncStatus.SYNCED
)
