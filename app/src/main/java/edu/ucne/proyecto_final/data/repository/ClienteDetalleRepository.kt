package edu.ucne.proyecto_final.data.repository

import edu.ucne.proyecto_final.data.local.dao.ClienteDetalleDao
import edu.ucne.proyecto_final.data.mapper.toDto
import edu.ucne.proyecto_final.data.mapper.toEntity
import edu.ucne.proyecto_final.data.remote.RemoteDataSource
import edu.ucne.proyecto_final.data.remote.Resource
import edu.ucne.proyecto_final.data.remote.dto.ClienteDetalleDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ClienteDetalleRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val clienteDetalleDao: ClienteDetalleDao
) {

    fun getClienteDetalles(): Flow<Resource<List<ClienteDetalleDto>>> = flow {
        emit(Resource.Loading())

        // Emitir datos locales primero
        val localData = clienteDetalleDao.getByCliente(0) // 0 o el clienteId si lo quieres filtrar
            .map { list -> list.map { it.toDto() } }
            .first()
        if (localData.isNotEmpty()) emit(Resource.Success(localData))

        // Intentar sincronizar con la API
        try {
            val remoteData = remoteDataSource.getClienteDetalles()
            clienteDetalleDao.saveAll(remoteData.map { it.toEntity() })
            emit(Resource.Success(remoteData))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al obtener detalles del cliente"))
        }
    }

    suspend fun getClienteDetalle(id: Int): Resource<ClienteDetalleDto> = try {
        val local = clienteDetalleDao.find(id)
        if (local != null) Resource.Success(local.toDto())
        else {
            val remote = remoteDataSource.getClienteDetalle(id)
            clienteDetalleDao.save(remote.toEntity())
            Resource.Success(remote)
        }
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al obtener detalle del cliente")
    }

    suspend fun createClienteDetalle(detalle: ClienteDetalleDto): Resource<ClienteDetalleDto> = try {
        val created = remoteDataSource.createClienteDetalle(detalle)
        clienteDetalleDao.save(created.toEntity())
        Resource.Success(created)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al crear detalle del cliente")
    }

    suspend fun updateClienteDetalle(id: Int, detalle: ClienteDetalleDto): Resource<ClienteDetalleDto> = try {
        val updated = remoteDataSource.updateClienteDetalle(id, detalle)
        clienteDetalleDao.save(updated.toEntity())
        Resource.Success(updated)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al actualizar detalle del cliente")
    }

    suspend fun deleteClienteDetalle(id: Int): Resource<Unit> = try {
        remoteDataSource.deleteClienteDetalle(id)
        clienteDetalleDao.find(id)?.let { clienteDetalleDao.delete(it) }
        Resource.Success(Unit)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al eliminar detalle del cliente")
    }
}
