package edu.ucne.proyecto_final.data.repository
import edu.ucne.proyecto_final.data.remote.Resource
import edu.ucne.proyecto_final.data.remote.usuario.RemoteDataSource
import edu.ucne.proyecto_final.dto.remote.UsuarioDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UsuarioRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
){

    fun getUsuarios(): Flow<Resource<List<UsuarioDto>>> = flow {
        emit(Resource.Loading())
        try {
            val result = remoteDataSource.getUsuarios()
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error desconocido"))
        }
    }

    suspend fun getUsuario(id: Int): Resource<UsuarioDto> = try {
        Resource.Success(remoteDataSource.getUsuario(id))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al obtener usuario")
    }

    suspend fun createUsuario(usuario: UsuarioDto): Resource<UsuarioDto> = try {
        Resource.Success(remoteDataSource.createUsuario(usuario))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al crear usuario")
    }

    suspend fun updateUsuario(id: Int, usuario: UsuarioDto): Resource<UsuarioDto> = try {
        Resource.Success(remoteDataSource.updateUsuario(id, usuario))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al actualizar usuario")
    }

    suspend fun deleteUsuario(id: Int): Resource<Unit> = try {
        remoteDataSource.deleteUsuario(id)
        Resource.Success(Unit)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al eliminar usuario")
    }
}