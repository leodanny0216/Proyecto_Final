// file: data/local/entity/CompraDetalleEntity.kt
package edu.ucne.proyecto_final.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "compra_detalles")
data class CompraDetalleEntity(
    @PrimaryKey(autoGenerate = false)
    val compraDetalleId: Int,
    val compraId: Int,
    val fecha: String,
    val total: Double,
    val updatedAt: Long = System.currentTimeMillis(),
    val isDeleted: Boolean = false,
    val syncStatus: SyncStatus = SyncStatus.SYNCED
)
