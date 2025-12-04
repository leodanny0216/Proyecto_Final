// package edu.ucne.proyecto_final.data.remote
package edu.ucne.proyecto_final.data.remote

import edu.ucne.proyecto_final.data.remote.dto.ReclamoDto
import retrofit2.http.*
interface ReclamoApi {
    @GET("api/ReclamoDtoes")
    suspend fun getReclamos(): List<ReclamoDto>

    @GET("api/ReclamoDtoes/{id}")
    suspend fun getReclamo(@Path("id") id: Int): ReclamoDto

    @POST("api/ReclamoDtoes")
    suspend fun createReclamo(@Body reclamo: ReclamoDto): ReclamoDto

    @PUT("api/ReclamoDtoes/{id}")
    suspend fun updateReclamo(@Path("id") id: Int, @Body reclamo: ReclamoDto): ReclamoDto

    @DELETE("api/ReclamoDtoes/{id}")
    suspend fun deleteReclamo(@Path("id") id: Int)
}