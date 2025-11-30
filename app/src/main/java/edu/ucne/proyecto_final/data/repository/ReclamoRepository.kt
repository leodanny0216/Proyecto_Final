package edu.ucne.proyecto_final.data.repository

import edu.ucne.proyecto_final.data.remote.RemoteDataSource
import edu.ucne.proyecto_final.data.remote.Resource
import edu.ucne.proyecto_final.data.remote.dto.ReclamoDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ReclamoRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {
    fun getReclamos(): Flow<Resource<List<ReclamoDto>>> = flow {
        emit(Resource.Loading())
        try {
            val result = remoteDataSource.getReclamos()
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error desconocido"))
        }
    }

    suspend fun getReclamo(id: Int): Resource<ReclamoDto> = try {
        Resource.Success(remoteDataSource.getReclamo(id))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al obtener reclamo")
    }

    suspend fun createReclamo(reclamo: ReclamoDto): Resource<ReclamoDto> = try {
        Resource.Success(remoteDataSource.createReclamo(reclamo))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al crear reclamo")
    }

    suspend fun updateReclamo(id: Int, reclamo: ReclamoDto): Resource<ReclamoDto> = try {
        Resource.Success(remoteDataSource.updateReclamo(id, reclamo))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al actualizar reclamo")
    }

    suspend fun deleteReclamo(id: Int): Resource<Unit> = try {
        remoteDataSource.deleteReclamo(id)
        Resource.Success(Unit)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al eliminar reclamo")
    }
}