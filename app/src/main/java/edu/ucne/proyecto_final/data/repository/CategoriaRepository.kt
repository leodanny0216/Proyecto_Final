package edu.ucne.proyecto_final.data.repository

import edu.ucne.proyecto_final.data.local.dao.CategoriaDao
import edu.ucne.proyecto_final.data.mapper.toDto
import edu.ucne.proyecto_final.data.mapper.toEntity
import edu.ucne.proyecto_final.data.remote.RemoteDataSource
import edu.ucne.proyecto_final.data.remote.Resource
import edu.ucne.proyecto_final.data.remote.dto.CategoriaDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoriaRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val categoriaDao: CategoriaDao
) {

    fun getCategorias(): Flow<Resource<List<CategoriaDto>>> = flow {
        emit(Resource.Loading())

        //  Emitir datos locales primero
        val localData = categoriaDao.getAll().map { list -> list.map { it.toDto() } }.first()
        if (localData.isNotEmpty()) emit(Resource.Success(localData))

        // Intentar sincronizar con la API
        try {
            val remoteData = remoteDataSource.getCategorias()
            categoriaDao.upsertAll(remoteData.map { it.toEntity() })
            emit(Resource.Success(remoteData))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al obtener categorías"))
        }
    }

    suspend fun getCategoria(id: Int): Resource<CategoriaDto> = try {
        val local = categoriaDao.find(id)
        if (local != null) Resource.Success(local.toDto())
        else {
            val remote = remoteDataSource.getCategoria(id)
            categoriaDao.upsert(remote.toEntity())
            Resource.Success(remote)
        }
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al obtener categoría")
    }

    suspend fun createCategoria(categoria: CategoriaDto): Resource<CategoriaDto> = try {
        val created = remoteDataSource.createCategoria(categoria)
        categoriaDao.upsert(created.toEntity())
        Resource.Success(created)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al crear categoría")
    }

    suspend fun updateCategoria(id: Int, categoria: CategoriaDto): Resource<CategoriaDto> = try {
        val updated = remoteDataSource.updateCategoria(id, categoria)
        categoriaDao.upsert(updated.toEntity())
        Resource.Success(updated)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al actualizar categoría")
    }

    suspend fun deleteCategoria(id: Int): Resource<Unit> = try {
        remoteDataSource.deleteCategoria(id)
        categoriaDao.find(id)?.let { categoriaDao.delete(it) }
        Resource.Success(Unit)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al eliminar categoría")
    }
}
