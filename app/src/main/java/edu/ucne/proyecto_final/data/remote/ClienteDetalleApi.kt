package edu.ucne.proyecto_final.data.remote

import edu.ucne.proyecto_final.data.remote.dto.ClienteDetalleDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
interface ClienteDetalleApi {
    @GET("api/ClienteDetalleDtoes") suspend fun getClienteDetalles(): List<ClienteDetalleDto>
    @GET("api/ClienteDetalleDtoes/{id}") suspend fun getClienteDetalle(@Path("id") id: Int): ClienteDetalleDto
    @POST("api/ClienteDetalleDtoes") suspend fun createClienteDetalle(@Body detalle: ClienteDetalleDto): ClienteDetalleDto
    @PUT("api/ClienteDetalleDtoes/{id}") suspend fun updateClienteDetalle(@Path("id") id: Int, @Body detalle: ClienteDetalleDto): ClienteDetalleDto
    @DELETE("api/ClienteDetalleDtoes/{id}") suspend fun deleteClienteDetalle(@Path("id") id: Int)
}