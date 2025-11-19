package edu.ucne.proyecto_final.data.remote.usuario


import edu.ucne.proyecto_final.dto.remote.UsuarioDto
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val usuarioApi: UsuarioApi
){
    suspend fun getUsuarios(): List<UsuarioDto> = usuarioApi.getUsuarios()
    suspend fun getUsuario(id: Int): UsuarioDto = usuarioApi.getUsuario(id)
    suspend fun createUsuario(usuario: UsuarioDto): UsuarioDto = usuarioApi.createUsuario(usuario)
    suspend fun updateUsuario(id: Int, usuario: UsuarioDto): UsuarioDto = usuarioApi.updateUsuario(id, usuario)
    suspend fun deleteUsuario(id: Int) = usuarioApi.deleteUsuario(id)
}