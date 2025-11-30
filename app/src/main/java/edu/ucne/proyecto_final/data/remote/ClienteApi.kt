package edu.ucne.proyecto_final.data.remote

import edu.ucne.proyecto_final.data.remote.dto.ClienteDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ClienteApi {
    @GET("api/ClienteDtoes") suspend fun getClientes(): List<ClienteDto>
    @GET("api/ClienteDtoes/{id}") suspend fun getCliente(@Path("id") id: Int): ClienteDto
    @POST("api/ClienteDtoes") suspend fun createCliente(@Body cliente: ClienteDto): ClienteDto
    @PUT("api/ClienteDtoes/{id}") suspend fun updateCliente(@Path("id") id: Int, @Body cliente: ClienteDto): ClienteDto
    @DELETE("api/ClienteDtoes/{id}") suspend fun deleteCliente(@Path("id") id: Int)
}