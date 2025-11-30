package edu.ucne.proyecto_final.data.repository

import edu.ucne.proyecto_final.data.remote.RemoteDataSource
import edu.ucne.proyecto_final.data.remote.Resource
import edu.ucne.proyecto_final.data.remote.dto.ClienteDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ClienteRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {
    fun getClientes(): Flow<Resource<List<ClienteDto>>> = flow {
        emit(Resource.Loading())
        try {
            val result = remoteDataSource.getClientes()
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error desconocido"))
        }
    }

    suspend fun getCliente(id: Int): Resource<ClienteDto> = try {
        Resource.Success(remoteDataSource.getCliente(id))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al obtener cliente")
    }

    suspend fun createCliente(cliente: ClienteDto): Resource<ClienteDto> = try {
        Resource.Success(remoteDataSource.createCliente(cliente))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al crear cliente")
    }

    suspend fun updateCliente(id: Int, cliente: ClienteDto): Resource<ClienteDto> = try {
        Resource.Success(remoteDataSource.updateCliente(id, cliente))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al actualizar cliente")
    }

    suspend fun deleteCliente(id: Int): Resource<Unit> = try {
        remoteDataSource.deleteCliente(id)
        Resource.Success(Unit)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al eliminar cliente")
    }
}