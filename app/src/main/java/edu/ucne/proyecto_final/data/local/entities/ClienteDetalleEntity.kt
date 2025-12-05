// file: data/local/entity/ClienteDetalleEntity.kt
package edu.ucne.proyecto_final.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cliente_detalles")
data class ClienteDetalleEntity(
    @PrimaryKey(autoGenerate = false)
    val clienteDetalleId: Int,
    val clienteId: Int,
    val nombreCompleto: String,
    val numeroTelefono: String,
    val direccionCompleta: String,
    val notasAdicionales: String = "",
    val updatedAt: Long = System.currentTimeMillis(),
    val isDeleted: Boolean = false,
    val syncStatus: SyncStatus = SyncStatus.SYNCED
)
