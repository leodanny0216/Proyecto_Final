// file: data/local/entity/ReclamoEntity.kt
package edu.ucne.proyecto_final.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reclamos")
data class ReclamoEntity(
    @PrimaryKey(autoGenerate = false)
    val reclamoId: Int,
    val tipoReclamoId: Int,
    val tipoReclamo: String,
    val fechaIncidente: String,
    val descripcion: String,
    val updatedAt: Long = System.currentTimeMillis(),
    val isDeleted: Boolean = false,
    val syncStatus: SyncStatus = SyncStatus.SYNCED
)
