package edu.ucne.proyecto_final.data.repository

import edu.ucne.proyecto_final.data.local.dao.ProveedorDao
import edu.ucne.proyecto_final.data.mapper.toDto
import edu.ucne.proyecto_final.data.mapper.toEntity
import edu.ucne.proyecto_final.data.remote.RemoteDataSource
import edu.ucne.proyecto_final.data.remote.Resource
import edu.ucne.proyecto_final.dto.remote.ProveedorDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProveedorRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val proveedorDao: ProveedorDao
) {

    fun getProveedores(): Flow<Resource<List<ProveedorDto>>> = flow {
        emit(Resource.Loading())

        val localData = proveedorDao.getAll().map { it.map { p -> p.toDto() } }.first()
        if (localData.isNotEmpty()) emit(Resource.Success(localData))

        try {
            val remoteData = remoteDataSource.getProveedores()
            proveedorDao.saveAll(remoteData.map { it.toEntity() })
            emit(Resource.Success(remoteData))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al obtener proveedores"))
        }
    }

    suspend fun getProveedor(id: Int): Resource<ProveedorDto> = try {
        val local = proveedorDao.find(id)
        if (local != null) Resource.Success(local.toDto())
        else {
            val remote = remoteDataSource.getProveedor(id)
            proveedorDao.save(remote.toEntity())
            Resource.Success(remote)
        }
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al obtener proveedor")
    }

    suspend fun createProveedor(proveedor: ProveedorDto): Resource<ProveedorDto> = try {
        val created = remoteDataSource.createProveedor(proveedor)
        proveedorDao.save(created.toEntity())
        Resource.Success(created)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al crear proveedor")
    }

    suspend fun updateProveedor(id: Int, proveedor: ProveedorDto): Resource<ProveedorDto> = try {
        val updated = remoteDataSource.updateProveedor(id, proveedor)
        proveedorDao.save(updated.toEntity())
        Resource.Success(updated)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al actualizar proveedor")
    }

    suspend fun deleteProveedor(id: Int): Resource<Unit> = try {
        remoteDataSource.deleteProveedor(id)
        proveedorDao.find(id)?.let { proveedorDao.delete(it) }
        Resource.Success(Unit)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al eliminar proveedor")
    }
}
