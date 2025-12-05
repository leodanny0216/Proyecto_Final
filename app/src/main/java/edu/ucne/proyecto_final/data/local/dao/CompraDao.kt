package edu.ucne.proyecto_final.data.local.dao

import androidx.room.*
import edu.ucne.proyecto_final.data.local.entity.CompraEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CompraDao {

    @Upsert
    suspend fun save(entity: CompraEntity)

    @Query("SELECT * FROM compras WHERE compraId = :id LIMIT 1")
    suspend fun find(id: Int): CompraEntity?

    @Delete
    suspend fun delete(entity: CompraEntity)

    @Query("SELECT * FROM compras WHERE isDeleted = 0 ORDER BY fecha DESC")
    fun getAll(): Flow<List<CompraEntity>>

    // Función para guardar una lista completa
    suspend fun saveAll(entities: List<CompraEntity>) {
        entities.forEach { save(it) }
    }
}
