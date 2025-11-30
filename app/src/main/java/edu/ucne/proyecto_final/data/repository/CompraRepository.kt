package edu.ucne.proyecto_final.data.repository

import edu.ucne.proyecto_final.data.remote.RemoteDataSource
import edu.ucne.proyecto_final.data.remote.Resource
import edu.ucne.proyecto_final.data.remote.dto.CompraDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CompraRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {
    fun getCompras(): Flow<Resource<List<CompraDto>>> = flow {
        emit(Resource.Loading())
        try {
            val result = remoteDataSource.getCompras()
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error desconocido"))
        }
    }

    suspend fun getCompra(id: Int): Resource<CompraDto> = try {
        Resource.Success(remoteDataSource.getCompra(id))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al obtener compra")
    }

    suspend fun createCompra(compra: CompraDto): Resource<CompraDto> = try {
        Resource.Success(remoteDataSource.createCompra(compra))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al crear compra")
    }

    suspend fun updateCompra(id: Int, compra: CompraDto): Resource<CompraDto> = try {
        Resource.Success(remoteDataSource.updateCompra(id, compra))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al actualizar compra")
    }

    suspend fun deleteCompra(id: Int): Resource<Unit> = try {
        remoteDataSource.deleteCompra(id)
        Resource.Success(Unit)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al eliminar compra")
    }
}