package edu.ucne.proyecto_final.data.repository

import edu.ucne.proyecto_final.data.remote.RemoteDataSource
import edu.ucne.proyecto_final.data.remote.Resource
import edu.ucne.proyecto_final.data.remote.dto.CategoriaDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CategoriaRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {
    fun getCategorias(): Flow<Resource<List<CategoriaDto>>> = flow {
        emit(Resource.Loading())
        try {
            val result = remoteDataSource.getCategorias()
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error desconocido"))
        }
    }

    suspend fun getCategoria(id: Int): Resource<CategoriaDto> = try {
        Resource.Success(remoteDataSource.getCategoria(id))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al obtener categoría")
    }

    suspend fun createCategoria(categoria: CategoriaDto): Resource<CategoriaDto> = try {
        Resource.Success(remoteDataSource.createCategoria(categoria))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al crear categoría")
    }

    suspend fun updateCategoria(id: Int, categoria: CategoriaDto): Resource<CategoriaDto> = try {
        Resource.Success(remoteDataSource.updateCategoria(id, categoria))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al actualizar categoría")
    }

    suspend fun deleteCategoria(id: Int): Resource<Unit> = try {
        remoteDataSource.deleteCategoria(id)
        Resource.Success(Unit)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al eliminar categoría")
    }
}