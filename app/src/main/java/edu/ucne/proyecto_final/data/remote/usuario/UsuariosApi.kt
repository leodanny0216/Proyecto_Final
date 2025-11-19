package edu.ucne.proyecto_final.data.remote.usuario

import edu.ucne.proyecto_final.dto.remote.UsuarioDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UsuarioApi {

    @GET("api/Usuarios")
    suspend fun getUsuarios(): List<UsuarioDto>

    @GET("api/Usuarios/{id}")
    suspend fun getUsuario(@Path("id") id: Int): UsuarioDto

    @POST("api/Usuarios")
    suspend fun createUsuario(@Body usuario: UsuarioDto): UsuarioDto

    @PUT("api/Usuarios/{id}")
    suspend fun updateUsuario(@Path("id") id: Int, @Body usuario: UsuarioDto): UsuarioDto

    @DELETE("api/Usuarios/{id}")
    suspend fun deleteUsuario(@Path("id") id: Int)
}