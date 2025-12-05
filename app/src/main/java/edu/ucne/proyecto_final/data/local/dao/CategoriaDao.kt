package edu.ucne.proyecto_final.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.proyecto_final.data.local.entity.CategoriaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriaDao {

    @Upsert
    suspend fun upsert(entity: CategoriaEntity)

    @Upsert
    suspend fun upsertAll(entities: List<CategoriaEntity>)

    @Query("SELECT * FROM categorias WHERE categoriaId = :id LIMIT 1")
    suspend fun find(id: Int): CategoriaEntity?

    @Delete
    suspend fun delete(entity: CategoriaEntity)

    @Query("SELECT * FROM categorias WHERE isDeleted = 0 ORDER BY nombre")
    fun getAll(): Flow<List<CategoriaEntity>>
}
