package edu.ucne.proyecto_final.data.remote

import edu.ucne.proyecto_final.data.remote.dto.CategoriaDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CategoriaApi {
    @GET("api/CategoriaDtoes") suspend fun getCategorias(): List<CategoriaDto>
    @GET("api/CategoriaDtoes/{id}") suspend fun getCategoria(@Path("id") id: Int): CategoriaDto
    @POST("api/CategoriaDtoes") suspend fun createCategoria(@Body categoria: CategoriaDto): CategoriaDto
    @PUT("api/CategoriaDtoes/{id}") suspend fun updateCategoria(@Path("id") id: Int, @Body categoria: CategoriaDto): CategoriaDto
    @DELETE("api/CategoriaDtoes/{id}") suspend fun deleteCategoria(@Path("id") id: Int)
}