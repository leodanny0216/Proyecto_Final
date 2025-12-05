package edu.ucne.proyecto_final.data.local.dao

import androidx.room.*
import edu.ucne.proyecto_final.data.local.entity.CompraDetalleEntity
import kotlinx.coroutines.flow.Flow
@Dao
interface CompraDetalleDao {
    @Upsert
    suspend fun save(entity: CompraDetalleEntity)

    @Upsert
    suspend fun saveAll(entities: List<CompraDetalleEntity>) {
        entities.forEach { save(it) }
    }

    @Query("SELECT * FROM compra_detalles WHERE compraDetalleId = :id LIMIT 1")
    suspend fun find(id: Int): CompraDetalleEntity?

    @Delete
    suspend fun delete(entity: CompraDetalleEntity)

    @Query("SELECT * FROM compra_detalles WHERE compraId = :compraId AND isDeleted = 0")
    fun getByCompra(compraId: Int): Flow<List<CompraDetalleEntity>>
}
