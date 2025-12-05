package edu.ucne.proyecto_final.data.repository

import edu.ucne.proyecto_final.data.local.dao.CompraDao
import edu.ucne.proyecto_final.data.mapper.toDto
import edu.ucne.proyecto_final.data.mapper.toEntity
import edu.ucne.proyecto_final.data.remote.RemoteDataSource
import edu.ucne.proyecto_final.data.remote.Resource
import edu.ucne.proyecto_final.data.remote.dto.CompraDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CompraRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val compraDao: CompraDao
) {

    fun getCompras(): Flow<Resource<List<CompraDto>>> = flow {
        emit(Resource.Loading())

        val localData = compraDao.getAll()
            .map { list -> list.map { it.toDto() } }
            .first()
        if (localData.isNotEmpty()) emit(Resource.Success(localData))

        try {
            val remoteData = remoteDataSource.getCompras()
            compraDao.saveAll(remoteData.map { it.toEntity() })
            emit(Resource.Success(remoteData))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al obtener compras"))
        }
    }

    suspend fun getCompra(id: Int): Resource<CompraDto> = try {
        val local = compraDao.find(id)
        if (local != null) Resource.Success(local.toDto())
        else {
            val remote = remoteDataSource.getCompra(id)
            compraDao.save(remote.toEntity())
            Resource.Success(remote)
        }
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al obtener compra")
    }

    suspend fun createCompra(compra: CompraDto): Resource<CompraDto> = try {
        val created = remoteDataSource.createCompra(compra)
        compraDao.save(created.toEntity())
        Resource.Success(created)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al crear compra")
    }

    suspend fun updateCompra(id: Int, compra: CompraDto): Resource<CompraDto> = try {
        val updated = remoteDataSource.updateCompra(id, compra)
        compraDao.save(updated.toEntity())
        Resource.Success(updated)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al actualizar compra")
    }

    suspend fun deleteCompra(id: Int): Resource<Unit> = try {
        remoteDataSource.deleteCompra(id)
        compraDao.find(id)?.let { compraDao.delete(it) }
        Resource.Success(Unit)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al eliminar compra")
    }
}
