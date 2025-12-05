package edu.ucne.proyecto_final.data.repository

import edu.ucne.proyecto_final.data.local.dao.ClienteDao
import edu.ucne.proyecto_final.data.mapper.toDto
import edu.ucne.proyecto_final.data.mapper.toEntity
import edu.ucne.proyecto_final.data.remote.RemoteDataSource
import edu.ucne.proyecto_final.data.remote.Resource
import edu.ucne.proyecto_final.data.remote.dto.ClienteDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ClienteRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val clienteDao: ClienteDao
) {

    fun getClientes(): Flow<Resource<List<ClienteDto>>> = flow {
        emit(Resource.Loading())

        // Emitir datos locales primero
        val localData = clienteDao.getAll()
            .map { list -> list.map { it.toDto() } }
            .first()
        if (localData.isNotEmpty()) emit(Resource.Success(localData))

        // Intentar sincronizar con la API
        try {
            val remoteData = remoteDataSource.getClientes()
            // Guardar en base local
            remoteData.forEach { clienteDao.save(it.toEntity()) }
            emit(Resource.Success(remoteData))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al obtener clientes"))
        }
    }

    suspend fun getCliente(id: Int): Resource<ClienteDto> = try {
        val local = clienteDao.find(id)
        if (local != null) Resource.Success(local.toDto())
        else {
            val remote = remoteDataSource.getCliente(id)
            clienteDao.save(remote.toEntity())
            Resource.Success(remote)
        }
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al obtener cliente")
    }

    suspend fun createCliente(cliente: ClienteDto): Resource<ClienteDto> = try {
        val created = remoteDataSource.createCliente(cliente)
        clienteDao.save(created.toEntity())
        Resource.Success(created)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al crear cliente")
    }

    suspend fun updateCliente(id: Int, cliente: ClienteDto): Resource<ClienteDto> = try {
        val updated = remoteDataSource.updateCliente(id, cliente)
        clienteDao.save(updated.toEntity())
        Resource.Success(updated)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al actualizar cliente")
    }

    suspend fun deleteCliente(id: Int): Resource<Unit> = try {
        remoteDataSource.deleteCliente(id)
        clienteDao.find(id)?.let { clienteDao.delete(it) }
        Resource.Success(Unit)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al eliminar cliente")
    }
}
