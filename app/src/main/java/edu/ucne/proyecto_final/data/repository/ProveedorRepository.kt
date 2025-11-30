package edu.ucne.proyecto_final.data.repository

import edu.ucne.proyecto_final.data.remote.RemoteDataSource
import edu.ucne.proyecto_final.data.remote.Resource
import edu.ucne.proyecto_final.dto.remote.ProveedorDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProveedorRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {
    fun getProveedores(): Flow<Resource<List<ProveedorDto>>> = flow {
        emit(Resource.Loading())
        try {
            val result = remoteDataSource.getProveedores()
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error desconocido"))
        }
    }

    suspend fun getProveedor(id: Int): Resource<ProveedorDto> = try {
        Resource.Success(remoteDataSource.getProveedor(id))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al obtener proveedor")
    }

    suspend fun createProveedor(proveedor: ProveedorDto): Resource<ProveedorDto> = try {
        Resource.Success(remoteDataSource.createProveedor(proveedor))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al crear proveedor")
    }

    suspend fun updateProveedor(id: Int, proveedor: ProveedorDto): Resource<ProveedorDto> = try {
        Resource.Success(remoteDataSource.updateProveedor(id, proveedor))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al actualizar proveedor")
    }

    suspend fun deleteProveedor(id: Int): Resource<Unit> = try {
        remoteDataSource.deleteProveedor(id)
        Resource.Success(Unit)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al eliminar proveedor")
    }
}