package edu.ucne.proyecto_final.data.local.dao

import androidx.room.*
import edu.ucne.proyecto_final.data.local.entity.UsuarioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {

    @Upsert
    suspend fun save(entity: UsuarioEntity)

    @Upsert
    suspend fun saveAll(entities: List<UsuarioEntity>)

    @Query("SELECT * FROM usuarios WHERE usuarioId = :id LIMIT 1")
    suspend fun find(id: Int): UsuarioEntity?

    @Delete
    suspend fun delete(entity: UsuarioEntity)

    @Query("SELECT * FROM usuarios WHERE isDeleted = 0 ORDER BY userName")
    fun getAll(): Flow<List<UsuarioEntity>>
}
