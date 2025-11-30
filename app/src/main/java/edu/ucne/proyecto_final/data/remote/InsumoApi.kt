package edu.ucne.proyecto_final.data.remote
import edu.ucne.proyecto_final.data.remote.dto.InsumoDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
interface InsumoApi {
    @GET("api/InsumoDtoes") suspend fun getInsumos(): List<InsumoDto>
    @GET("api/InsumoDtoes/{id}") suspend fun getInsumo(@Path("id") id: Int): InsumoDto
    @POST("api/InsumoDtoes") suspend fun createInsumo(@Body insumo: InsumoDto): InsumoDto
    @PUT("api/InsumoDtoes/{id}") suspend fun updateInsumo(@Path("id") id: Int, @Body insumo: InsumoDto): InsumoDto
    @DELETE("api/InsumoDtoes/{id}") suspend fun deleteInsumo(@Path("id") id: Int)
}
