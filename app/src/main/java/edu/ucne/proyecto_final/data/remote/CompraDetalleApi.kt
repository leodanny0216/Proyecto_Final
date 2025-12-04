package edu.ucne.proyecto_final.data.remote

import edu.ucne.proyecto_final.data.remote.dto.CompraDetalleDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CompraDetalleApi {
    @GET("api/CompraDetalleDtoes")
    suspend fun getCompraDetalles(): List<CompraDetalleDto>

    @GET("api/CompraDetalleDtoes/{id}")
    suspend fun getCompraDetalle(@Path("id") id: Int): CompraDetalleDto

    @GET("api/CompraDtoes/{compraId}/detalles")
    suspend fun getDetallesByCompra(@Path("compraId") compraId: Int): List<CompraDetalleDto>

    @POST("api/CompraDetalleDtoes")
    suspend fun createCompraDetalle(@Body detalle: CompraDetalleDto): CompraDetalleDto

    @PUT("api/CompraDetalleDtoes/{id}")
    suspend fun updateCompraDetalle(@Path("id") id: Int, @Body detalle: CompraDetalleDto): CompraDetalleDto

    @DELETE("api/CompraDetalleDtoes/{id}")
    suspend fun deleteCompraDetalle(@Path("id") id: Int)
}

