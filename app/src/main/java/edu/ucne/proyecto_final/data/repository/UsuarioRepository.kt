package edu.ucne.proyecto_final.data.repository

import edu.ucne.proyecto_final.data.local.dao.UsuarioDao
import edu.ucne.proyecto_final.data.mapper.toDto
import edu.ucne.proyecto_final.data.mapper.toEntity
import edu.ucne.proyecto_final.data.remote.RemoteDataSource
import edu.ucne.proyecto_final.data.remote.Resource
import edu.ucne.proyecto_final.dto.remote.UsuarioDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UsuarioRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val usuarioDao: UsuarioDao
) {

    fun getUsuarios(): Flow<Resource<List<UsuarioDto>>> = flow {
        emit(Resource.Loading())

        // Emitir datos locales primero
        val localData = usuarioDao.getAll()
            .map { list -> list.map { it.toDto() } }
            .first()
        if (localData.isNotEmpty()) emit(Resource.Success(localData))

        //  Intentar sincronizar con la API
        try {
            val remoteData = remoteDataSource.getUsuarios()
            usuarioDao.saveAll(remoteData.map { it.toEntity() })
            emit(Resource.Success(remoteData))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al obtener usuarios"))
        }
    }

    suspend fun getUsuario(id: Int): Resource<UsuarioDto> = try {
        val local = usuarioDao.find(id)
        if (local != null) Resource.Success(local.toDto())
        else {
            val remote = remoteDataSource.getUsuario(id)
            usuarioDao.save(remote.toEntity())
            Resource.Success(remote)
        }
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al obtener usuario")
    }

    suspend fun createUsuario(usuario: UsuarioDto): Resource<UsuarioDto> = try {
        val created = remoteDataSource.createUsuario(usuario)
        usuarioDao.save(created.toEntity())
        Resource.Success(created)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al crear usuario")
    }

    suspend fun updateUsuario(id: Int, usuario: UsuarioDto): Resource<UsuarioDto> = try {
        val updated = remoteDataSource.updateUsuario(id, usuario)
        usuarioDao.save(updated.toEntity())
        Resource.Success(updated)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al actualizar usuario")
    }

    suspend fun deleteUsuario(id: Int): Resource<Unit> = try {
        remoteDataSource.deleteUsuario(id)
        usuarioDao.find(id)?.let { usuarioDao.delete(it) }
        Resource.Success(Unit)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al eliminar usuario")
    }
}
