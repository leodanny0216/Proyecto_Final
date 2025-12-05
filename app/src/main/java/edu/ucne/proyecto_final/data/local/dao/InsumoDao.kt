package edu.ucne.proyecto_final.data.local.dao

import androidx.room.*
import edu.ucne.proyecto_final.data.local.entity.InsumoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InsumoDao {

    @Upsert
    suspend fun save(entity: InsumoEntity)

    suspend fun saveAll(entities: List<InsumoEntity>) {
        entities.forEach { save(it) }
    }

    @Query("SELECT * FROM insumos WHERE insumoId = :id LIMIT 1")
    suspend fun find(id: Int): InsumoEntity?

    @Delete
    suspend fun delete(entity: InsumoEntity)

    @Query("SELECT * FROM insumos WHERE isDeleted = 0 ORDER BY nombre")
    fun getAll(): Flow<List<InsumoEntity>>
}
