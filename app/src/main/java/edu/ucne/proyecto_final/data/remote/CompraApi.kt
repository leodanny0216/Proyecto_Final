package edu.ucne.proyecto_final.data.remote
import edu.ucne.proyecto_final.data.remote.dto.CompraDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
interface CompraApi {
    @GET("api/CompraDtoes") suspend fun getCompras(): List<CompraDto>
    @GET("api/CompraDtoes/{id}") suspend fun getCompra(@Path("id") id: Int): CompraDto
    @POST("api/CompraDtoes") suspend fun createCompra(@Body compra: CompraDto): CompraDto
    @PUT("api/CompraDtoes/{id}") suspend fun updateCompra(@Path("id") id: Int, @Body compra: CompraDto): CompraDto
    @DELETE("api/CompraDtoes/{id}") suspend fun deleteCompra(@Path("id") id: Int)
}