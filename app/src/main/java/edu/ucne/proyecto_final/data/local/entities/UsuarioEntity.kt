// file: data/local/entity/UsuarioEntity.kt
package edu.ucne.proyecto_final.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = false)
    val usuarioId: Int,
    val userName: String,
    val password: String,
    val updatedAt: Long = System.currentTimeMillis(),
    val isDeleted: Boolean = false,
    val syncStatus: SyncStatus = SyncStatus.SYNCED
)
