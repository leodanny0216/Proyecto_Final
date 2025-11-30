package edu.ucne.proyecto_final.data.repository

import edu.ucne.proyecto_final.data.remote.RemoteDataSource
import edu.ucne.proyecto_final.data.remote.Resource
import edu.ucne.proyecto_final.data.remote.dto.InsumoDetalleDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class InsumoDetalleRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {
    fun getInsumoDetalles(): Flow<Resource<List<InsumoDetalleDto>>> = flow {
        emit(Resource.Loading())
        try {
            val result = remoteDataSource.getInsumoDetalles()
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error desconocido"))
        }
    }

    suspend fun getInsumoDetalle(id: Int): Resource<InsumoDetalleDto> = try {
        Resource.Success(remoteDataSource.getInsumoDetalle(id))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al obtener detalle de insumo")
    }

    suspend fun createInsumoDetalle(detalle: InsumoDetalleDto): Resource<InsumoDetalleDto> = try {
        Resource.Success(remoteDataSource.createInsumoDetalle(detalle))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al crear detalle de insumo")
    }

    suspend fun updateInsumoDetalle(id: Int, detalle: InsumoDetalleDto): Resource<InsumoDetalleDto> = try {
        Resource.Success(remoteDataSource.updateInsumoDetalle(id, detalle))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al actualizar detalle de insumo")
    }

    suspend fun deleteInsumoDetalle(id: Int): Resource<Unit> = try {
        remoteDataSource.deleteInsumoDetalle(id)
        Resource.Success(Unit)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al eliminar detalle de insumo")
    }
}