package edu.ucne.proyecto_final.data.remote
import edu.ucne.proyecto_final.data.remote.dto.InsumoDetalleDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
interface InsumoDetalleApi {
    @GET("api/InsumoDetalleDtoes") suspend fun getInsumoDetalles(): List<InsumoDetalleDto>
    @GET("api/InsumoDetalleDtoes/{id}") suspend fun getInsumoDetalle(@Path("id") id: Int): InsumoDetalleDto
    @POST("api/InsumoDetalleDtoes") suspend fun createInsumoDetalle(@Body detalle: InsumoDetalleDto): InsumoDetalleDto
    @PUT("api/InsumoDetalleDtoes/{id}") suspend fun updateInsumoDetalle(@Path("id") id: Int, @Body detalle: InsumoDetalleDto): InsumoDetalleDto
    @DELETE("api/InsumoDetalleDtoes/{id}") suspend fun deleteInsumoDetalle(@Path("id") id: Int)
}
