package edu.ucne.proyecto_final.data.repository

import edu.ucne.proyecto_final.data.remote.RemoteDataSource
import edu.ucne.proyecto_final.data.remote.Resource
import edu.ucne.proyecto_final.data.remote.dto.InsumoDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class InsumoRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {
    fun getInsumos(): Flow<Resource<List<InsumoDto>>> = flow {
        emit(Resource.Loading())
        try {
            val result = remoteDataSource.getInsumos()
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error desconocido"))
        }
    }

    suspend fun getInsumo(id: Int): Resource<InsumoDto> = try {
        Resource.Success(remoteDataSource.getInsumo(id))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al obtener insumo")
    }

    suspend fun createInsumo(insumo: InsumoDto): Resource<InsumoDto> = try {
        Resource.Success(remoteDataSource.createInsumo(insumo))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al crear insumo")
    }

    suspend fun updateInsumo(id: Int, insumo: InsumoDto): Resource<InsumoDto> = try {
        Resource.Success(remoteDataSource.updateInsumo(id, insumo))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al actualizar insumo")
    }

    suspend fun deleteInsumo(id: Int): Resource<Unit> = try {
        remoteDataSource.deleteInsumo(id)
        Resource.Success(Unit)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al eliminar insumo")
    }
}