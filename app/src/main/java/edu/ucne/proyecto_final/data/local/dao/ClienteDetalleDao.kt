package edu.ucne.proyecto_final.data.local.dao

import androidx.room.*
import edu.ucne.proyecto_final.data.local.entity.ClienteDetalleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClienteDetalleDao {
    @Upsert
    suspend fun save(entity: ClienteDetalleEntity)

    @Upsert
    suspend fun saveAll(entities: List<ClienteDetalleEntity>)  // <-- esto es nuevo

    @Query("SELECT * FROM cliente_detalles WHERE clienteDetalleId = :id LIMIT 1")
    suspend fun find(id: Int): ClienteDetalleEntity?

    @Delete
    suspend fun delete(entity: ClienteDetalleEntity)

    @Query("SELECT * FROM cliente_detalles WHERE clienteId = :clienteId AND isDeleted = 0")
    fun getByCliente(clienteId: Int): Flow<List<ClienteDetalleEntity>>
}

