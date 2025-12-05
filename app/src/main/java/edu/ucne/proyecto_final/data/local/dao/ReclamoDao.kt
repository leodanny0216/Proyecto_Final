package edu.ucne.proyecto_final.data.local.dao

import androidx.room.*
import edu.ucne.proyecto_final.data.local.entity.ReclamoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReclamoDao {

    @Upsert
    suspend fun save(entity: ReclamoEntity)

    suspend fun saveAll(entities: List<ReclamoEntity>) {
        entities.forEach { save(it) }
    }

    @Query("SELECT * FROM reclamos WHERE reclamoId = :id LIMIT 1")
    suspend fun find(id: Int): ReclamoEntity?

    @Delete
    suspend fun delete(entity: ReclamoEntity)

    @Query("SELECT * FROM reclamos WHERE isDeleted = 0 ORDER BY fechaIncidente DESC")
    fun getAll(): Flow<List<ReclamoEntity>>
}
