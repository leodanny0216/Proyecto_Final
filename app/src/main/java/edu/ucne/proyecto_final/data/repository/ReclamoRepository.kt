package edu.ucne.proyecto_final.data.repository

import edu.ucne.proyecto_final.data.local.dao.ReclamoDao
import edu.ucne.proyecto_final.data.mapper.toDto
import edu.ucne.proyecto_final.data.mapper.toEntity
import edu.ucne.proyecto_final.data.remote.RemoteDataSource
import edu.ucne.proyecto_final.data.remote.Resource
import edu.ucne.proyecto_final.data.remote.dto.ReclamoDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ReclamoRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val reclamoDao: ReclamoDao
) {

    fun getReclamos(): Flow<Resource<List<ReclamoDto>>> = flow {
        emit(Resource.Loading())

        val localData = reclamoDao.getAll().map { it.map { r -> r.toDto() } }.first()
        if (localData.isNotEmpty()) emit(Resource.Success(localData))

        try {
            val remoteData = remoteDataSource.getReclamos()
            reclamoDao.saveAll(remoteData.map { it.toEntity() })
            emit(Resource.Success(remoteData))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al obtener reclamos"))
        }
    }

    suspend fun getReclamo(id: Int): Resource<ReclamoDto> = try {
        val local = reclamoDao.find(id)
        if (local != null) Resource.Success(local.toDto())
        else {
            val remote = remoteDataSource.getReclamo(id)
            reclamoDao.save(remote.toEntity())
            Resource.Success(remote)
        }
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al obtener reclamo")
    }

    suspend fun createReclamo(reclamo: ReclamoDto): Resource<ReclamoDto> = try {
        val created = remoteDataSource.createReclamo(reclamo)
        reclamoDao.save(created.toEntity())
        Resource.Success(created)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al crear reclamo")
    }

    suspend fun updateReclamo(id: Int, reclamo: ReclamoDto): Resource<ReclamoDto> = try {
        val updated = remoteDataSource.updateReclamo(id, reclamo)
        reclamoDao.save(updated.toEntity())
        Resource.Success(updated)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al actualizar reclamo")
    }

    suspend fun deleteReclamo(id: Int): Resource<Unit> = try {
        remoteDataSource.deleteReclamo(id)
        reclamoDao.find(id)?.let { reclamoDao.delete(it) }
        Resource.Success(Unit)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al eliminar reclamo")
    }
}
