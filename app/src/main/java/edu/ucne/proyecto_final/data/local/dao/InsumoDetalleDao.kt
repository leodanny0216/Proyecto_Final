package edu.ucne.proyecto_final.data.local.dao

import androidx.room.*
import edu.ucne.proyecto_final.data.local.entity.InsumoDetalleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InsumoDetalleDao {

    @Upsert
    suspend fun save(entity: InsumoDetalleEntity)

    @Query("SELECT * FROM insumo_detalles WHERE insumoDetalleId = :id LIMIT 1")
    suspend fun find(id: Int): InsumoDetalleEntity?

    @Delete
    suspend fun delete(entity: InsumoDetalleEntity)

    @Query("SELECT * FROM insumo_detalles WHERE insumoId = :insumoId AND isDeleted = 0")
    fun getByInsumo(insumoId: Int): Flow<List<InsumoDetalleEntity>>

    // Función para guardar una lista completa
    suspend fun saveAll(entities: List<InsumoDetalleEntity>) {
        entities.forEach { save(it) }
    }
}
