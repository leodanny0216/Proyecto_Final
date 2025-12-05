// file: data/local/entity/InsumoEntity.kt
package edu.ucne.proyecto_final.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "insumos")
data class InsumoEntity(
    @PrimaryKey(autoGenerate = false)
    val insumoId: Int,
    val nombre: String,
    val categoriaId: Int,
    val categoriaNombre: String,
    val proveedorId: Int,
    val proveedorNombre: String,
    val stockInicial: Int,
    val updatedAt: Long = System.currentTimeMillis(),
    val isDeleted: Boolean = false,
    val syncStatus: SyncStatus = SyncStatus.SYNCED
)
