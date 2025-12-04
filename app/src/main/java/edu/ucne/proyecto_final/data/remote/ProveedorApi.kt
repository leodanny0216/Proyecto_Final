package edu.ucne.proyecto_final.data.remote

import edu.ucne.proyecto_final.dto.remote.ProveedorDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
interface ProveedorApi {
    @GET("api/ProveedorDtoes") suspend fun getProveedores(): List<ProveedorDto>
    @GET("api/ProveedorDtoes/{id}") suspend fun getProveedor(@Path("id") id: Int): ProveedorDto
    @POST("api/ProveedorDtoes") suspend fun createProveedor(@Body proveedor: ProveedorDto): ProveedorDto
    @PUT("api/ProveedorDtoes/{id}") suspend fun updateProveedor(@Path("id") id: Int, @Body proveedor: ProveedorDto): ProveedorDto
    @DELETE("api/ProveedorDtoes/{id}") suspend fun deleteProveedor(@Path("id") id: Int)
}
