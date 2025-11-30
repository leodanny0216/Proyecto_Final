package edu.ucne.proyecto_final.data.repository

import edu.ucne.proyecto_final.data.remote.RemoteDataSource
import edu.ucne.proyecto_final.data.remote.Resource
import edu.ucne.proyecto_final.data.remote.dto.CompraDetalleDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CompraDetalleRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {
    fun getCompraDetalles(): Flow<Resource<List<CompraDetalleDto>>> = flow {
        emit(Resource.Loading())
        try {
            val result = remoteDataSource.getCompraDetalles()
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error desconocido"))
        }
    }

    suspend fun getCompraDetalle(id: Int): Resource<CompraDetalleDto> = try {
        Resource.Success(remoteDataSource.getCompraDetalle(id))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al obtener detalle de compra")
    }

    suspend fun createCompraDetalle(detalle: CompraDetalleDto): Resource<CompraDetalleDto> = try {
        Resource.Success(remoteDataSource.createCompraDetalle(detalle))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al crear detalle de compra")
    }

    suspend fun updateCompraDetalle(id: Int, detalle: CompraDetalleDto): Resource<CompraDetalleDto> = try {
        Resource.Success(remoteDataSource.updateCompraDetalle(id, detalle))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al actualizar detalle de compra")
    }

    suspend fun deleteCompraDetalle(id: Int): Resource<Unit> = try {
        remoteDataSource.deleteCompraDetalle(id)
        Resource.Success(Unit)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al eliminar detalle de compra")
    }
}