// file: data/local/entity/ClienteEntity.kt
package edu.ucne.proyecto_final.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clientes")
data class ClienteEntity(
    @PrimaryKey(autoGenerate = false)
    val clienteId: Int,
    val nombre: String,
    val apellido: String,
    val numeroTelefono: String,
    val correoElectronico: String,
    val direccion: String,
    val updatedAt: Long = System.currentTimeMillis(),
    val isDeleted: Boolean = false,
    val syncStatus: SyncStatus = SyncStatus.SYNCED
)
