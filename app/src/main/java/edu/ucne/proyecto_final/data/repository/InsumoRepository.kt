package edu.ucne.proyecto_final.data.repository

import edu.ucne.proyecto_final.data.local.dao.InsumoDao
import edu.ucne.proyecto_final.data.mapper.toDto
import edu.ucne.proyecto_final.data.mapper.toEntity
import edu.ucne.proyecto_final.data.remote.RemoteDataSource
import edu.ucne.proyecto_final.data.remote.Resource
import edu.ucne.proyecto_final.data.remote.dto.InsumoDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class InsumoRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val insumoDao: InsumoDao
) {

    fun getInsumos(): Flow<Resource<List<InsumoDto>>> = flow {
        emit(Resource.Loading())

        val localData = insumoDao.getAll().map { it.map { i -> i.toDto() } }.first()
        if (localData.isNotEmpty()) emit(Resource.Success(localData))

        try {
            val remoteData = remoteDataSource.getInsumos()
            insumoDao.saveAll(remoteData.map { it.toEntity() })
            emit(Resource.Success(remoteData))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al obtener insumos"))
        }
    }

    suspend fun getInsumo(id: Int): Resource<InsumoDto> = try {
        val local = insumoDao.find(id)
        if (local != null) Resource.Success(local.toDto())
        else {
            val remote = remoteDataSource.getInsumo(id)
            insumoDao.save(remote.toEntity())
            Resource.Success(remote)
        }
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al obtener insumo")
    }

    suspend fun createInsumo(insumo: InsumoDto): Resource<InsumoDto> = try {
        val created = remoteDataSource.createInsumo(insumo)
        insumoDao.save(created.toEntity())
        Resource.Success(created)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al crear insumo")
    }

    suspend fun updateInsumo(id: Int, insumo: InsumoDto): Resource<InsumoDto> = try {
        val updated = remoteDataSource.updateInsumo(id, insumo)
        insumoDao.save(updated.toEntity())
        Resource.Success(updated)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al actualizar insumo")
    }

    suspend fun deleteInsumo(id: Int): Resource<Unit> = try {
        remoteDataSource.deleteInsumo(id)
        insumoDao.find(id)?.let { insumoDao.delete(it) }
        Resource.Success(Unit)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al eliminar insumo")
    }
}
