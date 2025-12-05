package edu.ucne.proyecto_final.data.repository

import edu.ucne.proyecto_final.data.local.dao.CompraDetalleDao
import edu.ucne.proyecto_final.data.mapper.toDto
import edu.ucne.proyecto_final.data.mapper.toEntity
import edu.ucne.proyecto_final.data.remote.RemoteDataSource
import edu.ucne.proyecto_final.data.remote.Resource
import edu.ucne.proyecto_final.data.remote.dto.CompraDetalleDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CompraDetalleRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val compraDetalleDao: CompraDetalleDao
) {

    fun getCompraDetalles(compraId: Int = 0): Flow<Resource<List<CompraDetalleDto>>> = flow {
        emit(Resource.Loading())

        // Datos locales primero
        val localData = if (compraId > 0) {
            compraDetalleDao.getByCompra(compraId)
        } else {
            compraDetalleDao.getByCompra(0) // todos si quieres
        }.map { list -> list.map { it.toDto() } }.first()

        if (localData.isNotEmpty()) emit(Resource.Success(localData))

        // Intentar sincronizar con API
        try {
            val remoteData = remoteDataSource.getCompraDetalles()
            compraDetalleDao.saveAll(remoteData.map { it.toEntity() })
            emit(Resource.Success(remoteData))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al obtener detalles de compra"))
        }
    }

    suspend fun getCompraDetalle(id: Int): Resource<CompraDetalleDto> = try {
        val local = compraDetalleDao.find(id)
        if (local != null) Resource.Success(local.toDto())
        else {
            val remote = remoteDataSource.getCompraDetalle(id)
            compraDetalleDao.save(remote.toEntity())
            Resource.Success(remote)
        }
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al obtener detalle de compra")
    }

    suspend fun createCompraDetalle(detalle: CompraDetalleDto): Resource<CompraDetalleDto> = try {
        val created = remoteDataSource.createCompraDetalle(detalle)
        compraDetalleDao.save(created.toEntity())
        Resource.Success(created)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al crear detalle de compra")
    }

    suspend fun updateCompraDetalle(id: Int, detalle: CompraDetalleDto): Resource<CompraDetalleDto> = try {
        val updated = remoteDataSource.updateCompraDetalle(id, detalle)
        compraDetalleDao.save(updated.toEntity())
        Resource.Success(updated)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al actualizar detalle de compra")
    }

    suspend fun deleteCompraDetalle(id: Int): Resource<Unit> = try {
        remoteDataSource.deleteCompraDetalle(id)
        compraDetalleDao.find(id)?.let { compraDetalleDao.delete(it) }
        Resource.Success(Unit)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al eliminar detalle de compra")
    }
}
