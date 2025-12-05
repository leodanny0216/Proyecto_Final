// file: data/local/dao/ClienteDao.kt
package edu.ucne.proyecto_final.data.local.dao

import androidx.room.*
import edu.ucne.proyecto_final.data.local.entity.ClienteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClienteDao {
    @Upsert
    suspend fun save(cliente: ClienteEntity)

    @Query("SELECT * FROM clientes WHERE clienteId = :id LIMIT 1")
    suspend fun find(id: Int): ClienteEntity?

    @Delete
    suspend fun delete(cliente: ClienteEntity)

    @Query("SELECT * FROM clientes WHERE isDeleted = 0 ORDER BY nombre")
    fun getAll(): Flow<List<ClienteEntity>>
}
