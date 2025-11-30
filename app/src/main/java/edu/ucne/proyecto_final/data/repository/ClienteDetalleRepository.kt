package edu.ucne.proyecto_final.data.repository

import edu.ucne.proyecto_final.data.remote.RemoteDataSource
import edu.ucne.proyecto_final.data.remote.Resource
import edu.ucne.proyecto_final.data.remote.dto.ClienteDetalleDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ClienteDetalleRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {
    fun getClienteDetalles(): Flow<Resource<List<ClienteDetalleDto>>> = flow {
        emit(Resource.Loading())
        try {
            val result = remoteDataSource.getClienteDetalles()
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error desconocido"))
        }
    }

    suspend fun getClienteDetalle(id: Int): Resource<ClienteDetalleDto> = try {
        Resource.Success(remoteDataSource.getClienteDetalle(id))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al obtener detalle del cliente")
    }

    suspend fun createClienteDetalle(detalle: ClienteDetalleDto): Resource<ClienteDetalleDto> = try {
        Resource.Success(remoteDataSource.createClienteDetalle(detalle))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al crear detalle del cliente")
    }

    suspend fun updateClienteDetalle(id: Int, detalle: ClienteDetalleDto): Resource<ClienteDetalleDto> = try {
        Resource.Success(remoteDataSource.updateClienteDetalle(id, detalle))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al actualizar detalle del cliente")
    }

    suspend fun deleteClienteDetalle(id: Int): Resource<Unit> = try {
        remoteDataSource.deleteClienteDetalle(id)
        Resource.Success(Unit)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al eliminar detalle del cliente")
    }
}