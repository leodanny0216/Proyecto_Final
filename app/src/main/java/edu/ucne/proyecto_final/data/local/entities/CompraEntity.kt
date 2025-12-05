// file: data/local/entity/CompraEntity.kt
package edu.ucne.proyecto_final.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "compras")
data class CompraEntity(
    @PrimaryKey(autoGenerate = false)
    val compraId: Int,
    val articulo: String,
    val cantidad: Int,
    val proveedorId: Int,
    val proveedorNombre: String,
    val fecha: String,
    val updatedAt: Long = System.currentTimeMillis(),
    val isDeleted: Boolean = false,
    val syncStatus: SyncStatus = SyncStatus.SYNCED
)
