package edu.ucne.proyecto_final.data.local.dao

import androidx.room.*
import edu.ucne.proyecto_final.data.local.entity.ProveedorEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProveedorDao {

    @Upsert
    suspend fun save(entity: ProveedorEntity)

    suspend fun saveAll(entities: List<ProveedorEntity>) {
        entities.forEach { save(it) }
    }

    @Query("SELECT * FROM proveedores WHERE proveedorId = :id LIMIT 1")
    suspend fun find(id: Int): ProveedorEntity?

    @Delete
    suspend fun delete(entity: ProveedorEntity)

    @Query("SELECT * FROM proveedores WHERE isDeleted = 0 ORDER BY nombre")
    fun getAll(): Flow<List<ProveedorEntity>>
}
