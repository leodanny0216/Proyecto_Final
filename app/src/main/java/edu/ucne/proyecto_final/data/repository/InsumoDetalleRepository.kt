package edu.ucne.proyecto_final.data.repository

import edu.ucne.proyecto_final.data.local.dao.InsumoDetalleDao
import edu.ucne.proyecto_final.data.mapper.toDto
import edu.ucne.proyecto_final.data.mapper.toEntity
import edu.ucne.proyecto_final.data.remote.RemoteDataSource
import edu.ucne.proyecto_final.data.remote.Resource
import edu.ucne.proyecto_final.data.remote.dto.InsumoDetalleDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class InsumoDetalleRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val insumoDetalleDao: InsumoDetalleDao
) {

    fun getInsumoDetalles(insumoId: Int = 0): Flow<Resource<List<InsumoDetalleDto>>> = flow {
        emit(Resource.Loading())

        val localData = if (insumoId > 0) {
            insumoDetalleDao.getByInsumo(insumoId)
        } else {
            insumoDetalleDao.getByInsumo(0)
        }.map { list -> list.map { it.toDto() } }.first()

        if (localData.isNotEmpty()) emit(Resource.Success(localData))

        try {
            val remoteData = remoteDataSource.getInsumoDetalles()
            insumoDetalleDao.saveAll(remoteData.map { it.toEntity() })
            emit(Resource.Success(remoteData))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al obtener detalles de insumo"))
        }
    }

    suspend fun getInsumoDetalle(id: Int): Resource<InsumoDetalleDto> = try {
        val local = insumoDetalleDao.find(id)
        if (local != null) Resource.Success(local.toDto())
        else {
            val remote = remoteDataSource.getInsumoDetalle(id)
            insumoDetalleDao.save(remote.toEntity())
            Resource.Success(remote)
        }
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al obtener detalle de insumo")
    }

    suspend fun createInsumoDetalle(detalle: InsumoDetalleDto): Resource<InsumoDetalleDto> = try {
        val created = remoteDataSource.createInsumoDetalle(detalle)
        insumoDetalleDao.save(created.toEntity())
        Resource.Success(created)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al crear detalle de insumo")
    }

    suspend fun updateInsumoDetalle(id: Int, detalle: InsumoDetalleDto): Resource<InsumoDetalleDto> = try {
        val updated = remoteDataSource.updateInsumoDetalle(id, detalle)
        insumoDetalleDao.save(updated.toEntity())
        Resource.Success(updated)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al actualizar detalle de insumo")
    }

    suspend fun deleteInsumoDetalle(id: Int): Resource<Unit> = try {
        remoteDataSource.deleteInsumoDetalle(id)
        insumoDetalleDao.find(id)?.let { insumoDetalleDao.delete(it) }
        Resource.Success(Unit)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al eliminar detalle de insumo")
    }
}
